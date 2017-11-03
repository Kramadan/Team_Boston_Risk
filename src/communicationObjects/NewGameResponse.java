package communicationObjects;

public class NewGameResponse extends Response {
	private static final long serialVersionUID = -600905847235708160L;
	private final String gameName;
	
	public NewGameResponse(String message, String gameName) {
		super(message);
		this.gameName = gameName;
	}
	
	public String getGameName() {
		return gameName;
	}
}
