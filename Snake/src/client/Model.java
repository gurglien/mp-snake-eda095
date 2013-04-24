package client;

import java.net.InetAddress;
import java.util.ArrayList;

import autodetect.ServerFinder;

public class Model {

	/**
	 * Retrives a matrix of the servers that are online currently.
	 * @return 
	 */
	public Object[][] getServers() {
		ServerFinder sf = new ServerFinder();
		sf.findServers(2000);
		ArrayList<InetAddress> addr = sf.getServerAddresses();
		ArrayList<Integer> ports = sf.getServerPorts();
		Object[][] servers = new Object[addr.size()][2]; //2 = number of columns in gui server table
		for (int i = 0; i < addr.size(); i++) {
			servers[i][0] = addr.get(i);
			servers[i][1] = ports.get(i);
		}
		return servers;
	}
	
	/**
	 * Creates a new game with the current paramaters resulting in a gameserver start and a game client start.
	 * @param serverName
	 * @param serverPort
	 * @param password
	 */
	public void startNewGame(String serverName, String serverPort,String password) {
		// TODO Password kan ignoreras s� l�nge d� det inte �r n�dv�ndigt, dock l�ttare att l�gga till senare om vi vill.
		
	}
	/**
	 * Connect to an already online server.
	 * @param server
	 */
	public void connectToServer(Object[] server) {
		// TODO Auto-generated method stub
		
	}

}
