package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class DenyLogin extends LoginResponse {
	private static final long serialVersionUID = 262789391782540923L;

	public DenyLogin(String message) {
		super(message);
	}
}
