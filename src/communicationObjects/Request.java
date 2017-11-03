package communicationObjects;

import java.io.Serializable;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public abstract class Request implements Serializable {
	private static final long serialVersionUID = 8779204053463182354L;
	private final String message;
	
	public Request(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
