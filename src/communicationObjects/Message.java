package communicationObjects;

import java.io.Serializable;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public abstract class Message implements Serializable {
	private static final long serialVersionUID = 2819804637585972316L;
	private final String message;
	
	public Message(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
