package client;

/**
 * An Exception thrown if the PongingThread stops receiving pings from the server.
 * @author Oliver Kamperis
 */
public class StoppedPingingException extends Exception {
	private static final long serialVersionUID = 3177409035233036396L;

	public StoppedPingingException() {
		super();
	}
	
	public StoppedPingingException(String message) {
		super(message);
	}
	
	public StoppedPingingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public StoppedPingingException(Throwable cause) {
		super(cause);
	}
}
