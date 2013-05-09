package server;

import java.util.ArrayList;

import client.*;
import client.ClientMonitor.GameState;
import client.Player.Move;

public class ServerLoop extends Thread {
	private ServerMonitor servMon;
	private Player p1;
	private Player p2;
	private int width;
	private Position food;

	public ServerLoop(ServerMonitor servMon, int width){
		this.servMon = servMon;
		this.width = width;
		newFood();
		servMon.putFood(food);
	}

	public void run(){
		p1 = new Player(1, width);
		p2 = new Player(2, width);
		
		try {
			servMon.getState(); // Needed to postpone timer start until server is ready
			long loopStart = System.currentTimeMillis();
			
			while (servMon.getState() == GameState.PLAY) {
				Move[] moves = servMon.getNextMoves();
				p1.move(moves[0]);
				p2.move(moves[1]);
				servMon.putCurrentMoves(moves);
				if(checkCollisions()){
					break;					
				}
				
				checkFood();
				if(food == null){
					newFood();
					servMon.putFood(food);
				}
				
				loopStart += 300; // Update every 300 ms (this will determine the speed of the game)
				long diff = loopStart - System.currentTimeMillis();
				if(diff > 0) sleep(diff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkCollisions(){
		boolean c1 = p1.checkCollision(p2.getSnake());
		boolean c2 = p2.checkCollision(p1.getSnake());
		// Simultaneous collision
		if(c1 && c2){
			longestSnakeWins(p1.getSnakeLength(), p2.getSnakeLength());
			return true;
			// Player 1 collided
		}else if(c1 && !c2){
			servMon.setState(GameState.LOSE);
			return true;
			// Player 2 collided
		}else if(!c1 && c2){
			servMon.setState(GameState.WIN);
			return true;
		}
		return false;
	}

	private void longestSnakeWins(int snakeLength1, int snakeLength2){
		if(snakeLength1 > snakeLength2){
			servMon.setState(GameState.WIN);	
		}else if(snakeLength1 < snakeLength2){
			servMon.setState(GameState.LOSE);
		}else{
			servMon.setState(GameState.DRAW);
		}
	}

	private void checkFood(){
		Position head1 = p1.getSnake().getFirst();
		Position head2 = p2.getSnake().getFirst();
		if(head1.x == food.x && head1.y == food.y){
			servMon.setShouldGrow(1);
			food = null;
		}else if(head2.x == food.x && head2.y == food.y){
			servMon.setShouldGrow(2);
			food = null;
		}
	}

	// TODO This should place new food at random and free positions
	private void newFood(){
		food = new Position(width/3, width/2);
	}





}
