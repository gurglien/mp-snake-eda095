package client;

import java.util.ArrayList;

import client.ClientMonitor.GameState;
import client.Player.Move;

//USED FOR TESTING PURPOSES

public class PseudoServer extends Thread{
	private ClientMonitor monitor;
	private Player p1;
	private Player p2;
	private ArrayList<Position> food = new ArrayList<Position>();
	private int width;
	private boolean runs;

	public PseudoServer(ClientMonitor cm, int playfieldWidth){
		monitor = cm;
		width = playfieldWidth;
		p1 = new Player(1, width);
		p2 = new Player(2, width);
		runs = true;
	}

	public void stopThread(){
		runs = false;
	}
	
	public void run() {
		newFood();
		monitor.putFood(food);

		// Ska egentligen vara pausat innan vi sätter igång, men inte implementerat än
		monitor.setState(GameState.PLAY);

		long loopStart = System.currentTimeMillis();
		try {
			while(monitor.getState() == GameState.PLAY && runs){
				try {
					// Send to server
					Move m1 = monitor.getNextMove();
					Move m2 = Move.LEFT;
					
					// Compute on server
					p1.move(m1);
					p2.move(m2);
					Move[] moves = {m1, m2};
					
					// Return new info to client
					monitor.putCurrentMoves(moves); // Send moves first so the client know the last step in case of collision
					if(checkCollisions()){
						break;					
					}
					int nbrOfFood = food.size();
					checkFood();
					if(nbrOfFood != food.size()){
						newFood();
						monitor.putFood(food);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				loopStart += 100; // Update every 100 ms
				long diff = loopStart - System.currentTimeMillis();
				if(diff > 0) sleep(diff);
			}
		} catch (InterruptedException e) {
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
			monitor.setState(GameState.LOSE);
			return true;
			// Player 2 collided
		}else if(!c1 && c2){
			monitor.setState(GameState.WIN);
			return true;
		}
		return false;
	}

	private void longestSnakeWins(int snakeLength1, int snakeLength2){
		if(snakeLength1 > snakeLength2){
			monitor.setState(GameState.WIN);	// I den riktiga servern får vi skicka LOSE till andra spelaren också
		}else if(snakeLength1 < snakeLength2){
			monitor.setState(GameState.LOSE);
		}else{
			monitor.setState(GameState.DRAW);
		}
	}

	private void checkFood(){
		Position head1 = p1.getSnake().getFirst();
		Position head2 = p2.getSnake().getFirst();
		for(int i = 0; i < food.size(); ++i){
			if(head1.x == food.get(i).x && head1.y == food.get(i).y){
				monitor.setShouldGrow(1);
				food.remove(i);
			}
		}
		for(int i = 0; i < food.size(); ++i){
			if(head2.x == food.get(i).x && head2.y == food.get(i).y){
				monitor.setShouldGrow(2);
				food.remove(i);
			}
		}
	}

	private void newFood(){
		food.add(new Position(width/3, width/2));
	}
}
