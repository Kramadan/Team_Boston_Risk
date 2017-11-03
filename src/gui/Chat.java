package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import client.Client;
import client.ClientGui;
import client.Data;
import client.LobbyView.KickGuestListener;
import client.LobbyView.LeaveLobbyListener;
import client.LobbyView.SendMessage;
import communicationObjects.DirectMessage;
import communicationObjects.GlobalMessage;
import communicationObjects.LobbyMessage;
import communicationObjects.User;

/**
 * This GUI is for a simple chat system - Still needs to be edited.
 *
 * @author Oliver Kamperis and Abdikhaliq Timer
 */
public class Chat extends JFrame implements Observer {

	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;
	
	private Client client;
	private Data data;
	
	private final JTabbedPane messagingTabs;
	private final JList<String> globalMessagingPane;
	private final JList<String> lobbyMessagingPane;
	private final JList<String> directMessagingPane;
	private final JTextField messagingField;
	private final JTextPane displayPane;

	/**
	 * Creates new form Chat
	 */
	public Chat(Client client) {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Chat window");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		this.client = client;
		this.data = client.getData();
		data.addObserver(this);
		
		this.displayPane = new JTextPane();
		this.messagingTabs = new JTabbedPane();
		this.globalMessagingPane = new JList<>();
		this.lobbyMessagingPane = new JList<>();
		this.directMessagingPane = new JList<>();
		this.messagingField = new JTextField();
		
		populateAndAddMainPanel();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		displayPane.setText(data.getText());
		
		globalMessagingPane.setListData(data.getGlobalChat().toArray(new String[data.getGlobalChat().size()]));
		lobbyMessagingPane.setListData(data.getLobbyChat().toArray(new String[data.getLobbyChat().size()]));
		directMessagingPane.setListData(data.getDirectChat().toArray(new String[data.getDirectChat().size()]));
	}

	public void populateAndAddMainPanel() {
		displayPane.setEditable(false);
		displayPane.setPreferredSize(new Dimension(290, 40));
		
		JPanel displayPanel = new JPanel();
		displayPanel.add(displayPane);
		displayPanel.setPreferredSize(new Dimension(300, 50));
		
		globalMessagingPane.setLayoutOrientation(JList.VERTICAL);
		globalMessagingPane.setVisibleRowCount(-1);
		JScrollPane globalMessagingScrollPane = new JScrollPane(globalMessagingPane);
		globalMessagingScrollPane.setName("Global chat");
		
		lobbyMessagingPane.setLayoutOrientation(JList.VERTICAL);
		lobbyMessagingPane.setVisibleRowCount(-1);
		JScrollPane lobbyMessagingScrollPane = new JScrollPane(lobbyMessagingPane);
		lobbyMessagingScrollPane.setName("Game chat");
		
		directMessagingPane.setLayoutOrientation(JList.VERTICAL);
		directMessagingPane.setVisibleRowCount(-1);
		JScrollPane directMessagingScrollPane = new JScrollPane(directMessagingPane);
		directMessagingScrollPane.setName("Direct chat");
		
		messagingTabs.addTab("Global chat", globalMessagingScrollPane);
		messagingTabs.addTab("Game chat", lobbyMessagingScrollPane);
		messagingTabs.addTab("Direct chat", directMessagingScrollPane);
		messagingTabs.setPreferredSize(new Dimension(300, 220));
		
		messagingField.setEditable(true);
		messagingField.addActionListener(new SendMessage());
		messagingField.setText("Type messages here, press enter to send");

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.add(messagingTabs);
		chatPanel.add(messagingField);
		chatPanel.setPreferredSize(new Dimension(300, 250));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(displayPanel);
		mainPanel.add(chatPanel);
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		this.add(mainPanel);
	}

	
public class SendMessage implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String tabName;
			String message;
			if ((tabName = messagingTabs.getSelectedComponent().getName()) == null) {
				data.setText("No chat tab selected!");
			} else if ((message = messagingField.getText()) == null || message.equals("")) {
				data.setText("There is no message to send!");
			} else if (tabName.equals("Direct chat")) {
				String[] splitMessage;
				if ((splitMessage = messagingField.getText().split("//")).length != 3) {
					data.setText("Message is not formatted correctly!");
				} else if (!ClientGui.isAlphaNumeric(splitMessage[1])) {
					data.setText("Player name must be alphanumeric!");
				} else if (!ClientGui.isAlphaNumericWithSpaces(splitMessage[2])) {
					data.setText("Message must be alphanumeric!");
				} else if (!data.getPlayers().contains(new User(0, splitMessage[1], 0, 0, 0))) {
					data.setText("That player is either not online or does not exist!");
				} else {
					data.setText("Direct message sent to player: " + splitMessage[1] + "!");
					data.addToDirectChat("Me to " + splitMessage[1] + ": " + splitMessage[2]);
					client.sendMessage(new DirectMessage(data.getUser().getUsername() + ": " + splitMessage[2], splitMessage[1]));
					messagingField.setText("");
				}
			} else if (!ClientGui.isAlphaNumericWithSpaces(message)) {
				data.setText("Message must be alphanumeric!");
			} else if (tabName.equals("Game chat")) {
				data.setText("Lobby message sent to: " + data.getMyLobby().getName() + "!");
				data.addToLobbyChat("Me to " + data.getMyLobby().getName() + ": " + message);
				client.sendMessage(new LobbyMessage(data.getUser().getUsername() + ": " + message, data.getMyLobby().getName()));
				messagingField.setText("");
			} else if (tabName.equals("Global chat")) {
				data.setText("Global message sent!");
				data.addToGlobalChat("Me: " + message);
				client.sendMessage(new GlobalMessage(data.getUser().getUsername() + ": " + message));
				messagingField.setText("");
			} else {
				data.setText("The selected chat tab could not be determined!");
			}
		}
	}
}
