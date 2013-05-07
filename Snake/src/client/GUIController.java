package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class GUIController {
	private GUI gui;
	private Model model;
	private boolean gameOn;
	private GamePanel game;
	
	public GUIController(GUI gui, Model model){
		this.gui = gui;
		this.model = model;
		this.gui.setConnectListener(new ConnectListener());
		this.gui.setRefreshListener(new RefreshListener());
		this.gui.setNewGameListener(new NewGameListener());
	}
	/**
	 * An ActionListerner Class that handles the actions of the connect button.
	 * @author marcus
	 *
	 */
	private class ConnectListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String manualServerName = gui.getManualServerName();
			String manualServerPort = gui.getServerPort();
			if(manualServerName.equals(null) && manualServerPort.equals(null)){
				model.connectToServer(gui.getServer());
			}else{
				Object[] obj = new Object[2];
				obj[0] = gui.getManualServerName();
				obj[1] = gui.getPlayerName();
				model.connectToServer(obj);
			}
		}
		
	}
	
	/**
	 * An ActionListerner Class that handles the actions of the new game button.
	 * @author marcus
	 *
	 */
	private class NewGameListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(gameOn){
				model.closeGame();
				gui.removeGame();
			}
			model.initiateNewGame(gui.getServerName(), gui.getServerPort());
			game = gui.startGame(model.getMonitor(), 50);
			model.addGamePanel(game);
			model.startServer();
			//TODO server needs to tell when the game shall start
			model.startInitiatedGame();
			gameOn = true;
			
		}
		
	}
	
	/**
	 * An ActionListerner Class that handles the actions of the refresh button.
	 * @author marcus
	 *
	 */
	private class RefreshListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			gui.setServerTable(model.getServers());
		}
		
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);
		Model m = new Model();
		GUIController c = new GUIController(gui, m);
		
	}
}
