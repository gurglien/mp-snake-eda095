package client;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.net.*;

public class ClientSender extends Thread{
	public static final int DEFAULT_PORT = 30000;
	private Socket socket;
	private ClientMonitor monitor;
	private MessageHandler mh;

	public ClientSender(ClientMonitor monitor, Socket socket){
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){
		try{
			// Wait for ready (M�ste �ndras senare)
			GameState rdy = monitor.getState();
			if(rdy == GameState.PLAY){
				mh.sendCode(Protocol.READY);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		while(socket.isConnected()){
			// Send local player's next move to server
			sendNextMove();
		}
	}

	private void sendNextMove(){
		try {
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}