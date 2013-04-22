package client;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

	@Override
	public void run() {
		long loopStart = System.currentTimeMillis();
		while(true){
			// Limit to 30 fps
			if(System.currentTimeMillis() - loopStart > 1000*1/30){
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						panel.updateSnake(monitor.getSnake(1), monitor.getSnake(2));
					}
				});
				loopStart = System.currentTimeMillis();
			}
		}
	}
}
