package communicationObjects;

import java.io.Serializable;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public abstract class Response implements Serializable {
	private static final long serialVersionUID = -1840069425176786012L;
	private final String message;
	
	public Response(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
