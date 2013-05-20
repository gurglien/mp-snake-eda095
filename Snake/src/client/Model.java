package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import client.ClientMonitor.GameState;

import server.Server;
import server.ServerMonitor;

import autodetect.DServer;
import autodetect.ServerFinder;

public class Model {
//	private ClientGameLoop game;
	private ClientMonitor clientMonitor;
	private ServerMonitor serverMonitor;
	private Server server;
	private Socket socket;
	private Client client;
	private DServer detector;
	
	volatile boolean serverReady;
	int playfieldWidth = 59;
	int port = 0;
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
		System.out.println("initiateNewGame");
		if(serverPort.equals("")){
			port = 5000;
		}else{
			port = Integer.parseInt(serverPort);
		}
		String host = "localhost";
		
		serverMonitor = new ServerMonitor();
		server = new Server(serverMonitor, playfieldWidth, port, this);
		server.start();
		while(!serverReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
//			System.out.println(1);
			
			socket = new Socket(host, port);
			System.out.println(2);
			clientMonitor = new ClientMonitor();
			System.out.println(3);
			client = new Client(clientMonitor, playfieldWidth, socket, this);
			System.out.println("new Client");
			detector = new DServer(port+5, 10000);
			detector.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void serverReady() {
		serverReady = true;
		notifyAll();
	}
	
	public void closeGame(){
		clientMonitor.setState(GameState.CLOSE);
		
		if(detector != null){
			detector.interrupt();
		}
		
		try {
			socket.close();
			if(detector != null){
				detector.join();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client = null;
		server = null;
		detector = null;
	}
	
	public void addGamePanel(GamePanel panel){
		client.setPanel(panel);
	}
	
	public void startInitiatedGame(){
		client.start();
	}
	

	public ClientMonitor getMonitor(){
		return clientMonitor;
	}
	/**
	 * Connect to an already online server.
	 * @param server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NoPortException 
	 */
	public void connectToServer(Object[] server) throws UnknownHostException, IOException, NoPortException {
		int playfieldWidth = 59;
		int port = 0;
		if(((String)server[1]).equals("")){
			throw new NoPortException();
		}else{
			try{
				port = Integer.parseInt(((String)server[1]));
			}catch(NumberFormatException e){
				throw new NoPortException();
			}
			
		}
		String host = (String)server[0];
		while(host.charAt(0) == '/'){
			host = host.substring(1);
		}
		socket = new Socket(host, port);
		clientMonitor = new ClientMonitor();
		client = new Client(clientMonitor, playfieldWidth, socket, this);

	}
	
	
	public class NoPortException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8813013410854236792L;
		
	}
}