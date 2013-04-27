package Tests;

import java.io.IOException;
import java.net.*;

import client.Position;

import common.MessageHandler;
import common.Protocol;

public class MessageHandlerTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(30000);
			Socket socket = serverSocket.accept();
			MessageHandler mh = new MessageHandler(socket);
			String message = "";
			Position pos;



			int code = mh.recieveCode();

			switch (code) {

			case Protocol.COM_SEND_POSITION:	pos = mh.recievePosition();
			System.out
			.println(pos);
			mh.sendCode(Protocol.ANS_ACK);
			break;
			default: 							System.out.println("default");
			break;	
			}






		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
