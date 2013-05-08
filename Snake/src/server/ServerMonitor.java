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
	private boolean movesChecked;
	private boolean serverReady;
	private ArrayList<Position> food;
	
	public ServerMonitor(){
		nextMoves = new Move[2];
		currentMoves = new Move[2];
		shouldGrow = new boolean[2];
		gameState = GameState.WAIT;
		movesChecked = false;
		serverReady = false;
		food = new ArrayList<Position>();
		
		nextMoves[0] = Move.RIGHT;
		nextMoves[1] = Move.LEFT;
		shouldGrow[0] = false;
		shouldGrow[1] = false;

	}
	
	
	/** MOVE METHODS */
	public synchronized void putNextMove(int player, Move move){
		nextMoves[player - 1] = move;
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
	public synchronized void setState(GameState state){
		if(state == GameState.PLAY) {
			serverReady = true;
		}
		gameState = state;
		notifyAll();
	}
	
	public synchronized GameState getState() throws InterruptedException{
		while(!serverReady) {
			wait();
		}
		return gameState;
	}
	
	/** FOOD METHODS */
	public synchronized void putFood(ArrayList<Position> f){
		food = f;
	}
	
	public synchronized ArrayList<Position> getFood(){
		return food;
	}
	
	public synchronized void setShouldGrow(int player){
		shouldGrow[player - 1] = true;
	}
	
	public synchronized boolean getShouldGrow(int player){
		if(shouldGrow[player - 1]) {
			shouldGrow[player - 1] = false;
			return true;
		} else {
			return false;
		}
	}
}
