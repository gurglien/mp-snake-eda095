package client;

import java.io.IOException;
import java.net.Socket;


public class Client extends Thread{
	private Socket socket;
	private ClientMonitor monitor;
	private ClientGameLoop game;
	private ClientReceiver cr;
	private Model model;
	
	public Client(ClientMonitor monitor, int width, Socket socket, Model model){
		this.socket = socket;
		this.monitor = monitor;
		game = new ClientGameLoop(monitor, width);
		cr = new ClientReceiver(monitor, socket);
		this.model = model;
	}
	
	public void run(){		
		game.start();
		
		ClientSender cs = new ClientSender(monitor, socket);
		cs.start();
		
		cr.start();
		
		try {
			cr.join();
			cs.join();
			model.closeGame();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPanel(GamePanel panel){
		game.setPanel(panel);
		cr.setPanel(panel);
	}
}
