package client;

import client.Player.Move;


public class ClientMonitor {
	public static enum GameState{PLAY, WIN, LOSE, DRAW, READY, NOT_READY, OPPONENT_DISC, CLOSE};
	private Move nextMove;
	private Move[] currentMoves = new Move[2];
	private boolean[] shouldGrow = {false, false};
	private GameState gameState = GameState.NOT_READY;
	private boolean moveChecked = false;
	private boolean moveChanged = false;
	private Position food;
	private int player;
	private int opponent;
	
	public ClientMonitor(){
		
	}
	
	public synchronized void initialize(int playerId) throws IllegalArgumentException{
		if(playerId == 1){
			nextMove = Move.RIGHT; // putNextMove?
			player = 0;
			opponent = 1;
		}else if(playerId == 2){
			nextMove = Move.LEFT;
			player = 1;
			opponent = 0;
		}else{
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
//		currentMoves[player] = nextMove;
	}
	
	/** MOVE METHODS 
	 * @throws InterruptedException */
	public synchronized boolean moveChanged(){
		return moveChanged;
	}
	
	// Used by InputHandler
	public synchronized void putNextMove(Move move) throws InterruptedException{
		nextMove = move;
		moveChanged = true;
	}
	
	// Used by ClientSender
	public synchronized Move getNextMove() throws InterruptedException{
		moveChanged = false;
		return nextMove;
	}
	
	// Used by ClientReceiver
	public synchronized void putCurrentMoves(Move[] moves) throws InterruptedException{
		while(moveChecked) wait();
		moveChecked = true;
		notifyAll();
		currentMoves = moves.clone();
	}
	
	// Used by ClientGameLoop
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
