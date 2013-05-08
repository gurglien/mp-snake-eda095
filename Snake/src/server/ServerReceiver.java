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
		try{
			// Wait for ready (Måste ändras senare)
			int rdy = mh.recieveCode();
			while(rdy != Protocol.READY){
				// Tillfällig felkontroll
				System.out.println("Server: Received something other than READY.");
				rdy = mh.recieveCode();
			}
			if(rdy == Protocol.READY){
				monitor.setState(GameState.PLAY);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		while(socket.isConnected()){
			// Receive one player's next move 
			recvNextMove();
		}
	}

	private void recvNextMove(){
		int m1 = mh.recieveCode();
		switch(m1){
		case Protocol.TURN_LEFT : monitor.putNextMove(player, Move.LEFT);
		break;
		case Protocol.TURN_RIGHT : monitor.putNextMove(player, Move.RIGHT);
		break;
		case Protocol.TURN_UP : monitor.putNextMove(player, Move.UP);
		break;
		case Protocol.TURN_DOWN : monitor.putNextMove(player, Move.DOWN);
		break;
		}
	}
}
