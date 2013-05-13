package server;

import java.util.ArrayList;
import java.util.Random;

import client.*;
import client.ClientMonitor.GameState;
import client.Player.Move;

public class ServerLoop extends Thread {
	private ServerMonitor servMon;
	private Player p1;
	private Player p2;
	private int width;
	private Position food;
	private Random rand = new Random(System.currentTimeMillis());

	public ServerLoop(ServerMonitor servMon, int width){
		this.servMon = servMon;
		this.width = width;
		p1 = new Player(1, width);
		p2 = new Player(2, width);
		newFood();
		servMon.putFood(food);
	}

	public void run(){
		try {
			// While loop needed to postpone timer start until server is ready
			while(servMon.getClientState(1) != GameState.PLAY && servMon.getClientState(2) != GameState.PLAY);
			long loopStart = System.currentTimeMillis();
			
			while (servMon.getClientState(1) == GameState.PLAY && servMon.getClientState(2) == GameState.PLAY) {
				Move[] moves = servMon.getNextMoves();
				p1.move(moves[0]);
				p2.move(moves[1]);
				
				checkFood();
				if(food == null){
					newFood();
					servMon.putFood(food);
				}
				
				servMon.putCurrentMoves(moves);
				if(checkCollisions()){
					break;					
				}

				loopStart += 200; // Update every 200 ms (this will determine the speed of the game)
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
			servMon.setClientState(1, GameState.LOSE);
			servMon.setClientState(2, GameState.WIN);
			return true;
			// Player 2 collided
		}else if(!c1 && c2){
			servMon.setClientState(1, GameState.WIN);
			servMon.setClientState(2, GameState.LOSE);
			return true;
		}
		return false;
	}

	private void longestSnakeWins(int snakeLength1, int snakeLength2){
		if(snakeLength1 > snakeLength2){
			servMon.setClientState(1, GameState.WIN);
			servMon.setClientState(2, GameState.LOSE);	
		}else if(snakeLength1 < snakeLength2){
			servMon.setClientState(1, GameState.LOSE);
			servMon.setClientState(2, GameState.WIN);
		}else{
			servMon.setClientState(1, GameState.DRAW);
			servMon.setClientState(2, GameState.DRAW);
		}
	}

	private void checkFood(){
		Position head1 = p1.getSnake().getFirst();
		Position head2 = p2.getSnake().getFirst();
		if(head1.equals(food)){
			boolean[] shouldGrow = {true, false};
			servMon.setShouldGrow(shouldGrow);
			food = null;
		}else if(head2.equals(food)){
			boolean[] shouldGrow = {false, true};
			servMon.setShouldGrow(shouldGrow);
			food = null;
		}
	}

	private void newFood(){
		int x = rand.nextInt(width);
		int y = rand.nextInt(width);
		Position f = new Position(x, y);
		for(Position p : p1.getSnake()){
			if(f.equals(p)){
				newFood();
			}
		}
		for(Position p : p2.getSnake()){
			if(f.equals(p)){
				newFood();
			}
		}
		food = f;
	}
}
