package client;

import java.util.ArrayList;
import java.util.Observable;

import communicationObjects.Lobby;
import communicationObjects.ServerDataResponse;
import communicationObjects.User;
import riskGame.Game;

/**
 * public class Data extends Observable.
 * Contains data that is used by the various views of the ClientGui in order to populate the Gui with relevant information for the user.
 * Also controls the view state of the Gui based on the desiredViewState field.
 * @author Oliver Kamperis
 */
public class Data extends Observable {
	private String text;
	private String desiredViewState;
	private User user;
	private ArrayList<Lobby> games;
	private ArrayList<Lobby> lobbies;
	private ArrayList<User> players;
	private ArrayList<String> globalChat;
	private ArrayList<String> lobbyChat;
	private ArrayList<String> directChat;
	private Lobby myLobby;
	private Game game;
	
	/**
	 * Constructs a new Data object in a default initial state.
	 * The desiredViewState of the Gui is set to the LoginRegister view.
	 * The text, games, lobbies, players, globalChat and gameChat fields are all "blank" (empty) but are not null.
	 * The user, myLobby and game fields are set to null.
	 * This Data object is Observable and when any field variable to set or changed in any way, using any of this class' setter methods, all observers of this object are automatically updated and notified of the change.
	 */
	public Data() {
		this.text = "";
		this.desiredViewState = ClientGui.LOGINREGISTERVIEW;
		this.user = null;
		this.games = new ArrayList<>();
		this.lobbies = new ArrayList<>();
		this.players = new ArrayList<>();
		this.globalChat = new ArrayList<>();
		this.lobbyChat = new ArrayList<>();
		this.directChat = new ArrayList<>();
		this.myLobby = null;
		this.game = null;
	}
	
	public void clearAll() {
		this.user = null;
		this.games = new ArrayList<>();
		this.lobbies = new ArrayList<>();
		this.players = new ArrayList<>();
		this.globalChat = new ArrayList<>();
		this.lobbyChat = new ArrayList<>();
		this.directChat = new ArrayList<>();
		this.myLobby = null;
		this.game = null;
		notifyGUIs();
	}
	
	/**
	 * Calls the setChanged() and notifyObservers() methods.
	 * This calls the update() methods of all observing objects of this object.
	 */
	public void notifyGUIs() {
		setChanged();
		notifyObservers();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		notifyGUIs();
	}
	
	public String getDesiredViewState() {
		return desiredViewState;
	}

	public void setDesiredViewState(String desiredViewState) {
		this.desiredViewState = desiredViewState;
		notifyGUIs();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
		notifyGUIs();
	}
	
	/**
	 * Sets the games, lobbies and players based on the specified ServerDataResponse.
	 * Also updates the player's lobby if they are in one.
	 * If the player is in a "ghost lobby" i.e. a lobby that no longer exists on the server for whatever reason the client's leaveLobby() method is invoked.
	 * @param client - The client invoking the method
	 * @param serverData - The ServerDataResponse to be used to update the games, lobbies and players
	 */
	public void setServerData(Client client, ServerDataResponse serverData) {
		this.players = serverData.getPlayersList();
		this.lobbies = serverData.getLobbyList();
		this.games = serverData.getGameList();
		boolean test = false;
		for (Lobby lobby : lobbies) {
			if (lobby.equals(myLobby)) {
				myLobby = lobby;
				test = true;
				break;
			}
		}
		for (Lobby game : games) {
			if (game.equals(myLobby)) {
				myLobby = game;
				test = true;
				break;
			}
		}
		if (!test && (client.getClientGui().getCurrentViewState().equals(ClientGui.LOBBYVIEW) || client.getClientGui().getCurrentViewState().equals(ClientGui.RISKGUI))) {
			client.leaveLobby();
		}
		notifyGUIs();
	}

	public ArrayList<Lobby> getGames() {
		return games;
	}

	public void setGames(ArrayList<Lobby> games) {
		this.games = games;
		notifyGUIs();
	}

	public ArrayList<Lobby> getLobbies() {
		return lobbies;
	}

	public void setLobbies(ArrayList<Lobby> lobbies) {
		this.lobbies = lobbies;
		notifyGUIs();
	}

	public ArrayList<User> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<User> players) {
		this.players = players;
		notifyGUIs();
	}
	
	public ArrayList<String> getGlobalChat() {
		return globalChat;
	}
	
	/**
	 * Adds a String to the globalChat.
	 * @param globalChatMessage - The String to be added to the globalChat
	 */
	public void addToGlobalChat(String globalChatMessage) {
		if (this.globalChat.size() == 10) {
			this.globalChat.remove(0);
		}
		this.globalChat.add(globalChatMessage);
		notifyGUIs();
	}
	
	public ArrayList<String> getLobbyChat() {
		return lobbyChat;
	}
	
	/**
	 * Adds a String to the lobbyChat.
	 * @param lobbyChatMessage - The String to be added to the lobbyChat
	 */
	public void addToLobbyChat(String lobbyChatMessage) {
		if (this.lobbyChat.size() == 10) {
			this.lobbyChat.remove(0);
		}
		this.lobbyChat.add(lobbyChatMessage);
		notifyGUIs();
	}
	
	public ArrayList<String> getDirectChat() {
		return directChat;
	}
	
	/**
	 * Adds a String to the directChat.
	 * @param directChatMessage - The String to be added to the directChat
	 */
	public void addToDirectChat(String gameChatMessage) {
		if (this.directChat.size() == 10) {
			this.directChat.remove(0);
		}
		this.directChat.add(gameChatMessage);
		notifyGUIs();
	}

	public Lobby getMyLobby() {
		return myLobby;
	}

	public void setMyLobby(Lobby myLobby) {
		this.myLobby = myLobby;
		notifyGUIs();
	}
	
	public Game getGame() {
		return game;
	}

	/**
	 * Starts the Risk game.
	 * Either creates a new game or loads a game stored in the myLobby field.
	 * If the game type cannot be determined the client's crash(String stackTrace) method is invoked.
	 * @param client - The client invoking the method
	 */
	public void startGame(Client client) {
		if (myLobby.getDesiredGameType().equals(Lobby.NEWGAME)) {
			if (myLobby.getHost().equals(user)) {
				this.game = new Game(myLobby.getHost().getUsername(), myLobby.getGuest().getUsername(), true, this, client);
			} else {
				this.game = new Game(myLobby.getGuest().getUsername(), myLobby.getHost().getUsername(), false, this, client);
			}
		} else if (myLobby.getDesiredGameType().equals(Lobby.LOADGAME)) {
			this.game = client.loadGame();
		} else {
			client.crash("When attempting to start a game the desired game type could not be determined.");
		}
	}
	
	/**
	 * Sets the game field to be null.
	 */
	public void stopGame() {
		this.game = null;
	}
}
