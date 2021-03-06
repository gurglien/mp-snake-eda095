package client;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;



/**
 * Menu GUI
 * @author marcus
 * A GUI representing the games menu system allowing the user to create and join different game servers.
 */
public class GUI extends JFrame{


/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//TextFields

	private JTextField serverIpField;
	private JTextField serverPort;
	private JTextField createPortField;;


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
	

	private Object[][] serverData = { {"<refresh>", "<refresh>"} };
	private String[] columnNames = {"Server IP", "Server port"};
	
	public GUI() {
		setTitle("MPSnake Pro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 666);
		setResizable(false);
		tabbedPane = new JTabbedPane();
		
		

		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		settingsPanel.setLayout(null);
				
		btnNewGame = new JButton("New Game");
		btnNewGame.setBounds(60, 125, 117, 29);
		settingsPanel.add(btnNewGame);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(50, 57, 61, 16);
		settingsPanel.add(lblPort);
		
		createPortField = new JTextField();
		createPortField.setBounds(50, 85, 134, 28);
		settingsPanel.add(createPortField);
		createPortField.setColumns(10);
		
		JLabel lblWelcomeToMpsnake = new JLabel("Welcome to MPSnake Pro!");
		lblWelcomeToMpsnake.setBounds(340, 91, 196, 16);
		settingsPanel.add(lblWelcomeToMpsnake);
		
		JLabel lblPleaseEnterA = new JLabel("Please enter a free port number");
		lblPleaseEnterA.setBounds(327, 130, 295, 16);
		settingsPanel.add(lblPleaseEnterA);
		
		JLabel lblToCreateA = new JLabel(" to create a multiplayer server!");
		lblToCreateA.setBounds(327, 158, 196, 16);
		settingsPanel.add(lblToCreateA);
		
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
		
		serverIpField = new JTextField();
		serverIpField.setBounds(436, 258, 134, 28);
		serverPanel.add(serverIpField);
		serverIpField.setColumns(10);
		
		serverPort = new JTextField();
		serverPort.setBounds(436, 338, 134, 28);
		serverPanel.add(serverPort);
		serverPort.setColumns(10);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(436, 241, 61, 16);
		serverPanel.add(lblIp);
		
		JLabel lblName = new JLabel("Port");
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
	public String getManualIp() {
		return serverIpField.getText();
	}

	public String getManualServerPort() {
		return serverPort.getText();
	}

	
	public String getCreatePort() {
		return createPortField.getText();
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
		btnNewGame.setEnabled(false);
		btnConnect.setEnabled(false);
		return game;
	}
	
	public void enableNewGame() {
		btnNewGame.setEnabled(true);
		btnConnect.setEnabled(true);
	}
	
	public boolean isTableSelected(){
		int selected = table.getSelectedRow();
		if(selected == -1) return false;
		return true;
	}
	
	public void updateServerData(){
		serverIpField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
		serverPort.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
	}
	
	public void removeGame(){
		tabbedPane.remove(game);
	}
	
	public Object[] getServer(){
		//TODO define selected server and return it.
		return null;
	}
	
	public void unknownHost(){
		JOptionPane.showMessageDialog(this,
			    "Unknown Host, please check your spelling.",
			    "Inane warning",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	public void unknownPort(){
		JOptionPane.showMessageDialog(this,
			    "The portnumber was not recognized.",
			    "Inane warning",
			    JOptionPane.WARNING_MESSAGE);
	}
}
