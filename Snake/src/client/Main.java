package client;

public class Main {
	
	/**
	 * One main to rule them all, one main to find them,
	 * One main to bring them all and in the darkness bind them.
	 * @param args
	 */
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);
		Model m = new Model();
		GUIController c = new GUIController(gui, m);		
	}
}
