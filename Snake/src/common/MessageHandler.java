package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Logger;

import client.Position;

/**
 * Än så länge bara ett exempel på hur det här ska fungera. Bör väl se till att exceptions kastas ordentligt osv
 * @author oscar
 *
 */

public class MessageHandler {


	private Socket socket; // the connection
	private Logger logWindow; // the log window

	private InputStream istream;
	private OutputStream ostream;
	private PrintWriter out;
	private BufferedReader reader;


	public MessageHandler(Socket socket) {
		this.socket = socket;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			istream = socket.getInputStream();
			ostream = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = new BufferedReader(new InputStreamReader(istream));
	}

	/*
	 * Send code defined in class Protocol
	 */
	public void sendCode(int code){

		out.println(code);
	}

	/*
	 * recieve code defined in class Protocol
	 */
	public int recieveCode() {
		int code = -1;
		try {
			code = Integer.parseInt(reader.readLine());

		} catch (IOException e) {

		}
		return code;

	}

	/**
	 * 
	 * Sends Position pos as a string
	 */
	public void sendPosition(Position pos){
		out.println(pos);
	}

	/**
	 * Receives position as Position
	 * @return
	 * @throws ConnectException
	 */
	public Position recievePosition() throws ConnectException {
		Position pos;
		try {
			pos = new Position(reader.readLine());
			return pos;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ConnectException("Could not create position");
		}

	}

	/**
	 * turns snake according to Protocol.java
	 * @param turn
	 */
	public void sendTurn(int turn){
		out.println(turn);
	}

	public int recieveTurn() {
		int ret = -1;
		try {
			String turn = reader.readLine();				
			ret = Integer.parseInt(turn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}


}