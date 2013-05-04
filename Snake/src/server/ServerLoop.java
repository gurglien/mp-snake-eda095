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
	private ArrayList<Position> food;
	
	public ServerLoop(ServerMonitor servMon, int width){
		food = new ArrayList<Position>();
		this.servMon = servMon;
		this.width = width;
	}

	public void run(){

		p1 = new Player(1, width);
		p2 = new Player(2, width);
		
		while (true) {
			Move m1 = servMon.getNextMove(1);
			Move m2 = servMon.getNextMove(2);
			p1.move(m1);
			p2.move(m2);
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
		for(int i = 0; i < food.size(); ++i){
			if(head1.x == food.get(i).x && head1.y == food.get(i).y){
				servMon.setShouldGrow(1);
				food.remove(i);
			}
		}
		for(int i = 0; i < food.size(); ++i){
			if(head2.x == food.get(i).x && head2.y == food.get(i).y){
				servMon.setShouldGrow(2);
				food.remove(i);
			}
		}
	}

	private void newFood(){
		food.add(new Position(width/3, width/2));
	}
	
	
	
	
	
}
