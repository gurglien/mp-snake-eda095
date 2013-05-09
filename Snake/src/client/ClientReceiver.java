package client;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.net.*;

public class ClientReceiver extends Thread{
	public static final int DEFAULT_PORT = 30000;
	private Socket socket;
	private ClientMonitor monitor;
	private MessageHandler mh;

	public ClientReceiver(ClientMonitor monitor, Socket socket){
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){		
		while(socket.isConnected()){
			int code = mh.recieveCode();
			switch(code){
			case Protocol.COM_MOVE : recvCurrentOpponentMove();
			break;
			case Protocol.COM_SHOULD_GROW : recvShouldGrow();
			break;
			case Protocol.COM_FOOD : recvFoodPos();
			break;
			case Protocol.COM_STATE : recvGameState();
			break;
			}
		}
	}

	// Receive both players current moves that should be updated locally
	private void recvCurrentOpponentMove(){
		Move move = null;
		int m = mh.recieveCode();
		switch(m){
		case Protocol.LEFT : move = Move.LEFT;
		break;
		case Protocol.RIGHT : move = Move.RIGHT;
		break;
		case Protocol.UP : move = Move.UP;
		break;
		case Protocol.DOWN : move = Move.DOWN;
		break;
		}
		try {
			monitor.putCurrentOpponentMove(move);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void recvShouldGrow(){
		int playerId = mh.recieveCode();
		switch(playerId){
		case Protocol.ID_PLAYER : monitor.setShouldGrow(1);
		break;
		case Protocol.ID_OPPONENT : monitor.setShouldGrow(2);
		}
	}
	
	private void recvFoodPos(){
		try {
			Position food = mh.recievePosition();
			monitor.putFood(food);
		} catch (ConnectException e) {
			e.printStackTrace();
		}
	}
	
	private void recvGameState(){
		int s = mh.recieveCode();
		switch(s){
		case Protocol.PLAY : monitor.setState(GameState.PLAY);
		break;
		}
	}

}
