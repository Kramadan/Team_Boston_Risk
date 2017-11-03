package communicationObjects;
/**
 * @author Mark Alston
 */
public class CreateLobbyAccept extends CreateLobbyResponse {
	private static final long serialVersionUID = 7267924788754608371L;
	private final Lobby lobby;
	
	public CreateLobbyAccept(String message, Lobby lobby) {
		super(message);
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
}
