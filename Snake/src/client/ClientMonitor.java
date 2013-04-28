package client;

import java.util.ArrayList;

import client.Player.Move;


public class ClientMonitor {
	public static enum GameState{PLAY, WIN, LOSE, DRAW};
	private Move[] nextMove = new Move[2];
	private Move[] currentMove = new Move[2];
	private boolean[] shouldGrow = new boolean[2];
	private GameState gameState = GameState.PLAY;
	private boolean moveChecked = false;
	private boolean serverReady = false;
	private ArrayList<Position> food = new ArrayList<Position>();
	
	public ClientMonitor(){
		nextMove[0] = Move.RIGHT;
		nextMove[1] = Move.LEFT;
		shouldGrow[0] = false;
		shouldGrow[1] = false;
	}
	
	/** MOVE METHODS */
	// Used by the inputhandler
	public synchronized void putNextMove(int player, Move move){
		nextMove[player - 1] = move;
	}
	
	// Used in the server game loop
	public synchronized Move getNextMove(int player){
		return nextMove[player - 1];
	}
	
	// Used in the server game loop
	public synchronized void putCurrentMove(int player, Move move) throws InterruptedException{
		while(moveChecked)	wait();
		moveChecked = true;
		notifyAll();
		currentMove[player - 1] = move;
	}
	
	// Used in the client game loop
	public synchronized Move getCurrentMove(int player) throws InterruptedException{
		while(!moveChecked)	wait();
		moveChecked = false;
		notifyAll();
		return currentMove[player - 1];
	}
	
	/** STATE METHODS */
	public synchronized void setState(GameState state){
		if(state == GameState.PLAY) serverReady = true;
		gameState = state;
		notifyAll();
	}
	
	public synchronized GameState getState() throws InterruptedException{
		while(!serverReady) wait();
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
		if(shouldGrow[player - 1]){
			shouldGrow[player - 1] = false;
			return true;
		}else{
			return false;
		}
	}

}
