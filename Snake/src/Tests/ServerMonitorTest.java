package Tests;

import client.Player.Move;
import server.ServerMonitor;

public class ServerMonitorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerMonitor servMon = new ServerMonitor();
		Move m1 = servMon.getNextMove(1);
		servMon.putNextMove(1, m1);
	}

}
