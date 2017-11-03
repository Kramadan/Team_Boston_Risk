package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class JoinLobby extends Request {
	private static final long serialVersionUID = -5996319320282477045L;
	private final Lobby lobbyToJoin;

	public JoinLobby(String message, Lobby lobby) {
		super(message);
		this.lobbyToJoin = lobby;
	}
	
	public Lobby getLobbyToJoin() {
		return lobbyToJoin;
	}
}
