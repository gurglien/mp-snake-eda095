package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import client.ClientMonitor.GameState;

import server.Server;
import server.ServerMonitor;

import autodetect.ServerFinder;

public class Model {
//	private ClientGameLoop game;
	private ClientMonitor clientMonitor;
	private ServerMonitor serverMonitor;
	private Server server;
	private Socket socket;
	private Client client;
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
	 */
	public void initiateNewGame(String serverPort) {
		int playfieldWidth = 60;
		int port = 0;
		if(serverPort.equals("")){
			System.err.println("Error: You need to specify a port number");
			System.exit(1);
		}else{
			port = Integer.parseInt(serverPort);
		}
		String host = "localhost"; // TODO Change hard coded "localhost", so it's possible to connect to other servers
		
		serverMonitor = new ServerMonitor();
		server = new Server(serverMonitor, playfieldWidth, port);
		server.start();
		
		try {
			socket = new Socket(host, port);
			clientMonitor = new ClientMonitor();
			client = new Client(clientMonitor, playfieldWidth, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeGame(){
		clientMonitor.setState(GameState.CLOSE);
		serverMonitor.setClientState(1, GameState.CLOSE);
		serverMonitor.setClientState(2, GameState.CLOSE);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client = null;
		server = null;
	}
	
	public void addGamePanel(GamePanel panel){
		client.setPanel(panel);
	}
	
//	public void startServer(){
//		if(server.equals(null)){
//			
//		}else{
//					server.start();
//		}
//	}
	
	public void startInitiatedGame(){
//		if(server.equals(null) && client.equals(null)){
//			//TODO game not yet initialised unsupported behaviuor.
//		}else{
			client.start();
//		}
	}
	

	public ClientMonitor getMonitor(){
		return clientMonitor;
	}
	/**
	 * Connect to an already online server.
	 * @param server
	 */
	public void connectToServer(Object[] server) {
		int playfieldWidth = 60;
		int port = 0;
		if(((String)server[1]).equals("")){
			System.err.println("Error: You need to specify a port number");
			System.exit(1);
		}else{
			port = Integer.parseInt(((String)server[1]));
		}
		String host = (String)server[0];
		try {
			socket = new Socket(host, port);
			clientMonitor = new ClientMonitor();
			client = new Client(clientMonitor, playfieldWidth, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
