package communicationObjects;

import java.io.Serializable;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public abstract class Action implements Serializable {
	private static final long serialVersionUID = 4535121135289613446L;
	private final String message;
	
	public Action(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
