package client;

import java.net.InetAddress;
import java.util.ArrayList;

import autodetect.ServerFinder;

public class Model {
	private ClientGameLoop game;
	private PseudoServer serv;
	private ClientMonitor monitor;
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
	public void initiateNewGame(String serverName, String serverPort,String password) {
		monitor = new ClientMonitor();		
		int playfieldWidth = 50; // Detta räknas om till pixlar senare, varje rad/kolumn är 10 px bred, spelaren är också 10 px bred.
		game = new ClientGameLoop(monitor, playfieldWidth);
		//skall Šndras till en riktig server som anvŠnder sig av serverPort och liknande TODO
		serv = new PseudoServer(monitor, playfieldWidth); 
	}
	
	public void addGamePanel(GamePanel panel){
		game.setPanel(panel);
	}
	
	public void startServer(){
		if(serv.equals(null)){
			
		}else{
					serv.start();
		}
	}
	
	public void startInitiatedGame(){
		if(serv.equals(null) && game.equals(null)){
			//TODO game not yet initialised unsupported behaviuor.
		}else{
		
		game.start();
		}
	}
	

	public ClientMonitor getMonitor(){
		return monitor;
	}
	/**
	 * Connect to an already online server.
	 * @param server
	 */
	public void connectToServer(Object[] server) {
		// TODO Auto-generated method stub
		
	}

}
