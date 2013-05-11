package client;

import java.net.Socket;


public class Client extends Thread{
	private Socket socket;
	private ClientMonitor monitor;
	private ClientGameLoop game;
	private ClientReceiver cr;
	
	public Client(ClientMonitor monitor, int width, Socket socket){
		this.socket = socket;
		this.monitor = monitor;
		game = new ClientGameLoop(monitor, width);
		cr = new ClientReceiver(monitor, socket);
	}
	
	public void run(){		
		game.start();
		
		ClientSender cs = new ClientSender(monitor, socket);
		cs.start();
		
		cr.start();
	}
	
	public void setPanel(GamePanel panel){
		game.setPanel(panel);
		cr.setPanel(panel);
	}
}
