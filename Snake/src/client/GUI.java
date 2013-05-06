package client;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Menu GUI
 * @author marcus
 * A GUI representing the games menu system allowing the user to create and join different game servers.
 */
public class GUI extends JFrame{


//TextFields

	private JTextField ipField;
	private JTextField manualServerName;
	private JTextField playerNameField;
	private JTextField serverNameField;
	private JTextField passwordField;
	private JTextField serverPortField;;


//Tables
	
	private JTable table;
//Panes
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
//Buttons
	
	private JButton btnConnect;
	private JButton btnNewGame;
	private JButton btnRefresh;
	
//Panels
	private JPanel serverPanel;
	private GamePanel game;
	
	
	
//TODO This data is temporary for display purposes, will be changed later.
//	private Object[][] serverData = {
//		    {"Kathy", "Smith",
//		     30000, new Integer(5), new Boolean(false)},
//		    {"John", "Doe",
//		     10, new Integer(3), new Boolean(true)},
//		    {"Sue", "Black",
//		     2000, new Integer(2), new Boolean(false)},
//		    {"Jane", "White",
//		     4311, new Integer(20), new Boolean(true)},
//		    {"Joe", "Brown",
//		     13431, new Integer(10), new Boolean(false)}
//		};

//		String[] columnNames = {"Server Name",
//                "Server IP",
//                "Server Port",
//                "# of Players",
//                "Private"};

	private Object[][] serverData = {
			{"1.2.3.4", 5678},
			{"111.222.33.44", 5555}
	};
	
	private String[] columnNames = {"Server IP", "Server port"};
	
	public GUI() {
		setTitle("MPSnake Pro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 666);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		settingsPanel.setLayout(null);
		
		JLabel lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(50, 72, 97, 16);
		settingsPanel.add(lblPlayerName);
		
		playerNameField = new JTextField();
		playerNameField.setBounds(50, 100, 134, 28);
		settingsPanel.add(playerNameField);
		playerNameField.setColumns(10);
		
		JLabel lblServerName = new JLabel("Server Name:");
		lblServerName.setBounds(50, 140, 97, 16);
		settingsPanel.add(lblServerName);
		
		serverNameField = new JTextField();
		serverNameField.setBounds(50, 168, 134, 28);
		settingsPanel.add(serverNameField);
		serverNameField.setColumns(10);
		
		btnNewGame = new JButton("New Game");
		btnNewGame.setBounds(66, 386, 117, 29);
		settingsPanel.add(btnNewGame);
		
		JLabel lblPassword = new JLabel("Password*:");
		lblPassword.setBounds(50, 276, 97, 16);
		settingsPanel.add(lblPassword);
		
		passwordField = new JTextField();
		passwordField.setBounds(50, 304, 134, 28);
		settingsPanel.add(passwordField);
		passwordField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(50, 208, 61, 16);
		settingsPanel.add(lblPort);
		
		serverPortField = new JTextField();
		serverPortField.setBounds(50, 236, 134, 28);
		settingsPanel.add(serverPortField);
		serverPortField.setColumns(10);
		
		serverPanel = new JPanel();
		tabbedPane.addTab("Servers", null, serverPanel, null);
		serverPanel.setLayout(null);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(436, 73, 117, 29);
		serverPanel.add(btnConnect);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(436, 125, 117, 29);
		serverPanel.add(btnRefresh);
		
		JLabel lblManualConnect = new JLabel("Manual Connect");
		lblManualConnect.setBounds(436, 213, 117, 16);
		serverPanel.add(lblManualConnect);
		
		ipField = new JTextField();
		ipField.setBounds(436, 258, 134, 28);
		serverPanel.add(ipField);
		ipField.setColumns(10);
		
		manualServerName = new JTextField();
		manualServerName.setBounds(436, 338, 134, 28);
		serverPanel.add(manualServerName);
		manualServerName.setColumns(10);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(436, 241, 61, 16);
		serverPanel.add(lblIp);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(436, 319, 61, 16);
		serverPanel.add(lblName);
		
		

		
		table = new JTable(serverData, columnNames);
		table.setBounds(22, 41, 375, 444);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(22, 41, 402, 444);
		table.setFillsViewportHeight(true);
		serverPanel.add(scrollPane);
	}
	
	// Methods that the controller uses to add actionlisteners to the GUI.
	public void setConnectListener(ActionListener list){
		btnConnect.addActionListener(list);
	} 
	
	public void setNewGameListener(ActionListener list){
		btnNewGame.addActionListener(list);
	}
	
	public void setRefreshListener(ActionListener list){
		btnRefresh.addActionListener(list);
	}
	
	
	//Methods that the controller uses to retrive values from the different textfields.
	public String getIp() {
		return ipField.getText();
	}

	public String getManualServerName() {
		return manualServerName.getText();
	}

	public String getPlayerName() {
		return playerNameField.getText();
	}

	public String getServerName() {
		return serverNameField.getText();
	}

	public String getPassword() {
		return passwordField.getText();
	}
	
	public String getServerPort() {
		return serverPortField.getText();
	}
	
	public Object[][] getServerTable(){
		return serverData;
	}
	
	public void setServerTable(Object[][] serverData){
		this.serverData = serverData;
		
//TODO find better way of updating the table, maybe something like:
//		DefaultTableModel tModel = (DefaultTableModel) table.getModel();
//		tModel.setDataVector(serverData, columnNames);		
		table = new JTable(serverData, columnNames);
		table.setBounds(22, 41, 375, 444);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(22, 41, 402, 444);
		table.setFillsViewportHeight(true);
		serverPanel.remove(serverPanel.getComponentCount()-1); //FIXME 
		serverPanel.add(scrollPane);
	}
	
	public GamePanel startGame(ClientMonitor monitor, int size){
		game = new GamePanel(monitor, size);
		tabbedPane.addTab("Game", null, game, null);
		tabbedPane.setSelectedIndex(2);
		return game;
	}
	
	public void removeGame(){
		tabbedPane.remove(game);
	}
	
	public Object[] getServer(){
		//TODO define selected server and return it.
		return null;
	}
	public static void main(String[] args){
		GUI gui = new GUI();
		gui.setVisible(true);
	}
}
