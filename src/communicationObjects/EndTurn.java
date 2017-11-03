package communicationObjects;

public class EndTurn extends Action {

	private static final long serialVersionUID = 5132032553250342777L;

	public EndTurn(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "EndTurn: A turn has ended";
	}

}
