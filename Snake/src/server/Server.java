package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	private int width;
	private int port;
	private ServerMonitor monitor;
	
	public Server(ServerMonitor monitor, int width, int port){
		this.width = width;
		this.port = port;
		this.monitor = monitor;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket1 = new ServerSocket(port);
			
			ServerLoop game = new ServerLoop(monitor, width);
			game.start();	
			
			// Player 1
			Socket s = serverSocket1.accept();
			ServerSender ss1 = new ServerSender(1, monitor, s);
			ss1.start();
			
			ServerReceiver sr1 = new ServerReceiver(1, monitor, s);
			sr1.start();	
			
			// Player 2
			ServerSocket serverSocket2 = new ServerSocket(port+5); //TEMP - Behövs för att kunna köra två klienter lokalt
			s = null;
			s = serverSocket2.accept();
			ServerSender ss2 = new ServerSender(2, monitor, s);
			ss2.start();
			
			ServerReceiver sr2 = new ServerReceiver(2, monitor, s);
			sr2.start();
			
			try {
				sr1.join();
				serverSocket1.close();
				serverSocket2.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}