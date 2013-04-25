package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import common.MessageHandlerExample;
import common.Protocol;

public class ServerExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerSocket serverSocket;
		int code;
		String message;
		try {
			serverSocket = new ServerSocket(30000);
	
		Socket clientSocket = null;
		System.out.println("example server started");
		while (true) {
			clientSocket = serverSocket.accept();
			MessageHandlerExample mh = new MessageHandlerExample(clientSocket);
			
			code = mh.recieveCode();
			System.out.println(code);
			switch (code) {
			case Protocol.COM_SEND_POSITION: 
				message = mh.recievePosition();
				mh.sendPosition(Protocol.codeString(Protocol.ANS_SEND_POSITION));
				System.out.println(message);
			case Protocol.COM_TURN_LEFT: 
				System.out.println("etc");
			default: 
				System.out.println("default");
			}		
			
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
