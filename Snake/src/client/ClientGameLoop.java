package client;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import client.ClientMonitor.GameState;
import client.Player.Move;

public class ClientGameLoop extends Thread{
	private ClientMonitor monitor;
	private GamePanel panel;
	private int width;

	public ClientGameLoop(ClientMonitor m, int playfieldWidth){
		monitor = m;
		width = playfieldWidth;

		// Create the GUI 
		// USED FOR TESTING PURPOSES, DO NOT REMOVE
//		panel = new GamePanel(monitor, playfieldWidth);
//		JFrame frame = new JFrame("Snake");
//		frame.add(panel);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(500, 500);
//		frame.setLocationRelativeTo(null);
//		frame.setResizable(false);
//		frame.setVisible(true);
	}
	
	public void setPanel(GamePanel panel){
		this.panel = panel;
	}

	public void run() {
		final Player p1 = new Player(1, width);
		final Player p2 = new Player(2, width);
		
		try {
			// While loop needed to postpone timer start until server is ready
			while(monitor.getState() != GameState.PLAY);
			long loopStart = System.currentTimeMillis();
			
			while(monitor.getState() == GameState.PLAY){
				try {
					Move[] moves = monitor.getCurrentMoves();
					if(moves == null){
						continue;
					}
					p1.move(moves[0]);
					p2.move(moves[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(monitor.getShouldGrow(1)) p1.grow();
				if(monitor.getShouldGrow(2)) p2.grow();
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						panel.updatePositions(p1.getSnake(), p2.getSnake(), monitor.getFood());
					}
				});
				
				loopStart += 100; // Update every 100 ms (this should be faster than ServerLoop)
				long diff = loopStart - System.currentTimeMillis();
				if(diff > 0) sleep(diff);
			}	
			closeAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes all sockets streams etc for this thread
	 */
	private void closeAll(){
		
	}
}
