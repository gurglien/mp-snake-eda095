package client;
import javax.swing.JFrame;

import client.ClientMonitor.GameState;

public class ClientGameLoop extends Thread{
	private ClientMonitor monitor;
	private GamePanel panel;

	public ClientGameLoop(ClientMonitor m){
		monitor = m;
		
		// Create the GUI
		panel = new GamePanel(monitor);
		JFrame frame = new JFrame("Snake");
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void run() {
		Player p1 = new Player(1);
		Player p2 = new Player(2);
		long loopStart = System.currentTimeMillis();
		while(monitor.getState() == GameState.PLAY){
			// Limit update speed (fixa ngn sleep-anordning senare)
			if(System.currentTimeMillis() - loopStart > 300){
				try {
					p1.move(monitor.getCurrentMove(1));
					p2.move(monitor.getCurrentMove(2));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				if(monitor.getShouldGrow(1)) p1.grow();
				if(monitor.getShouldGrow(2)) p2.grow();
				
				panel.updatePositions(p1.getSnake(), p2.getSnake(), monitor.getFood());
				loopStart = System.currentTimeMillis();
			}
		}
		System.out.println(monitor.getState());
	}
	
	// Will be moved to server side later
//	public 
}
