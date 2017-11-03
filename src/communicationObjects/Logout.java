package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class Logout extends Request {
	private static final long serialVersionUID = 8370373596171357338L;
	
	public Logout(String message) {
		super(message);
	}
}
