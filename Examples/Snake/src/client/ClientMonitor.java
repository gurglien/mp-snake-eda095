package client;
import java.awt.event.KeyEvent;


public class ClientMonitor {
	private int[] moves = new int[2];
	
	public ClientMonitor(){
		moves[0] = KeyEvent.VK_RIGHT;
		moves[1] = KeyEvent.VK_RIGHT;
	}
	
	public synchronized void putNewMove(int player, int move){
		// player==1 is the local player
		moves[player - 1] = move;
	}
	
	public synchronized int getMove(int player){
		return moves[player - 1];
	}

}
