package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//USED FOR TESTING PURPOSES

public class Client extends Thread{
	private int width;
	private InetAddress host;
	private int port;
	
	public Client(int width, String host, int port){
		this.width = width;
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}
	
	public void run(){
		try {
			ClientMonitor monitor = new ClientMonitor();
			Socket s = new Socket(host, port);
			
			ClientGameLoop game = new ClientGameLoop(monitor, width);
			game.start();
			
			ClientSender cs = new ClientSender(monitor, s);
			cs.start();
			
			ClientReceiver cr = new ClientReceiver(monitor, s);
			cr.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
