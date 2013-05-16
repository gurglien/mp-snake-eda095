package server;

import client.ClientMonitor.GameState;
import client.Player.Move;
import common.MessageHandler;
import common.Protocol;

import java.io.IOException;
import java.net.*;

public class ServerReceiver extends Thread{
	private int player;
	private MessageHandler mh;
	private ServerMonitor monitor;
	private boolean closeSocket = false;
	private Socket socket;

	public ServerReceiver(int player, ServerMonitor monitor, Socket socket) throws IllegalArgumentException{
		if(player < 1 || player > 2){
			throw new IllegalArgumentException("Only player 1 or 2 allowed.");
		}
		this.player = player;
		this.monitor = monitor;
		this.socket = socket;
		mh = new MessageHandler(socket);
	}

	public void run(){
		while(!isInterrupted()){
			int com = mh.recieveCode();
			switch(com){
			case Protocol.COM_MOVE : recvNextMove();
			break;
			case Protocol.COM_STATE : recvClientState();
			break;
			}
		}
	}

	// Receive one player's next move 
	private void recvNextMove(){
		int m = mh.recieveCode();
		switch(m){
		case Protocol.LEFT : monitor.putNextMove(player, Move.LEFT);
		break;
		case Protocol.RIGHT : monitor.putNextMove(player, Move.RIGHT);
		break;
		case Protocol.UP : monitor.putNextMove(player, Move.UP);
		break;
		case Protocol.DOWN : monitor.putNextMove(player, Move.DOWN);
		break;
		}
	}

	private void recvClientState(){
		int s = mh.recieveCode();
		switch(s){
		case Protocol.READY : monitor.setClientState(player, GameState.READY);
		break;
		case Protocol.WIN : closeSocket = true;
		interrupt();
		break;
		case Protocol.LOSE : closeSocket = true;
		interrupt();
		break;
		case Protocol.DRAW : closeSocket = true;
		interrupt();
		break;
		case Protocol.CLOSE : monitor.setClientState(player, GameState.CLOSE);
		interrupt();
		break;
		}
		if(closeSocket){
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
