package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class AcceptLogin extends LoginResponse {
	private static final long serialVersionUID = -2785380680027777280L;
	private final User user;
	
	public AcceptLogin(String message, User user) {
		super(message);
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
}
