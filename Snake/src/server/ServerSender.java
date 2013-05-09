package server;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.io.IOException;
import java.net.*;

public class ServerSender extends Thread{
	private int player;
	private int opponent;
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
		opponent = (player == 1) ? 2 : 1;
	}

	public void run(){		
		while(socket.isConnected()){
			sendFoodPos();
			sendCurrenOpponentMove();
			sendShouldGrow();
		}
	}
	
	// Get both players' moves and send to client
	private void sendCurrenOpponentMove(){
		try {
			Move[] moves = monitor.getCurrentMoves();
			mh.sendCode(Protocol.COM_MOVE);
			switch(moves[1]){ // Remember to change, must depend on player variable
			case LEFT : mh.sendCode(Protocol.LEFT);
			break;
			case RIGHT : mh.sendCode(Protocol.RIGHT);
			break;
			case UP : mh.sendCode(Protocol.UP);
			break;
			case DOWN : mh.sendCode(Protocol.DOWN);
			break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sendShouldGrow(){
		if(monitor.getShouldGrow(player)){
			mh.sendCode(Protocol.COM_SHOULD_GROW);
			mh.sendCode(Protocol.ID_PLAYER);
		}
		if(monitor.getShouldGrow(opponent)){
			mh.sendCode(Protocol.COM_SHOULD_GROW);
			mh.sendCode(Protocol.ID_OPPONENT);
		}
	}
	
	private void sendFoodPos(){
		if(monitor.foodChanged()){
			mh.sendCode(Protocol.COM_FOOD);
			mh.sendPosition(monitor.getFood());
		}
	}
}
