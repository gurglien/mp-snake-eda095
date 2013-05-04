package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.Position;

import common.MessageHandler;

public class Server {

	public static void main(String[] args) {


		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(30000);
			String message = "";
			Position pos;
			int code;
			ServerMonitor servMon = new ServerMonitor();
			
			
			
//			Socket socket = serverSocket.accept();
//			MessageHandler mh = new MessageHandler(socket);
			
			//vänta på inkommande connections - starta trådar
			//skapa trådar för: 
				//inkommande connections
				//en tråd för att skicka state till client?
				//en tråd som slumpar fram käk på spelplanen? (servern ska ej accessa monitorn)
			
			
			
			
	
			
			
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}