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
import javax.swing.ListSelectionModel;

import communicationObjects.CreateLobby;
import communicationObjects.DirectMessage;
import communicationObjects.GlobalMessage;
import communicationObjects.JoinLobby;
import communicationObjects.Lobby;
import communicationObjects.LobbyMessage;
import communicationObjects.Logout;
import communicationObjects.User;

/**
 * public class MainView extends JPanel implements Observer.
 * This JPanel class is the ClientGui's main view which can be displayed on the ClientGui's main frame.
 * It is the view displayed when the user successfuly logs in to the server but is yet to enter a lobby.
 * This view contains many JComponents layed out in a complex manner.
 * Its update method updates the view based upon the Client's Data object.
 * @author Oliver Kamperis
 */
public class MainView extends JPanel implements Observer {
	private static final long serialVersionUID = 7849692873629126576L;
	private final Client client;
	private final Data data;
	
	private final JTextPane displayPane;
	private final JTextField userNameField;
	private final JTextField gamesPlayedField;
	private final JTextField gamesWonField;
	private final JTextField gamesLostField;
	private final JTextField ratingField;
	
	private final JList<User> playersListPane;
	private final JList<Lobby> lobbiesListPane;
	private final JList<Lobby> gamesListPane;
	
	private final JButton joinLobbyButton;
	private final JButton createLobbyButton;
	private final JButton logoutButton;
	private final JTabbedPane messagingTabs;
	private final JList<String> globalMessagingPane;
	private final JList<String> lobbyMessagingPane;
	private final JList<String> directMessagingPane;
	private final JTextField messagingField;
	
	/**
	 * Constructs a new MainView to interact with a given Client object.
	 * Its initial state has all its JComponents initialized but aren't populated with data.
	 * @param client - The Client to make the Gui view for
	 */
	public MainView(Client client) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.client = client;
		this.data = client.getData();
		
		this.displayPane = new JTextPane();
		this.userNameField = new JTextField();
		this.gamesPlayedField = new JTextField();
		this.gamesWonField = new JTextField();
		this.gamesLostField = new JTextField();
		this.ratingField = new JTextField();
		
		this.playersListPane = new JList<>();
		this.lobbiesListPane = new JList<>();
		this.gamesListPane = new JList<>();
		
		this.joinLobbyButton = new JButton("Join lobby");
		this.createLobbyButton = new JButton("Create lobby");
		this.logoutButton = new JButton("Logout");
		
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
	 * All the list panes, the messaging panes and the user information fields are also updated.
	 */
	@Override
	public void update(Observable o, Object arg) {
		displayPane.setText(data.getText());
		
		userNameField.setText("Username: " + data.getUser().getUsername());
		gamesPlayedField.setText("Played: " + data.getUser().getNumberOfGamesPlayed());
		gamesWonField.setText("Won: " + data.getUser().getNumberOfWins());
		gamesLostField.setText("Lost: " + data.getUser().getNumberOfLosses());
		ratingField.setText("Rating: " + data.getUser().getRating());
		
		playersListPane.setListData(data.getPlayers().toArray(new User[data.getPlayers().size()]));
		lobbiesListPane.setListData(data.getLobbies().toArray(new Lobby[data.getLobbies().size()]));
		gamesListPane.setListData(data.getGames().toArray(new Lobby[data.getGames().size()]));
		
		globalMessagingPane.setListData(data.getGlobalChat().toArray(new String[data.getGlobalChat().size()]));
		lobbyMessagingPane.setListData(data.getLobbyChat().toArray(new String[data.getLobbyChat().size()]));
		directMessagingPane.setListData(data.getDirectChat().toArray(new String[data.getDirectChat().size()]));
		
		repaint();
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
	 * Populates the upper most panel of the view with the user's information.
	 */
	public void populateAndAddTopPanel() {
		userNameField.setEditable(false);
		gamesPlayedField.setEditable(false);
		gamesWonField.setEditable(false);
		gamesLostField.setEditable(false);
		ratingField.setEditable(false);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(userNameField);
		topPanel.add(gamesPlayedField);
		topPanel.add(gamesWonField);
		topPanel.add(gamesLostField);
		topPanel.add(ratingField);
		topPanel.setPreferredSize(new Dimension(600, 30));
		
		this.add(topPanel);
	}
	
	/**
	 * Populates the middle panel of the view with three JListPanes, one for the list of lobbies, one for the list of games and for the list of players.
	 */
	public void populateAndAddMiddlePanel() {
		gamesListPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gamesListPane.setLayoutOrientation(JList.VERTICAL);
		gamesListPane.setVisibleRowCount(-1);
		JScrollPane gamesListScroller = new JScrollPane(gamesListPane);
		gamesListScroller.setPreferredSize(new Dimension(300, 95));
		
		lobbiesListPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lobbiesListPane.setLayoutOrientation(JList.VERTICAL);
		lobbiesListPane.setVisibleRowCount(-1);
		JScrollPane lobbiesListScroller = new JScrollPane(lobbiesListPane);
		lobbiesListScroller.setPreferredSize(new Dimension(300, 195));
		
		playersListPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playersListPane.setLayoutOrientation(JList.VERTICAL);
		playersListPane.setVisibleRowCount(-1);
		JScrollPane playersListScroller = new JScrollPane(playersListPane);
		playersListScroller.setPreferredSize(new Dimension(300, 320));
		
		JPanel gamesDisplay = new JPanel() {
			private static final long serialVersionUID = 7111175342267093331L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Games:", 0, 25);
			}
		};
		gamesDisplay.setPreferredSize(new Dimension(300, 30));
		
		JPanel lobbiesDisplay = new JPanel() {
			private static final long serialVersionUID = 5544429205964803253L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Lobbies:", 0, 25);
			}
		};
		lobbiesDisplay.setPreferredSize(new Dimension(300, 30));
		
		JPanel playersDisplay = new JPanel() {
			private static final long serialVersionUID = 8579608584760497805L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 25));
				g.drawString("Players:", 0, 25);
			}
		};
		playersDisplay.setPreferredSize(new Dimension(300, 30));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(gamesDisplay);
		leftPanel.add(gamesListScroller);
		leftPanel.add(lobbiesDisplay);
		leftPanel.add(lobbiesListScroller);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(playersDisplay);
		rightPanel.add(playersListScroller);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(1, 2));
		middlePanel.add(leftPanel);
		middlePanel.add(rightPanel);
		middlePanel.setPreferredSize(new Dimension(600, 350));
		
		this.add(middlePanel);
	}
	
	/**
	 * Populates the bottom most panel of the view with the messaging panel and three buttons.
	 * The "Create lobby", "Join lobby" and "Logout" buttons.
	 */
	public void populateAndAddBottomPanel() {
		joinLobbyButton.addActionListener(new JoinLobbyListener());
		createLobbyButton.addActionListener(new CreateLobbyListener());
		logoutButton.addActionListener(new LogoutListener());
		
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
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(3, 1));
		buttonsPanel.add(joinLobbyButton);
		buttonsPanel.add(createLobbyButton);
		buttonsPanel.add(logoutButton);
		
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
	 * If the "Direct chat" tab is selected the message will be sent if there is a selected player in the player list pane and if it is alphanumeric with spaces.
	 * @author Oliver Kamperis
	 */
	public class SendMessage implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String tabName;
			String message;
			Lobby selectedLobby;
			Lobby selectedGame;
			User selectedPlayer;
			if ((tabName = messagingTabs.getSelectedComponent().getName()) == null) {
				data.setText("No chat tab selected!");
			} else if ((message = messagingField.getText()) == null || message.equals("")) {
				data.setText("There is no message to send!");
			} else if (!ClientGui.isAlphaNumericWithSpaces(message)) {
				data.setText("Message must be alphanumeric!");
			} else if (tabName.equals("Direct chat")) {
				if ((selectedPlayer = playersListPane.getSelectedValue()) == null) {
					data.setText("No player selected!");
				} else {
					data.setText("Direct message sent to player: " + selectedPlayer.getUsername() + "!");
					data.addToDirectChat("Me to " + selectedPlayer.getUsername() + ": " + message);
					client.sendMessage(new DirectMessage(data.getUser().getUsername() + ": " + message, selectedPlayer.getUsername()));
					messagingField.setText("");
				}
			} else if (tabName.equals("Game/lobby chat")) {
				if ((selectedLobby = lobbiesListPane.getSelectedValue()) == null && gamesListPane.getSelectedValue() == null) {
					data.setText("No lobby or game selected!");
				} else if ((selectedGame = gamesListPane.getSelectedValue()) != null && selectedLobby != null) {
					data.setText("Please select only one lobby or one game!");
				} else if (selectedLobby != null) {
					data.setText("Lobby message sent to: " + selectedLobby.getName() + "!");
					data.addToLobbyChat("Me to " + selectedLobby.getName() + ": " + message);
					client.sendMessage(new LobbyMessage(data.getUser().getUsername() + ": " + message, selectedLobby.getName()));
					messagingField.setText("");
				} else {
					data.setText("Game message sent to: " + selectedGame.getName() + "!");
					data.addToLobbyChat("Me to " + selectedGame.getName() + ": " + message);
					client.sendMessage(new LobbyMessage(data.getUser().getUsername() + ": " + message, selectedGame.getName()));
					messagingField.setText("");
				}
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
	 * public class JoinLobbyListener implements ActionListener.
	 * This ActionListener class sends a JoinLobby request to the server if there is a selected lobby in the lobby list pane, the selected lobby is not full and the selected lobby is not currently being created.
	 * This ActionListener is assigned only to the "Join lobby" button.
	 */
	public class JoinLobbyListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Lobby selectedLobby;
			if ((selectedLobby = lobbiesListPane.getSelectedValue()) == null) {
				data.setText("No lobby selected!");
			} else if (selectedLobby.getNumPlayers() == 2) {
				data.setText("Selected lobby is full!");
			} else if (selectedLobby.getName().equals("Creating lobby")) {
				data.setText("Cannot join a lobby that is currently being created!");
			} else {
				client.sendRequest(new JoinLobby("", selectedLobby));
			}
		}
	}
	
	/**
	 * public class CreateLobbyListener implements ActionListener.
	 * This ActionListener class sends a CreateLobby request to the server if there is less than six lobbies in the lobby list.
	 * This ActionListener is assigned only to the "Create lobby" button.
	 */
	public class CreateLobbyListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (data.getLobbies().size() >= 6) {
				data.setText("Cannot create a lobby as the queue is full!");
			} else {
				client.sendRequest(new CreateLobby(""));
			}
		}
	}
	
	/**
	 * public class LogoutListener implements ActionListener.
	 * This ActionListener class sends a Logout request to the server.
	 * This ActionListener is assigned only to the "Logout" button.
	 */
	public class LogoutListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			client.sendRequest(new Logout(""));
		}
	}
}
