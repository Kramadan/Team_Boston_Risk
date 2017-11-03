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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import communicationObjects.LeaveLobby;
import communicationObjects.LoadGame;
import communicationObjects.NewGame;

/**
 * public class CreateLobbyView extends JPanel implements Observer.
 * This JPanel class is on of the ClientGui's views.
 * It is the view displayed when the user clicks the "Create lobby" button on the main view and the response from the server was an accept.
 * This is the most basic view, containing only three buttons, one to start a new game, one to load a game from the database and one to cancel creating the lobby.
 * There is also a text field that allows you to specify the name of the lobby you want to create.
 * @author Oliver Kamperis
 */
public class CreateLobbyView extends JPanel implements Observer {
	private static final long serialVersionUID = 9124530978797149676L;
	private final Client client;
	private final Data data;
	
	private final JTextPane displayPane;
	private final JTextField lobbyNameField;
	private final JButton newGameButton;
	private final JButton loadGameButton;
	private final JButton cancelButton;
	
	/**
	 * Constructs a new CreateLobbyView to interact with a given Client object.
	 * Its initial state has all its JComponents initialized but aren't populated with data.
	 * @param client - The Client to make the Gui view for
	 */
	public CreateLobbyView(Client client) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.client = client;
		this.data = client.getData();
		
		this.displayPane = new JTextPane();
		this.lobbyNameField = new JTextField();
		this.newGameButton = new JButton("New game");
		this.loadGameButton = new JButton("Load game");
		this.cancelButton = new JButton("Cancel");
		
		this.addDisplayPane();
		this.populateAndAddMainPanel();
	}
	
	/**
	 * Updates the display panel at the top of the view based on the data's text field.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		displayPane.setText(data.getText());
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
	 * Populates the main panel of the view with three JButtons and one JTextField and then adds it to the main panel.
	 */
	public void populateAndAddMainPanel() {
		lobbyNameField.setEditable(true);
		lobbyNameField.setPreferredSize(new Dimension(550, 50));
		
		JPanel lobbyNameDisplay = new JPanel() {
			private static final long serialVersionUID = 1471140917167237172L;
			
			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 40));
				g.drawString("Enter lobby name:", 0, 40);
			}
		};
		lobbyNameDisplay.setPreferredSize(new Dimension(550, 50));
		
		JPanel lobbyNamePanel = new JPanel();
		lobbyNamePanel.add(lobbyNameDisplay);
		lobbyNamePanel.add(lobbyNameField);
		
		newGameButton.addActionListener(new NewGameListener());
		newGameButton.setPreferredSize(new Dimension(550, 100));
		JPanel newGamePanel = new JPanel();
		newGamePanel.add(newGameButton);
		
		loadGameButton.addActionListener(new LoadGameListener());
		loadGameButton.setPreferredSize(new Dimension(550, 100));
		JPanel loadGamePanel = new JPanel();
		loadGamePanel.add(loadGameButton);
		
		cancelButton.addActionListener(new CancelListener());
		cancelButton.setPreferredSize(new Dimension(550, 100));
		JPanel cancelPanel = new JPanel();
		cancelPanel.add(cancelButton);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4, 1));
		mainPanel.add(lobbyNamePanel);
		mainPanel.add(newGamePanel);
		mainPanel.add(loadGamePanel);
		mainPanel.add(cancelPanel);
		mainPanel.setPreferredSize(new Dimension(600, 580));
		
		this.add(mainPanel);
	}
	
	/**
	 * public class NewGameListener implements ActionListener.
	 * This ActionListener class sends a NewGame request to the server if the lobby name field is not empty and is alphanumeric.
	 * This ActionListener is assigned only to the "New game" button.
	 */
	public class NewGameListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (lobbyNameField.getText() == null || lobbyNameField.getText().equals("")) {
				data.setText("You have not entered a game name! Please enter one before creating a new game!");
			} else if (!ClientGui.isAlphaNumeric(lobbyNameField.getText())) {
				data.setText("Game name must be alphanumeric!");
			} else {
				client.sendRequest(new NewGame("", lobbyNameField.getText()));
			}
		}
	}
	
	/**
	 * public class LoadGameListener implements ActionListener.
	 * This ActionListener class sends a LoadGame request to the server.
	 * This ActionListener is assigned only to the "Load game" button.
	 */
	public class LoadGameListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			client.sendRequest(new LoadGame(""));
		}
	}
	
	/**
	 * public class CancelListener implements ActionListener.
	 * This ActionListener class sends a LeaveLobby request to the server.
	 * This ActionListener is assigned only to the "Cancel" button.
	 */
	public class CancelListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			client.sendRequest(new LeaveLobby(""));
		}
	}
}
