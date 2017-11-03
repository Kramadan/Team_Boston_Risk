package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class LoadGame extends Request {
	private static final long serialVersionUID = 3594536173271437506L;
	
	public LoadGame(String message) {
		super(message);
	}
}
