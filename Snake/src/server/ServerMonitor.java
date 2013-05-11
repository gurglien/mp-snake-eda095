package server;

import java.util.ArrayList;

import client.Position;
import client.ClientMonitor.GameState;
import client.Player.Move;

public class ServerMonitor {
	
	private Move[] nextMoves;
	private Move[] currentMoves;
	private boolean[] shouldGrow;
	private GameState gameState;
	private GameState[] clientStates = {GameState.NOT_READY, GameState.NOT_READY};
	private boolean movesChecked;
	private boolean serverReady;
	private boolean foodChanged = false;
	private Position food;
	
	public ServerMonitor(){
		nextMoves = new Move[2];
		currentMoves = new Move[2];
		shouldGrow = new boolean[2];
		gameState = GameState.NOT_READY;
		movesChecked = false;
		serverReady = false;
		
		nextMoves[0] = Move.RIGHT;
		nextMoves[1] = Move.LEFT;
		shouldGrow[0] = false;
		shouldGrow[1] = false;

	}
	
	
	/** MOVE METHODS */
	public synchronized void putNextMove(int playerId, Move move){
		nextMoves[playerId - 1] = move;
	}
	
	public synchronized Move[] getNextMoves(){
		return nextMoves.clone();
	}
	
	public synchronized void putCurrentMoves(Move[] moves) throws InterruptedException{
		while(movesChecked)	wait();
		movesChecked = true;
		notifyAll();
		currentMoves = moves.clone();
	}
	
	public synchronized Move[] getCurrentMoves() throws InterruptedException{
		while(!movesChecked) wait();
		movesChecked = false;
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
	
//	public synchronized void setState(GameState state){
//		if(state == GameState.PLAY) {
//			serverReady = true;
//		}
//		gameState = state;
//		notifyAll();
//	}
	
//	public synchronized GameState getState() throws InterruptedException{
//		while(!serverReady) {
//			wait();
//		}
//		return gameState;
//	}
	
	/** FOOD METHODS */
	public synchronized boolean foodChanged(){
		return foodChanged;
	}
	
	public synchronized void putFood(Position f){
		food = f;
		foodChanged = true;
	}
	
	public synchronized Position getFood(){
		foodChanged = false;
		return food;
	}
	
	public synchronized void setShouldGrow(int playerId){
		shouldGrow[playerId - 1] = true;
	}
	
	public synchronized boolean getShouldGrow(int playerId){
		if(shouldGrow[playerId - 1]) {
			shouldGrow[playerId - 1] = false;
			return true;
		} else {
			return false;
		}
	}
}
