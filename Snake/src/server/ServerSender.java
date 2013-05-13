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
	private GameState prevState = GameState.NOT_READY;

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
			try {
				GameState state = monitor.getClientState(player);
				if(state != prevState){
					sendGameState(state);
				}
				if(state == GameState.PLAY){
					sendFoodPos();
					sendCurrentOpponentMove();
					sendShouldGrow();
				}
				prevState = state;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendGameState(GameState state){
		switch(state){
		case PLAY : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.PLAY);
		if(player == 1){
			mh.sendCode(Protocol.ID_PLAYER);
		}else{
			mh.sendCode(Protocol.ID_OPPONENT);
		}
		break;
		case WIN : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.WIN);
		break;
		case LOSE : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.LOSE);
		break;
		case DRAW : mh.sendCode(Protocol.COM_STATE);
		mh.sendCode(Protocol.DRAW);
		break;
		}
	}

	// Get both players' moves and send to client
	private void sendCurrentOpponentMove(){
		try {
			Move move = monitor.getCurrentMove(opponent);
			mh.sendCode(Protocol.COM_MOVE);
			switch(move){
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
		if(monitor.growChanged(player)){
			boolean[] shouldGrow = monitor.getShouldGrow(player);
			if(shouldGrow[player - 1]){
				mh.sendCode(Protocol.COM_SHOULD_GROW);
				mh.sendCode(Protocol.ID_PLAYER);
			}
			if(shouldGrow[opponent - 1]){
				mh.sendCode(Protocol.COM_SHOULD_GROW);
				mh.sendCode(Protocol.ID_OPPONENT);
			}
		}
	}

	private void sendFoodPos(){
		if(monitor.foodChanged(player)){
			mh.sendCode(Protocol.COM_FOOD);
			mh.sendPosition(monitor.getFood(player));
		}
	}
}
