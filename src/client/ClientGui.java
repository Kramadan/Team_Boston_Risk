package client;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import gui.StartGUI;

/**
 * public class ClientGui extends JFrame implements Observer.
 * The main frame upon which the Client's views are displayed.
 * Contains the current view state and its update method updates the view state based upon the Client's Data object's desiredViewState field.
 * @author Oliver Kamperis
 */
public class ClientGui extends JFrame implements Observer {
	private static final long serialVersionUID = 6945386908303590271L;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 630;
	private final Client client;
	private final Data data;
	private final LoginRegisterView loginRegisterView;
	private final MainView mainView;
	private final CreateLobbyView createLobbyView;
	private final LobbyView lobbyView;
	private StartGUI gameGui;
	
	private String currentViewState;
	public static final String LOGINREGISTERVIEW = "LoginRegisterView";
	public static final String MAINVIEW = "MainView";
	public static final String CREATELOBBYVIEW = "CreateLobbyView";
	public static final String LOBBYVIEW = "LobbyView";
	public static final String RISKGUI = "RiskGUI";
	
	/**
	 * Constructs a new ClientGui to interact with a given Client object.
	 * Its initial view state is the LoginRegisterView.
	 * @param client - The Client to make the Gui for
	 */
	public ClientGui(Client client) {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Risk client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.client = client;
		this.data = client.getData();
		
		this.loginRegisterView = new LoginRegisterView(client);
		this.mainView = new MainView(client);
		this.createLobbyView = new CreateLobbyView(client);
		this.lobbyView = new LobbyView(client);
		
		data.addObserver(this);
		data.addObserver(loginRegisterView);
		
		this.currentViewState = ClientGui.LOGINREGISTERVIEW;
		this.add(loginRegisterView);
	}
	
	/**
	 * Updates the view of the ClientGui based on the desiredViewState field contained in the Client's Data object and the ClientGui's currentViewState.
	 * If the currentViewState of the ClientGui is not equal to the desiredViewState then the view is changed to the desiredViewState.
	 * The observers of this class are automatically changed as the views change, such that only the current view is observing the client's data class.
	 * Else if it is the same then this method does nothing.
	 * If the Client has started a game then the ClientGui will be set non-visible.
	 * If the ClientGui cannot determine what the desiredViewSate is then the Client's crash(String stackTrace) method will be invoked.
	 * @see crash(String stackTrace) in Client
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (data.getDesiredViewState().equals(ClientGui.LOGINREGISTERVIEW)) {
			if (!currentViewState.equals(ClientGui.LOGINREGISTERVIEW)) {
				getContentPane().removeAll();
				getContentPane().add(loginRegisterView);
				repaint();
				printAll(getGraphics());
				setVisible(true);
				currentViewState = ClientGui.LOGINREGISTERVIEW;
				data.deleteObservers();
				data.addObserver(this);
				data.addObserver(loginRegisterView);
				data.notifyGUIs();
			}
		} else if (data.getDesiredViewState().equals(ClientGui.MAINVIEW)) {
			if (!currentViewState.equals(ClientGui.MAINVIEW)) {
				getContentPane().removeAll();
				getContentPane().add(mainView);
				repaint();
				printAll(getGraphics());
				setVisible(true);
				currentViewState = ClientGui.MAINVIEW;
				data.deleteObservers();
				data.addObserver(this);
				data.addObserver(mainView);
				data.notifyGUIs();
			}
		} else if (data.getDesiredViewState().equals(ClientGui.CREATELOBBYVIEW)) {
			if (!currentViewState.equals(ClientGui.CREATELOBBYVIEW)) {
				getContentPane().removeAll();
				getContentPane().add(createLobbyView);
				repaint();
				printAll(getGraphics());
				setVisible(true);
				currentViewState = ClientGui.CREATELOBBYVIEW;
				data.deleteObservers();
				data.addObserver(this);
				data.addObserver(createLobbyView);
				data.notifyGUIs();
			}
		} else if (data.getDesiredViewState().equals(ClientGui.LOBBYVIEW)) {
			if (!currentViewState.equals(ClientGui.LOBBYVIEW)) {
				getContentPane().removeAll();
				getContentPane().add(lobbyView);
				repaint();
				printAll(getGraphics());
				setVisible(true);
				currentViewState = ClientGui.LOBBYVIEW;
				data.deleteObservers();
				data.addObserver(this);
				data.addObserver(lobbyView);
				data.notifyGUIs();
				if (!data.getDirectChat().contains("//playerName//yourMessage")) {
					data.addToDirectChat("To send direct messages type like this:");
					data.addToDirectChat("//playerName//yourMessage");
				}
			}
		} else if (data.getDesiredViewState().equals(ClientGui.RISKGUI)) {
			if (!currentViewState.equals(ClientGui.RISKGUI)) {
				if (data.getGame() != null && gameGui != null) {
					setVisible(false);
					gameGui.setVisible(true);
					currentViewState = ClientGui.RISKGUI;
				} else {
					client.crash("The client GUI attempted to start the game GUI when the game was null or the game GUI was null.");
				}
				data.deleteObservers();
				data.addObserver(this);
				data.addObserver(gameGui);
				data.notifyGUIs();
			}
		} else {
			client.crash("The client cannot determine the desired view state.");
		}
	}
	
	public String getCurrentViewState() {
		return currentViewState;
	}
	
	public LoginRegisterView getLoginRegisterView() {
		return loginRegisterView;
	}
	
	public MainView getMainView() {
		return mainView;
	}
	
	public CreateLobbyView getCreateLobbyView() {
		return createLobbyView;
	}
	
	public LobbyView getLobbyView() {
		return lobbyView;
	}
	
	/**
	 * Sets the gameGui to null.
	 */
	public void stopGameGui() {
		gameGui.setVisible(false);
		this.gameGui = null;
	}
	
	/**
	 * Sets the gameGui to a new StartGUI(Client client).
	 */
	public void startGameGui() {
		this.gameGui = new StartGUI(client);
	}
	
	/**
	 * Checks to see whether a String contains only alphanumeric characters.
	 * @param word - the String to be checked
	 * @return boolean - true of word is alphanumeric, false if not
	 */
	public static boolean isAlphaNumeric(String word) {
		for (int i = 0; i < word.length(); i++) {
			switch (word.charAt(i)) {
				case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm':
				case 'n': case 'o': case 'p': case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
				case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M':
				case 'N': case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z':
				case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '0':
					break;
				default:
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks to see whether a String contains only alphanumeric characters and spaces.
	 * @param word - the String to be checked
	 * @return boolean - true of word is alphanumeric with spaces, false if not
	 */
	public static boolean isAlphaNumericWithSpaces(String word) {
		for (int i = 0; i < word.length(); i++) {
			switch (word.charAt(i)) {
				case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm':
				case 'n': case 'o': case 'p': case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
				case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M':
				case 'N': case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z':
				case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '0': case ' ':
					break;
				default:
					return false;
			}
		}
		return true;
	}
}
