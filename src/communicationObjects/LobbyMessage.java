package communicationObjects;

public class LobbyMessage extends Message {
	private static final long serialVersionUID = -2985372428571911483L;
	private final String lobbyToMessage;
	
	public LobbyMessage(String message, String lobbyToMessage) {
		super(message);
		this.lobbyToMessage = lobbyToMessage;
	}
	
	public String getLobbyToMessage() {
		return lobbyToMessage;
	}
}
