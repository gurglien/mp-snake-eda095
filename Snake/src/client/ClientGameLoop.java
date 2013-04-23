package client;
import javax.swing.JFrame;

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
		while(true){
			// Limit update speed (fixa ngn sleep-anordning senare)
			if(System.currentTimeMillis() - loopStart > 300){
				p1.move(monitor.getMove(1));
				p2.move(monitor.getMove(2));
				panel.updateSnake(p1.getSnake(), p2.getSnake());
				loopStart = System.currentTimeMillis();
			}
		}
	}
}
