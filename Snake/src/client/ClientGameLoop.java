package client;
import javax.swing.JFrame;

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
		panel = new GamePanel(monitor, playfieldWidth);
		JFrame frame = new JFrame("Snake");
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void run() {
		Player p1 = new Player(1, width);
		Player p2 = new Player(2, width);
		long loopStart = System.currentTimeMillis();
		try {
			while(monitor.getState() == GameState.PLAY){
				try {
					Move[] moves = monitor.getCurrentMoves();
					p1.move(moves[0]);
					p2.move(moves[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(monitor.getShouldGrow(1)) p1.grow();
				if(monitor.getShouldGrow(2)) p2.grow();
				panel.updatePositions(p1.getSnake(), p2.getSnake(), monitor.getFood());

				loopStart += 300; // Update every 300 ms
				long diff = loopStart - System.currentTimeMillis();
				if(diff > 0) sleep(diff);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// TEMP
		try {
			System.out.println(monitor.getState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
