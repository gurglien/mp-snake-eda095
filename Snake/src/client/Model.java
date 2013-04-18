package client;

public class Model {

	/**
	 * Retrives a matrix of the servers that are online currently.
	 * @return 
	 */
	public Object[][] getServers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Creates a new game with the current paramaters resulting in a gameserver start and a game client start.
	 * @param serverName
	 * @param serverPort
	 * @param password
	 */
	public void startNewGame(String serverName, String serverPort,String password) {
		// TODO Password kan ignoreras så länge då det inte är nödvändigt, dock lättare att lägga till senare om vi vill.
		
	}
	/**
	 * Connect to an already online server.
	 * @param server
	 */
	public void connectToServer(Object[] server) {
		// TODO Auto-generated method stub
		
	}

}
