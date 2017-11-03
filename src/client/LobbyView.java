package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import communicationObjects.DirectMessage;
import communicationObjects.GlobalMessage;
import communicationObjects.KickGuest;
import communicationObjects.LeaveLobby;
import communicationObjects.LobbyMessage;
import communicationObjects.StartGame;
import communicationObjects.User;

/**
 * public class LobbyView extends JPanel implements Observer.
 * This JPanel class is the ClientGui's lobby view which can be displayed on the ClientGui's main frame.
 * It is the view displayed when the user successfuly joins a lobby.
 * This view contains many JComponents layed out in a complex manner.
 * Its update method updates the view based upon the Client's Data object.
 * @author Oliver Kamperis
 */
public class LobbyView extends JPanel implements Observer {
	private static final long serialVersionUID = 6818089166501185209L;
	private final Client client;
	private final Data data;
	
	private final JTextPane displayPane;
	private final JTextField hostUserNameField;
	private final JTextField hostGamesPlayedField;
	private final JTextField hostGamesWonField;
	private final JTextField hostGamesLostField;
	private final JTextField hostRatingField;
	private final JTextField guestUserNameField;
	private final JTextField guestGamesPlayedField;
	private final JTextField guestGamesWonField;
	private final JTextField guestGamesLostField;
	private final JTextField guestRatingField;
	private final JTextField queuePositionField;
	private final JTextField gameNameField;
	
	private final JButton startGameButton;
	private final JButton kickGuestButton;
	private final JButton leaveLobbyButton;
	private final JPanel buttonsPanel;
	private final JTabbedPane messagingTabs;
	private final JList<String> globalMessagingPane;
	private final JList<String> lobbyMessagingPane;
	private final JList<String> directMessagingPane;
	private final JTextField messagingField;
	
	/**
	 * Constructs a new LobbyView to interact with a given Client object.
	 * Its initial state has all its JComponents initialized but aren't populated with data.
	 * @param client - The Client to make the Gui view for
	 */
	public LobbyView(Client client) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.client = client;
		this.data = client.getData();
		
		this.displayPane = new JTextPane();
		this.hostUserNameField = new JTextField();
		this.hostGamesPlayedField = new JTextField();
		this.hostGamesWonField = new JTextField();
		this.hostGamesLostField = new JTextField();
		this.hostRatingField = new JTextField();
		this.guestUserNameField = new JTextField();
		this.guestGamesPlayedField = new JTextField();
		this.guestGamesWonField = new JTextField();
		this.guestGamesLostField = new JTextField();
		this.guestRatingField = new JTextField();
		this.queuePositionField = new JTextField();
		this.gameNameField = new JTextField();
		
		this.startGameButton = new JButton("Start game!");
		this.kickGuestButton = new JButton("Kick guest");
		this.leaveLobbyButton = new JButton();
		this.buttonsPanel = new JPanel();
		this.messagingTabs = new JTabbedPane();
		this.globalMessagingPane = new JList<>();
		this.lobbyMessagingPane = new JList<>();
		this.directMessagingPane = new JList<>();
		this.messagingField = new JTextField();
		
		this.addDisplayPane();
		this.populateAndAddTopPanel();
		this.populateAndAddMiddlePanel();
		this.populateAndAddBottomPanel();
	}
	
	/**
	 * Updates the display panel at the top of the view based on the data's text field.
	 * All the messaging panes and the host/guest/game information fields are also updated.
	 * The button visibility is also changed depending on whether the user is the host or the guest and whether the game is ready to start.
	 */
	@Override
	public void update(Observable o, Object arg) {
		displayPane.setText(data.getText());
		
		gameNameField.setText("Game: " + data.getMyLobby().getName() + " Players: " + data.getMyLobby().getNumPlayers() + "/2");
		hostUserNameField.setText("Name: " + data.getMyLobby().getHost().getUsername());
		hostGamesPlayedField.setText("Played: " + data.getMyLobby().getHost().getNumberOfGamesPlayed());
		hostGamesWonField.setText("Won: " + data.getMyLobby().getHost().getNumberOfWins());
		hostGamesLostField.setText("Lost: " + data.getMyLobby().getHost().getNumberOfLosses());
		hostRatingField.setText("Rating: " + data.getMyLobby().getHost().getRating());
		
		if (data.getMyLobby().getGuest() != null) {
			guestUserNameField.setText("Name: " + data.getMyLobby().getGuest().getUsername());
			guestGamesPlayedField.setText("Played: " + data.getMyLobby().getGuest().getNumberOfGamesPlayed());
			guestGamesWonField.setText("Won: " + data.getMyLobby().getGuest().getNumberOfWins());
			guestGamesLostField.setText("Lost: " + data.getMyLobby().getGuest().getNumberOfLosses());
			guestRatingField.setText("Rating: " + data.getMyLobby().getGuest().getRating());
		} else {
			guestUserNameField.setText("Name:");
			guestGamesPlayedField.setText("Played:");
			guestGamesWonField.setText("Won:");
			guestGamesLostField.setText("Lost:");
			guestRatingField.setText("Rating:");
		}
		
		queuePositionField.setText("Queue position: " + data.getMyLobby().getIndex());
		
		if (data.getMyLobby().getIndex() == 0) {
			startGameButton.setVisible(true);
		} else {
			startGameButton.setVisible(false);
		}
		
		buttonsPanel.removeAll();
		if (data.getMyLobby().getHost().equals(data.getUser())) {
			buttonsPanel.setLayout(new GridLayout(2, 1));
			leaveLobbyButton.setText("Close lobby");
			buttonsPanel.add(kickGuestButton);
			buttonsPanel.add(leaveLobbyButton);
		} else {
			leaveLobbyButton.setText("Leave lobby");
			buttonsPanel.setLayout(new GridLayout(1, 1));
			buttonsPanel.add(leaveLobbyButton);
		}
		
		this.repaint();
		this.setVisible(true);
		
		globalMessagingPane.setListData(data.getGlobalChat().toArray(new String[data.getGlobalChat().size()]));
		lobbyMessagingPane.setListData(data.getLobbyChat().toArray(new String[data.getLobbyChat().size()]));
		directMessagingPane.setListData(data.getDirectChat().toArray(new String[data.getDirectChat().size()]));
	}
	
	/**
	 * Populates the display panel of the view with one display JTextPane and then adds it to the main panel.
	 */
	public void addDisplayPane() {
		displayPane.setEditable(false);
		displayPane.setPreferredSize(new Dimension(550, 40));
		
		JPanel displayPanel = new JPanel();
		displayPanel.add(displayPane);
		displayPanel.setPreferredSize(new Dimension(600, 50));
		
		this.add(displayPanel);
	}
	
	/**
	 * Populates the upper most panel of the view with the game, host and guest information.
	 */
	public void populateAndAddTopPanel() {
		hostUserNameField.setEditable(false);
		hostGamesPlayedField.setEditable(false);
		hostGamesWonField.setEditable(false);
		hostGamesLostField.setEditable(false);
		hostRatingField.setEditable(false);
		guestUserNameField.setEditable(false);
		guestGamesPlayedField.setEditable(false);
		guestGamesWonField.setEditable(false);
		guestGamesLostField.setEditable(false);
		guestRatingField.setEditable(false);
		gameNameField.setEditable(false);
		gameNameField.setFont(new Font("", 30, 30));
		gameNameField.setPreferredSize(new Dimension(500, 50));
		
		JPanel hostPanel = new JPanel();
		hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.X_AXIS));
		hostPanel.add(hostUserNameField);
		hostPanel.add(hostGamesPlayedField);
		hostPanel.add(hostGamesWonField);
		hostPanel.add(hostGamesLostField);
		hostPanel.add(hostRatingField);
		hostPanel.setPreferredSize(new Dimension(600, 50));
		
		JPanel guestPanel = new JPanel();
		guestPanel.setLayout(new BoxLayout(guestPanel, BoxLayout.X_AXIS));
		guestPanel.add(guestUserNameField);
		guestPanel.add(guestGamesPlayedField);
		guestPanel.add(guestGamesWonField);
		guestPanel.add(guestGamesLostField);
		guestPanel.add(guestRatingField);
		guestPanel.setPreferredSize(new Dimension(600, 50));
		
		JPanel hostDisplay = new JPanel() {
			private static final long serialVersionUID = 8579608584760497805L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 40));
				g.drawString("Host:", 0, 40);
			}
		};
		hostDisplay.setPreferredSize(new Dimension(600, 50));
		
		JPanel guestDisplay = new JPanel() {
			private static final long serialVersionUID = 8579608584760497805L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 40));
				g.drawString("Guest:", 0, 40);
			}
		};
		guestDisplay.setPreferredSize(new Dimension(600, 50));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(gameNameField);
		topPanel.add(hostDisplay);
		topPanel.add(hostPanel);
		topPanel.add(guestDisplay);
		topPanel.add(guestPanel);
		topPanel.setPreferredSize(new Dimension(600, 250));
		
		this.add(topPanel);
	}
	
	/**
	 * Populates the middle panel of the view with the queue position display and the start game button.
	 */
	public void populateAndAddMiddlePanel() {
		queuePositionField.setFont(new Font("", 30, 40));
		queuePositionField.setPreferredSize(new Dimension(500, 50));
		queuePositionField.setEditable(false);
		
		startGameButton.addActionListener(new StartGameListener());
		startGameButton.setPreferredSize(new Dimension(500, 50));
		
		JPanel queuePositionDisplayPanel = new JPanel();
		queuePositionDisplayPanel.add(queuePositionField);
		
		JPanel startGameButtonPanel = new JPanel();
		startGameButtonPanel.add(startGameButton);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(queuePositionDisplayPanel);
		middlePanel.add(startGameButtonPanel);
		middlePanel.setPreferredSize(new Dimension(600, 130));
		
		this.add(middlePanel);
	}
	
	/**
	 * Populates the bottom most panel of the view with the messaging panel and buttons.
	 * The "Kick guest" and "Close lobby" button if the user is the host of the lobby.
	 * Only the "Leave lobby" button if the user is the guest of the lobby.
	 */
	public void populateAndAddBottomPanel() {
		globalMessagingPane.setLayoutOrientation(JList.VERTICAL);
		globalMessagingPane.setVisibleRowCount(-1);
		JScrollPane globalMessagingScrollPane = new JScrollPane(globalMessagingPane);
		globalMessagingScrollPane.setName("Global chat");
		
		lobbyMessagingPane.setLayoutOrientation(JList.VERTICAL);
		lobbyMessagingPane.setVisibleRowCount(-1);
		JScrollPane lobbyMessagingScrollPane = new JScrollPane(lobbyMessagingPane);
		lobbyMessagingScrollPane.setName("Game/lobby chat");
		
		directMessagingPane.setLayoutOrientation(JList.VERTICAL);
		directMessagingPane.setVisibleRowCount(-1);
		JScrollPane directMessagingScrollPane = new JScrollPane(directMessagingPane);
		directMessagingScrollPane.setName("Direct chat");
		
		messagingTabs.addTab("Global chat", globalMessagingScrollPane);
		messagingTabs.addTab("Game/lobby chat", lobbyMessagingScrollPane);
		messagingTabs.addTab("Direct chat", directMessagingScrollPane);
		messagingTabs.setPreferredSize(new Dimension(300, 170));
		
		messagingField.setEditable(true);
		messagingField.addActionListener(new SendMessage());
		messagingField.setText("Type messages here, press enter to send");
		
		kickGuestButton.addActionListener(new KickGuestListener());
		leaveLobbyButton.addActionListener(new LeaveLobbyListener());
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.add(messagingTabs);
		chatPanel.add(messagingField);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		bottomPanel.add(buttonsPanel);
		bottomPanel.add(chatPanel);
		bottomPanel.setPreferredSize(new Dimension(600, 200));
		
		this.add(bottomPanel);
	}
	
	/**
	 * Sends a message to the server.
	 * The type of the message is determined by which messaging tab the user currently has selected.
	 * If the "Global chat" tab is selected the message will be sent if it is alphanumeric with spaces.
	 * If the "Game/Lobby chat" tab is selected the message will be sent if there is a selected lobby in the lobby list pane and if it is alphanumeric with spaces.
	 * If the "Direct chat" tab is selected the message will be sent if the user has typed the message in the following format //playerName//message, if the message is alphanumeric with spaces, the player name is alphanumeric and the specified player is currently online.
	 * @author Oliver Kamperis
	 */
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
			} else if (tabName.equals("Game/lobby chat")) {
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
	
	/**
	 * public class StartGameListener implements ActionListener.
	 * This ActionListener class sends a StartGame request to the server if the lobby is at index zero in the lobby list.
	 * This ActionListener is assigned only to the "Start game" button.
	 */
	public class StartGameListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (data.getMyLobby().getIndex() != 0) {
				data.setText("You are not first in the queue to start your game!");
			} else {
				client.sendRequest(new StartGame(""));
			}
		}
	}
	
	/**
	 * public class KickGuestListener implements ActionListener.
	 * This ActionListener class sends a KickGuest request to the server if there is a guest in the lobby.
	 * This ActionListener is assigned only to the "Kick guest" button.
	 */
	public class KickGuestListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (data.getMyLobby().getNumPlayers() == 1) {
				data.setText("There is no guest to kick!");
			} else {
				client.sendRequest(new KickGuest(""));
			}
		}
	}
	
	/**
	 * public class LeaveLobbyListener implements ActionListener.
	 * This ActionListener class sends a LeaveLobby request to the server.
	 * This ActionListener is assigned only to the "Join lobby" button.
	 */
	public class LeaveLobbyListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			client.sendRequest(new LeaveLobby(""));
		}
	}
}
