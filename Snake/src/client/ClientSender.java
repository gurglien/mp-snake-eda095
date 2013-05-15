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
	private GameState prevState = GameState.NOT_READY;

	public ClientSender(ClientMonitor monitor, Socket socket){
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){
		while(!isInterrupted()){
			GameState state = monitor.getState();
			if(state != prevState){
				prevState = state;
				sendGameState(state);
			}
			if(state == GameState.PLAY){
				sendNextMove();
			}else if(state == GameState.CLOSE){
				interrupt();
			}
		}
	}

	private void sendGameState(GameState state){
		switch(state){
		case READY : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.READY);
		break;
		case CLOSE : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.CLOSE);
		break;
		}
	}

	// Send local player's next move to server
	private void sendNextMove(){
		if(!monitor.moveChanged()) return;
		try {
			Move nextMove = monitor.getNextMove();
			mh.sendCode(Protocol.COM_MOVE);
			switch(nextMove){
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
}
