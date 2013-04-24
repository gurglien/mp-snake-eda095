package client;

public class Client {
	
	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		ClientGameLoop game = new ClientGameLoop(monitor);
		PseudoServer serv = new PseudoServer(monitor);
		serv.start();
		game.start();
	}

}
