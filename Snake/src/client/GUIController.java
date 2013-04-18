package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class GUIController {
	private GUI gui;
	private Model model;
	
	public GUIController(GUI gui, Model model){
		this.gui = gui;
		this.model = model;
		this.gui.setConnectListener(new ConnectListener());
		this.gui.setRefreshListener(new RefreshListener());
		this.gui.setNewGameListener(new NewGameListener());
	}
	
	private class ConnectListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.connectToServer(gui.getServer());
		}
		
	}
	
	private class NewGameListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			model.startNewGame(gui.getServerName(), gui.getServerPort(), gui.getPassword());
		}
		
	}
	
	private class RefreshListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			gui.setServerTable(model.getServers());
		}
		
	}
}
