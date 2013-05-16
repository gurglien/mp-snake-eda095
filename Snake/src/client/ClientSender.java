package client;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.net.*;

public class ClientSender extends Thread{
	private ClientMonitor monitor;
	private MessageHandler mh;
	private GameState prevState = GameState.NOT_READY;

	public ClientSender(ClientMonitor monitor, Socket socket){
		this.monitor = monitor;
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
			}
		}
	}

	private void sendGameState(GameState state){
		switch(state){
		case READY : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.READY);
		break;
		case WIN : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.WIN);
		interrupt();
		break;
		case LOSE : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.LOSE);
		interrupt();
		break;
		case DRAW : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.DRAW);
		interrupt();
		break;
		case CLOSE : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.CLOSE);
		interrupt();
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
