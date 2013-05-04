package server;

import client.*;

public class ServerLoop extends Thread {
	ServerMonitor servMon;
	Player p1;
	Player p2;
	int width;

	public void run(ServerMonitor servMon, int width){
		this.servMon = servMon;
		this.width = width;
		p1 = new Player(1, width);
		p2 = new Player(2, width);
		
	}
}
