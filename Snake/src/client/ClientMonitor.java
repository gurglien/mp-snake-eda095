import java.util.ArrayDeque;


public class ClientMonitor {
	private Player[] players = new Player[2];
	
	public ClientMonitor(){
		players[0] = new Player(1);
		players[1] = new Player(2);
	}
	
	public synchronized void putNewMove(int move){
		players[0].move(move);
//		players[1].move(move);
	}
	
	public synchronized ArrayDeque<Position> getSnake(int player){
		return players[player - 1].getSnake();
	}
	

}
