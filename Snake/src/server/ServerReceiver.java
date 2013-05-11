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
		while(socket.isConnected()){
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
		///// TEMP
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitor.setClientState(2, GameState.READY);
		/////
		break;
		}
	}
}
