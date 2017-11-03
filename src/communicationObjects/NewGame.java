package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class NewGame extends Request {
	private static final long serialVersionUID = 75541798975070492L;
	private final String gameName;
	
	public NewGame(String message, String gameName) {
		super(message);
		this.gameName = gameName;
	}
	
	public String getGameName() {
		return gameName;
	}
}
