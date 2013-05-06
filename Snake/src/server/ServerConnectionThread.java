package server;

import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.io.IOException;
import java.net.*;

public class ServerConnectionThread extends Thread{
	public static final int DEFAULT_PORT = 26700;
	private InetAddress adr;
	private int port;
	private int player;
	private MessageHandler mh;
	private ServerMonitor monitor;

	public ServerConnectionThread(int player, ServerMonitor monitor, InetAddress adr){
		this(player, monitor, adr, DEFAULT_PORT);
	}

	public ServerConnectionThread(int player, ServerMonitor monitor, InetAddress adr, int port) throws IllegalArgumentException{
		if(player < 1 || player > 2){
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
		this.player = player;
		this.monitor = monitor;
		this.adr = adr;
		this.port = port;
	}

	public void run(){
		try {
			ServerSocket server = new ServerSocket(port);
			Socket s = server.accept();
			mh = new MessageHandler(s);
			while(s.isConnected()){
				// Receive one player's next move 
				recvNextMove();

				// Get both players' moves and send to client
				sendCurrentMoves();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recvNextMove(){
		int m1 = mh.recieveCode();
		switch(m1){
		case Protocol.TURN_LEFT : monitor.putNextMove(player, Move.LEFT);
		break;
		case Protocol.TURN_RIGHT : monitor.putNextMove(player, Move.RIGHT);
		break;
		case Protocol.TURN_UP : monitor.putNextMove(player, Move.UP);
		break;
		case Protocol.TURN_DOWN : monitor.putNextMove(player, Move.DOWN);
		break;
		}
	}
	
	private void sendCurrentMoves(){
		try {
			Move[] moves = monitor.getCurrentMoves();
			
			switch(moves[0]){
			case LEFT : mh.sendCode(Protocol.TURN_LEFT);
			break;
			case RIGHT : mh.sendCode(Protocol.TURN_RIGHT);
			break;
			case UP : mh.sendCode(Protocol.TURN_UP);
			break;
			case DOWN : mh.sendCode(Protocol.TURN_DOWN);
			break;
			}
			switch(moves[1]){
			case LEFT : mh.sendCode(Protocol.TURN_LEFT);
			break;
			case RIGHT : mh.sendCode(Protocol.TURN_RIGHT);
			break;
			case UP : mh.sendCode(Protocol.TURN_UP);
			break;
			case DOWN : mh.sendCode(Protocol.TURN_DOWN);
			break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
