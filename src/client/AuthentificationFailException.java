package client;

/**
 * An Exception thrown if authentification with the server fails.
 * @author Oliver Kamperis
 */
public class AuthentificationFailException extends Exception {
	private static final long serialVersionUID = 2607581033852476850L;

	public AuthentificationFailException() {
		super();
	}
	
	public AuthentificationFailException(String message) {
		super(message);
	}
	
	public AuthentificationFailException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AuthentificationFailException(Throwable cause) {
		super(cause);
	}
}
