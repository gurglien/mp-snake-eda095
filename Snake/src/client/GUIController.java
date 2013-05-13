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
			if(gui.isTableSelected()){
				gui.updateServerData();

			}
			Object[] obj = new Object[2];
			obj[0] = gui.getManualIp();
			obj[1] = gui.getManualServerPort();	
			model.connectToServer(obj);
			
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
			model.initiateNewGame(gui.getCreatePort());
			game = gui.startGame(model.getMonitor(), 60);
			model.addGamePanel(game);
//			model.startServer();
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
