package communicationObjects;

public class JoinLobbyAccept extends JoinLobbyResponse {
	private static final long serialVersionUID = -1588259362148690066L;
	private final Lobby lobby;
	
	public JoinLobbyAccept(String message, Lobby lobby) {
		super(message);
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
}
