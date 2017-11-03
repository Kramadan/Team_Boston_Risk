package communicationObjects;

public class DirectMessage extends Message {
	private static final long serialVersionUID = -8264599967888000816L;
	private final String userToMessage;
	
	public DirectMessage(String message, String userToMessage) {
		super(message);
		this.userToMessage = userToMessage;
	}
	
	public String getUserToMessage() {
		return userToMessage;
	}
}
