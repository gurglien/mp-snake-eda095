package client;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import common.MessageHandlerExample;
import common.Protocol;

public class ClientExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Socket socket;
		PrintWriter out = null;
		MessageHandlerExample mh;
		
		try {
			
			socket = new Socket("localhost", 30000);
			mh = new MessageHandlerExample(socket);
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			mh.sendCode(Protocol.COM_SEND_POSITION);
			mh.sendPosition("Position");
			
			
		} catch (Exception e) {
			
		}
	}

}
