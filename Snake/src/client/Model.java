package client;

public class Model {

	/**
	 * Retrives a matrix of the servers that are online currently.
	 * @return 
	 */
	public Object[][] getServers() {
		// TODO Auto-generated method stub
		return new Object[][] {
//					{"1.0.0.4", 5678},
//					{"111.1.3.4", 5555},
					{"99.88.77.66", 5678}
				};
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
