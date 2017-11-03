package communicationObjects;

import java.util.ArrayList;

/**
 * @author Oliver Kamperis and Mark Alston
 */
public class SaveGame extends Request {
	private static final long serialVersionUID = 5174293011955596474L;
	private final ArrayList<String> gameData;
	
	public SaveGame(String message, ArrayList<String> gameData) {
		super(message);
		this.gameData = gameData;
	}
	
	public ArrayList<String> getGameData() {
		return gameData;
	}
}
