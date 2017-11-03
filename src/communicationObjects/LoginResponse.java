package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public abstract class LoginResponse extends Response {
	private static final long serialVersionUID = -251527400425334500L;

	public LoginResponse(String message) {
		super(message);
	}
}

