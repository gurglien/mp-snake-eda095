package client;

import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.io.IOException;
import java.net.*;

public class ClientConnectionThread extends Thread{
	public static final int DEFAULT_PORT = 26700;
	private InetAddress adr;
	private int port;
	private ClientMonitor monitor;
	private MessageHandler mh;

	public ClientConnectionThread(ClientMonitor monitor, InetAddress adr){
		this(monitor, adr, DEFAULT_PORT);
	}

	public ClientConnectionThread(ClientMonitor monitor, InetAddress adr, int port){
		this.monitor = monitor;
		this.adr = adr;
		this.port = port;
	}

	public void run(){
		try {
			Socket s = new Socket(adr, port);
			mh = new MessageHandler(s);
			while(s.isConnected()){
				// Send local player's next move to server
				sendNextMove();

				// Receive both players current moves that should be updated locally
				recvCurrentMoves();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendNextMove(){
		Move nextMove = monitor.getNextMove();
		switch(nextMove){
		case LEFT : mh.sendCode(Protocol.TURN_LEFT);
		break;
		case RIGHT : mh.sendCode(Protocol.TURN_RIGHT);
		break;
		case UP : mh.sendCode(Protocol.TURN_UP);
		break;
		case DOWN : mh.sendCode(Protocol.TURN_DOWN);
		break;
		}
	}
	
	private void recvCurrentMoves(){
		Move[] moves = new Move[2];
		int m1 = mh.recieveCode();
		switch(m1){
		case Protocol.TURN_LEFT : moves[0] = Move.LEFT;
		break;
		case Protocol.TURN_RIGHT : moves[0] = Move.RIGHT;
		break;
		case Protocol.TURN_UP : moves[0] = Move.UP;
		break;
		case Protocol.TURN_DOWN : moves[0] = Move.DOWN;
		break;
		}
		int m2 = mh.recieveCode();
		switch(m2){
		case Protocol.TURN_LEFT : moves[1] = Move.LEFT;
		break;
		case Protocol.TURN_RIGHT : moves[1] = Move.RIGHT;
		break;
		case Protocol.TURN_UP : moves[1] = Move.UP;
		break;
		case Protocol.TURN_DOWN : moves[1] = Move.DOWN;
		break;
		}
		try {
			monitor.putCurrentMoves(moves);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	private void recvShouldGrow(){
//		boolean[] grow = new boolean[2];
//		int g1 = mh.recieveCode();
//		switch(g1){
//		case Protocol.SHOULD_GROW_TRUE: grow[0] = true;
//		break;
//		case Protocol.SHOULD_GROW_FALSE : grow[0] = false;
//		break;
//		}
//		int g2 = mh.recieveCode();
//		switch(g2){
//		case Protocol.SHOULD_GROW_TRUE: grow[1] = true;
//		break;
//		case Protocol.SHOULD_GROW_FALSE : grow[1] = false;
//		break;
//		}
//		try {
//			monitor.
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}
