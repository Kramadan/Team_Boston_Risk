package database;

import java.util.ArrayList;

import communicationObjects.User;

public interface DatabaseManagerInterface {

	/**
	 * A method to check the login details of a user
	 */
	public User checkLogin(String username, String password);
	
	/**
	 * A method to check the registration details of a user.
	 */
	public User register(String username, String password);
	
	/**
	 * A method to insert a saved game into a database
	 * @param game
	 */
	public void saveGameDatabase(ArrayList<String> game);
	
	/**
	 * A method to load a game from the database
	 */
	public ArrayList<String> loadGameFromDatabase(String hostName);
	
	/**
	 * A method to update user stats.
	 * @param winnerUsername
	 * @param loserUsername
	 */
	public void updateStats(String winnerUsername, String loserUsername);
}
