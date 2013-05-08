package client;

import java.util.ArrayList;

import client.Player.Move;


public class ClientMonitor {
	public static enum GameState{PLAY, WIN, LOSE, DRAW, WAIT, CLOSE};
	private Move nextMove;
	private Move[] currentMoves = new Move[2];
	private boolean[] shouldGrow = {false, false};
	private GameState gameState = GameState.WAIT;
	private boolean moveChecked = false;
	private boolean moveChanged = false;
	private boolean serverReady = false;
	private ArrayList<Position> food = new ArrayList<Position>();
	
	public ClientMonitor(){
		nextMove = Move.RIGHT;
	}
	
	/** MOVE METHODS 
	 * @throws InterruptedException */
	// Used by the inputhandler
	public synchronized void putNextMove(Move move) throws InterruptedException{
		while(!serverReady) wait();
		nextMove = move;
		moveChanged = true;
		notifyAll();
	}
	
	// Used in the server game loop
	public synchronized Move getNextMove() throws InterruptedException{
		while(!moveChanged) wait();
		moveChanged = false;
		return nextMove;
	}
	
	// Used in the server game loop
	public synchronized void putCurrentMoves(Move[] moves) throws InterruptedException{
		while(moveChecked) wait();
		moveChecked = true;
		notifyAll();
		currentMoves = moves.clone();
	}
	
	// Used in the client game loop
	public synchronized Move[] getCurrentMoves() throws InterruptedException{
		while(!moveChecked)	wait();
		moveChecked = false;
		notifyAll();
		return currentMoves.clone();
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
