package client;

public class Client {
	
	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		
		int playfieldWidth = 50; // Detta r�knas om till pixlar senare, varje rad/kolumn �r 10 px bred, spelaren �r ocks� 10 px bred.
		ClientGameLoop game = new ClientGameLoop(monitor, playfieldWidth);
		PseudoServer serv = new PseudoServer(monitor, playfieldWidth);
		serv.start();
		game.start();
	}

}
