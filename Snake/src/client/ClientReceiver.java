package client;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.net.*;

import javax.swing.SwingUtilities;

public class ClientReceiver extends Thread{
	public static final int DEFAULT_PORT = 30000;
	private Socket socket;
	private ClientMonitor monitor;
	private MessageHandler mh;
	private int player;
	private int opponent;
	private GamePanel panel;

	public ClientReceiver(ClientMonitor monitor, Socket socket) throws IllegalArgumentException{
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){		
		while(!isInterrupted()){
			int code = mh.recieveCode();
			switch(code){
			case Protocol.COM_MOVE : recvCurrentMoves();
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
	private void recvCurrentMoves(){
		Move[] moves = new Move[2];
		int m1 = mh.recieveCode();
		switch(m1){
		case Protocol.LEFT : moves[0] = Move.LEFT;
		break;
		case Protocol.RIGHT : moves[0] = Move.RIGHT;
		break;
		case Protocol.UP : moves[0] = Move.UP;
		break;
		case Protocol.DOWN : moves[0] = Move.DOWN;
		break;
		}

		int m2 = mh.recieveCode();
		switch(m2){
		case Protocol.LEFT : moves[1] = Move.LEFT;
		break;
		case Protocol.RIGHT : moves[1] = Move.RIGHT;
		break;
		case Protocol.UP : moves[1] = Move.UP;
		break;
		case Protocol.DOWN : moves[1] = Move.DOWN;
		break;
		}
		try {
			monitor.putCurrentMoves(moves);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void recvShouldGrow(){
		int playerId = mh.recieveCode();
		switch(playerId){
		case Protocol.ID_PLAYER : monitor.setShouldGrow(player);
		break;
		case Protocol.ID_OPPONENT : monitor.setShouldGrow(opponent);
		break;
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
		GameState state = null;
		switch(s){
		case Protocol.PLAY : state = GameState.PLAY;
		int id = mh.recieveCode();
		if(id == Protocol.ID_PLAYER){
			player = 1;
			opponent = 2;
		}else{
			player = 2;
			opponent = 1;
		}
		monitor.initialize(player);
		break;
		case Protocol.WIN : state = GameState.WIN;
		interrupt();
		break;
		case Protocol.LOSE : state = GameState.LOSE;
		interrupt();
		break;
		case Protocol.DRAW : state = GameState.DRAW;
		interrupt();
		break;
		case Protocol.CLOSE : state = GameState.CLOSE;
		interrupt();
		break;
		}
		final GameState finalState = state;
		monitor.setState(finalState);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.updateGameState(finalState);
			}
		});
	}

	public void setPanel(GamePanel panel){
		this.panel = panel;
	}

}
