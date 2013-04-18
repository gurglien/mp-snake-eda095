package client;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;

/**
 * Menu GUI
 * @author marcus
 *
 */
public class GUI extends JFrame{
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	JButton btnConnect;
	private JTable table;
	
	
	public GUI() {
		setTitle("MPSnake Pro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 666);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		settingsPanel.setLayout(null);
		
		JLabel lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(50, 72, 97, 16);
		settingsPanel.add(lblPlayerName);
		
		textField_2 = new JTextField();
		textField_2.setBounds(50, 100, 134, 28);
		settingsPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblServerName = new JLabel("Server Name:");
		lblServerName.setBounds(50, 140, 97, 16);
		settingsPanel.add(lblServerName);
		
		textField_3 = new JTextField();
		textField_3.setBounds(50, 168, 134, 28);
		settingsPanel.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setBounds(60, 208, 117, 29);
		settingsPanel.add(btnNewGame);
		
		JPanel serverPanel = new JPanel();
		tabbedPane.addTab("Servers", null, serverPanel, null);
		serverPanel.setLayout(null);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(436, 73, 117, 29);
		serverPanel.add(btnConnect);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(436, 125, 117, 29);
		serverPanel.add(btnRefresh);
		
		JLabel lblManualConnect = new JLabel("Manual Connect");
		lblManualConnect.setBounds(436, 213, 117, 16);
		serverPanel.add(lblManualConnect);
		
		textField = new JTextField();
		textField.setBounds(436, 258, 134, 28);
		serverPanel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(436, 338, 134, 28);
		serverPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(436, 241, 61, 16);
		serverPanel.add(lblIp);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(436, 319, 61, 16);
		serverPanel.add(lblName);
		
		String[] columnNames = {"Server Name",
                "Server IP",
                "Server Port",
                "# of Current Players",
                "Private"};
		Object [][] data = new Object[5][10];
		table = new JTable(data, columnNames);
		table.setBounds(22, 41, 328, 444);
		serverPanel.add(table);
	}
	//CS was here
	
	public void setConnectListener(ActionListener list){
		btnConnect.addActionListener(list);
	} 
	
	public static void main(String[] args){
		GUI gui = new GUI();
		gui.setVisible(true);
	}
}
