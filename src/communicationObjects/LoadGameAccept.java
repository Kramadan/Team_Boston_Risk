package communicationObjects;

/**
 * @author Oliver Kamperis
 */
public class LoadGameAccept extends LoadGameResponse {
	private static final long serialVersionUID = 7011282537716325797L;
	private final Lobby lobby;
	
	public LoadGameAccept(String message, Lobby lobby) {
		super(message);
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
}
