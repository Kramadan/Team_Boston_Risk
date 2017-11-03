package communicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class Lobby implements Serializable, Comparable<Lobby> {
	private static final long serialVersionUID = -7326203001472228653L;
	private final int index;
	private String name;
	private final User host;
	private User guest;
	private int numPlayers;
	
	private String desiredGameType;
	public static final String NEWGAME = "NewGame";
	public static final String LOADGAME = "LoadGame";
	private ArrayList<String> gameData;
	
	public Lobby(int index, String name, User host, User guest) {
		super();
		this.index = index;
		this.name = name;
		this.host = host;
		this.guest = guest;
		this.desiredGameType = Lobby.NEWGAME;
		this.gameData = null;
		
		if (guest != null) {
			this.numPlayers = 2;
		} else {
			this.numPlayers = 1;
		}
	}
	
	@Override
	public int compareTo(Lobby arg0) {
		return this.index - arg0.index;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Lobby) {
			Lobby lobby = (Lobby)arg0;
			return this.name.equals(lobby.name);
		} else if (arg0 instanceof String) {
			String lobbyName = (String)arg0;
			return this.name.equals(lobbyName);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		if (guest == null) {
			return index + " Name: " + name + " Host: " + host.getUsername();
		} else {
			return index + " Name: " + name + " Host: " + host.getUsername() + " Guest: " + guest.getUsername();
		}
	}
	
	public int getIndex() {
		return index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public User getHost() {
		return host;
	}

	public User getGuest() {
		return guest;
	}

	public void setGuest(User guest) {
		this.guest = guest;
		if (guest != null) {
			this.numPlayers = 2;
		} else {
			this.numPlayers = 1;
		}
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public String getDesiredGameType() {
		return desiredGameType;
	}

	public ArrayList<String> getGameData() {
		return gameData;
	}

	public void setGameData(ArrayList<String> gameData) {
		this.gameData = gameData;
		this.desiredGameType = Lobby.LOADGAME;
	}
}
