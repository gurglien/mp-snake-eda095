package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.Position;

import common.MessageHandler;

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
			ServerSocket serverSocket = new ServerSocket(port);
			
			ServerLoop game = new ServerLoop(monitor, width);
			game.start();	
			
			// Player 1
			Socket s = serverSocket.accept();
			ServerSender ss1 = new ServerSender(1, monitor, s);
			ss1.start();
			
			ServerReceiver sr1 = new ServerReceiver(1, monitor, s);
			sr1.start();	
			
			// Player 2
			serverSocket = new ServerSocket(port+5); //TEMP - Behövs för att kunna köra två klienter lokalt
			s = null;
			s = serverSocket.accept();
			ServerSender ss2 = new ServerSender(2, monitor, s);
			ss2.start();
			
			ServerReceiver sr2 = new ServerReceiver(2, monitor, s);
			sr2.start();		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}