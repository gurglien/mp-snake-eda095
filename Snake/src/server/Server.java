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
			
			Socket s = serverSocket.accept();
			ServerSender ss = new ServerSender(1, monitor, s);
			ss.start();
			
			ServerReceiver sr = new ServerReceiver(1, monitor, s);
			sr.start();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}