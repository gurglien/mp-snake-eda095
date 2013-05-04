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
			int width = 99;
			
			
			ServerLoop game = new ServerLoop(servMon, width);
			Socket socket1 = serverSocket.accept();
			Socket socket2 = serverSocket.accept();
			//vänta på inkommande connections - starta trådar
			
			//skapa två messagehandlers eller ska messagehandler innehålla lista på sockets?
//			MessageHandler mh = new MessageHandler(socket);
			
			game.start();
			
			
			//skapa trådar för: 
				//en tråd för att skicka state till client?
			//hur slumpa fram käk?
			
	
			
			
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}