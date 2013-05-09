package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//USED FOR TESTING PURPOSES

public class Client extends Thread{
	private int width;
	private Socket socket;
	private ClientMonitor monitor;
	private ClientGameLoop game;
	
	public Client(ClientMonitor monitor, int width, Socket socket){
		this.width = width;
		this.socket = socket;
		this.monitor = monitor;
		game = new ClientGameLoop(monitor, width);
	}
	
	public void run(){		
		game.start();
		
		ClientSender cs = new ClientSender(monitor, socket);
		cs.start();
		
		ClientReceiver cr = new ClientReceiver(monitor, socket);
		cr.start();
	}
	
	public void setPanel(GamePanel panel){
		game.setPanel(panel);
	}
}
