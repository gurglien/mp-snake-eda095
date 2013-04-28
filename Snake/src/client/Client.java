package client;

public class Client {
	
	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		
		int playfieldWidth = 50; // Detta räknas om till pixlar senare, varje rad/kolumn är 10 px bred, spelaren är också 10 px bred.
		ClientGameLoop game = new ClientGameLoop(monitor, playfieldWidth);
		PseudoServer serv = new PseudoServer(monitor, playfieldWidth);
		serv.start();
		game.start();
	}

}
