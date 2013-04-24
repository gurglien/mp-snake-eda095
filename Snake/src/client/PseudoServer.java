package client;

import java.util.ArrayList;

import client.ClientMonitor.GameState;
import client.Player.Move;

public class PseudoServer extends Thread{
	private ClientMonitor monitor;
	private Player p1 = new Player(1);
	private Player p2 = new Player(2);
	private ArrayList<Position> food = new ArrayList<Position>();

	public PseudoServer(ClientMonitor cm){
		monitor = cm;
	}

	public void run() {
		newFood();
		monitor.putFood(food);
		
		// Ska egentligen vara pausat innan vi sätter igång, men inte implementerat än
		monitor.setState(GameState.PLAY);

		long loopStart = System.currentTimeMillis();
		while(monitor.getState() == GameState.PLAY){
			//Limit update speed (fixa ngn sleep-anordning senare)
			if(System.currentTimeMillis() - loopStart > 100){
				try {
					Move m1 = monitor.getNextMove(1);
					Move m2 = monitor.getNextMove(1);
					p1.move(m1);
					p2.move(m2);
					
					
					monitor.putCurrentMove(1, m1);
					monitor.putCurrentMove(1, m2);
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
				loopStart = System.currentTimeMillis();
			}
		}
	}
	
	// DÅLIG/OFÄRDIG ÄN SÅ LÄNGE
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
		food.add(new Position(110,130));
	}
}
