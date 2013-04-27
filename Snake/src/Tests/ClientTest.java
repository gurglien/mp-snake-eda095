package Tests;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import client.Position;

import common.MessageHandler;
import common.Protocol;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Socket socket;
		PrintWriter out = null;
		MessageHandler mh;
		Position pos = new Position("0201");

		try {

			socket = new Socket("localhost", 30000);
			mh = new MessageHandler(socket);
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			mh.sendCode(Protocol.COM_SEND_POSITION);
			mh.sendPosition(pos);
			
			int result = mh.recieveCode();

			if (result == Protocol.ANS_ACK) {
				System.out.println("test lyckat!");
			}
		} catch (Exception e) {
			System.out.println("FEL");
		}
	}
}
