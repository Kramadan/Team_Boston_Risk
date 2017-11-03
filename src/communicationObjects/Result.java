package communicationObjects;

public class Result extends Request {

	private static final long serialVersionUID = -2824850116814903816L;

	private final String username;
	private final boolean result;
	
	public Result(String message, String username, boolean result) {
		super(message);
		this.username = username;
		this.result = result;
	}
	
	public String getUsername() {
		return username;
	}

	public boolean getResult() {
		return result;
	}
}
