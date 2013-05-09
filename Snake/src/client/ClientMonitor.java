package client;

import java.util.ArrayList;

import client.Player.Move;


public class ClientMonitor {
	public static enum GameState{PLAY, WIN, LOSE, DRAW, READY, NOT_READY, CLOSE};
	private Move nextMove;
	private Move[] currentMoves = new Move[2];
	private boolean[] shouldGrow = {false, false};
	private GameState gameState = GameState.NOT_READY;
	private boolean moveChecked = false;
	private boolean moveChanged = false;
	private boolean serverReady = false;
	private Position food;
	
	public ClientMonitor(){
		nextMove = Move.RIGHT;
		currentMoves[0] = nextMove;
	}
	
	/** MOVE METHODS 
	 * @throws InterruptedException */
	// Used by the inputhandler
	public synchronized void putNextMove(Move move) throws InterruptedException{
		nextMove = move;
		moveChanged = true;
		notifyAll();
	}
	
	// Used in the server game loop
	public synchronized Move getNextMove() throws InterruptedException{
		while(!moveChanged) wait();
		moveChanged = false;
		currentMoves[0] = nextMove;
		return nextMove;
	}
	
	// Used in the server game loop
	public synchronized void putCurrentOpponentMove(Move move) throws InterruptedException{
		while(moveChecked) wait();
		moveChecked = true;
		notifyAll();
		currentMoves[1] = move;
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
		gameState = state;
	}
	
	public synchronized GameState getState(){
		return gameState;
	}
	
	/** FOOD METHODS */
	public synchronized void putFood(Position f){
		food = f;
	}
	
	public synchronized Position getFood(){
		return food;
	}
	
	public synchronized void setShouldGrow(int playerId){
		shouldGrow[playerId - 1] = true;
	}
	
	public synchronized boolean getShouldGrow(int playerId){
		if(shouldGrow[playerId - 1]){
			shouldGrow[playerId - 1] = false;
			return true;
		}else{
			return false;
		}
	}

}
