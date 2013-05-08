package server;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.io.IOException;
import java.net.*;

public class ServerSender extends Thread{
	private int player;
	private MessageHandler mh;
	private ServerMonitor monitor;
	private Socket socket;

	public ServerSender(int player, ServerMonitor monitor, Socket socket) throws IllegalArgumentException{
		if(player < 1 || player > 2){
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
		this.player = player;
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){		
		while(socket.isConnected()){
			// Get both players' moves and send to client
			sendCurrentMoves();
		}
	}
	
	private void sendCurrentMoves(){
		try {
			Move[] moves = monitor.getCurrentMoves();
			switch(moves[0]){ // Remember to change, must depend on player variable
			case LEFT : mh.sendCode(Protocol.TURN_LEFT);
			break;
			case RIGHT : mh.sendCode(Protocol.TURN_RIGHT);
			break;
			case UP : mh.sendCode(Protocol.TURN_UP);
			break;
			case DOWN : mh.sendCode(Protocol.TURN_DOWN);
			break;
			}
			
			switch(moves[1]){ // Remember to change, must depend on player variable
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
