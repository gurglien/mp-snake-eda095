package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.Model;

public class Server extends Thread{
	private int width;
	private int port;
	private ServerMonitor monitor;
	private Model model;
	
	public Server(ServerMonitor monitor, int width, int port, Model model){
		this.width = width;
		this.port = port;
		this.monitor = monitor;
		this.model = model;
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
			ServerSocket serverSocket2 = new ServerSocket(port+5); //TEMP - Beh�vs f�r att kunna k�ra tv� klienter lokalt
			s = null;
			s = serverSocket2.accept();
			ServerSender ss2 = new ServerSender(2, monitor, s);
			ss2.start();
			
			ServerReceiver sr2 = new ServerReceiver(2, monitor, s);
			sr2.start();
			
			serverSocket1.close();
			serverSocket2.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}