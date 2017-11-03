package communicationObjects;

import java.io.Serializable;

/**
 * A class to construct a user of the game
 * @author Matthew Byrne and Oliver Kamperis and Mark Alston
 */
public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = -7867513928248033140L;
	private int userId;
	private String username;
	private String password;
	private boolean user;
	private String message;
	private int numberOfGamesPlayed;
	private int numberOfLosses;
	private int numberOfWins;
	private int rating;
	
	/**
	 * Constructor used between the server and database.
	 * @param userId
	 * @param username
	 * @param password
	 * @param user
	 * @param message
	 * @param numberOfGamesPlayed
	 * @param numberOfLosses
	 * @param numberOfWins
	 * @param rating
	 */
	public User(int userId, String username, String password,  boolean user, String message, int numberOfGamesPlayed, int numberOfLosses, int numberOfWins){
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.user = user;
		this.message = message;
		this.numberOfLosses = numberOfLosses;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.numberOfWins = numberOfWins;	
		setRating(numberOfWins, numberOfGamesPlayed);	
		
	}
	
	/**
	 * Constructor to be sent to the client
	 * @param userId
	 * @param username
	 * @param numberOfGamesPlayed
	 * @param numberOfLosses
	 * @param numberOfWins
	 * @param rating
	 */
	public User(int userId, String username, int numberOfGamesPlayed, int numberOfLosses, int numberOfWins) {
		super();
		this.userId = userId;
		this.username = username;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.numberOfLosses = numberOfLosses;
		this.numberOfWins = numberOfWins;	
		setRating(numberOfWins, numberOfGamesPlayed);
	}
	@Override
	public int compareTo(User arg0) {
		return this.userId - arg0.userId;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof User) {
			User user = (User)arg0;
			return this.username.equals(user.username);
		} else if (arg0 instanceof String) {
			String username = (String)arg0;
			return this.username.equals(username);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return userId + " Name: " + username + " Rating: " + rating;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int numberOfWins, int numberOfGamesPlayed) {
		
		if(numberOfGamesPlayed == 0){
			this.rating = 0;
		}
		
		else{
			this.rating = (int) (numberOfWins*5/numberOfGamesPlayed);
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public int getNumberOfGamesPlayed() {
		return numberOfGamesPlayed;
	}

	public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
		this.numberOfGamesPlayed = numberOfGamesPlayed;
	}

	public int getNumberOfLosses() {
		return numberOfLosses;
	}

	public void setNumberOfLosses(int numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}

	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}
}


