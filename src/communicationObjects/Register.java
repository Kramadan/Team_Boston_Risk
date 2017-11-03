package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class Register extends Request {
	private static final long serialVersionUID = 538287985616147611L;
	private String userName;
	private String password;
	
	public Register(String message, String userName, String password) {
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
