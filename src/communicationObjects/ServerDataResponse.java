package communicationObjects;

import java.util.ArrayList;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class ServerDataResponse extends Response {
	private static final long serialVersionUID = -5420960484732533789L;
	private final ArrayList<Lobby> gameList; // Lobbies that are running a game. (can send and receive actions)
	private final ArrayList<User> playersList;
	private final ArrayList<Lobby> lobbyList; // Waiting to become games (in the queue)
	
	public ServerDataResponse(String message, ArrayList<Lobby> gameList, ArrayList<User> playersList, ArrayList<Lobby> lobbyList) {
		super(message);
		this.gameList = gameList; // indexed from -5 to -1. Arbitrary index, but negative means they are running games
		this.playersList = playersList;
		this.lobbyList = lobbyList;//indexed 0 - 10 , 0 is next in queue, if index changes server must resend the object to client
	}

	public ArrayList<Lobby> getGameList() {
		return gameList;
	}

	public ArrayList<User> getPlayersList() {
		return playersList;
	}

	public ArrayList<Lobby> getLobbyList() {
		return lobbyList;
	}
}
