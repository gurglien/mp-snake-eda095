package server;

import java.util.ArrayList;

import client.Position;
import client.ClientMonitor.GameState;
import client.Player.Move;

public class ServerMonitor {
	
	private Move[] nextMoves;
	private Move[] currentMoves;
	private boolean[] shouldGrow;
	private GameState[] clientStates = {GameState.NOT_READY, GameState.NOT_READY};
	private boolean[] moveRetreived = {true, true};
	private boolean movesChecked = false;
	private boolean serverReady;
	private boolean[] foodChanged = {false, false};
	private boolean[] growChanged = {false, false};
	private Position food;
	
	public ServerMonitor(){
		nextMoves = new Move[2];
		currentMoves = new Move[2];
		shouldGrow = new boolean[2];
		serverReady = false;
		
		nextMoves[0] = Move.RIGHT;
		nextMoves[1] = Move.LEFT;
		shouldGrow[0] = false;
		shouldGrow[1] = false;

	}
	
	
	/** MOVE METHODS */
	public synchronized boolean moveRetreived(int querierId){
		return moveRetreived[querierId-1];
	}
	
	public synchronized void putNextMove(int playerId, Move move){
		nextMoves[playerId - 1] = move;
	}
	
	public synchronized Move[] getNextMoves(){
		return nextMoves.clone();
	}
	
	public synchronized void putCurrentMoves(Move[] moves) throws InterruptedException{
		while(movesChecked) wait();
		movesChecked = true;
		moveRetreived[0] = false;
		moveRetreived[1] = false;
		notifyAll();
		currentMoves = moves.clone();
	}
	
	public synchronized Move[] getCurrentMoves(int querierId) throws InterruptedException{
		while(!movesChecked || moveRetreived[querierId-1]) wait();
		moveRetreived[querierId-1] = true;
		if(moveRetreived[0] && moveRetreived[1]){
			movesChecked = false;
		}
		notifyAll();
		return currentMoves.clone();
	}
	
	/** STATE METHODS */
	public synchronized void setClientState(int playerId, GameState state){
		clientStates[playerId - 1] = state;
		if(clientStates[0] == GameState.READY && clientStates[1] == GameState.READY){
			clientStates[0] = GameState.PLAY;
			clientStates[1] = GameState.PLAY;
			serverReady = true;
		}
		notifyAll();
	}
	
	public synchronized GameState getClientState(int playerId) throws InterruptedException{
		while(!serverReady) {
			wait();
		}
		return clientStates[playerId - 1];
	}
	
	/** FOOD METHODS */
	public synchronized boolean foodChanged(int playerId){
		return foodChanged[playerId - 1];
	}
	
	public synchronized void putFood(Position f){
		food = f;
		foodChanged[0] = true;
		foodChanged[1] = true;
	}
	
	public synchronized Position getFood(int playerId){
		foodChanged[playerId - 1] = false;
		return food;
	}
	
	/** GROW METHODS */
	public synchronized boolean growChanged(int querierId){
		return growChanged[querierId - 1];
	}
	
	public synchronized void setShouldGrow(boolean[] grow){
		shouldGrow = grow.clone();
		growChanged[0] = true;
		growChanged[1] = true;
	}
	
	public synchronized boolean[] getShouldGrow(int querierId){
		growChanged[querierId - 1] = false;
		boolean[] ret = shouldGrow.clone();
		if(!growChanged[0] && !growChanged[1]){
			shouldGrow[0] = false;
			shouldGrow[1] = false;
		}
		return ret;
	}
}
