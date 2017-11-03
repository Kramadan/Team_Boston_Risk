package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class Login extends Request {
	private static final long serialVersionUID = -4488161542536300692L;
	private String userName;
	private String password;
	
	public Login(String message, String userName, String password) {
		super(message);
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
