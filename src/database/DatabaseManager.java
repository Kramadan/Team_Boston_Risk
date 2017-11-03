
package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import communicationObjects.User;


/**
 * A class to manage the database which implements the DatabaseManager Interface
 * @author MatthewByrne
 */
public class DatabaseManager implements DatabaseManagerInterface {

	private static Properties databaseProperties = new Properties (); //Create new Properties object
	private Connection conn = null; //Initialize connection
	private PreparedStatement selectUserStatement = null;
	PreparedStatement selectGameStmt = null;
	private Statement statement = null;
	private ResultSet resultSet = null; //Initialize result set
	private User user = null;

	/**
	 * A method to connect to the database.
	 */
	private void databaseConnection(){

		try {
			FileInputStream stream = new FileInputStream("DB.properties"); 
			//read in the database properties file
			databaseProperties.load(stream);		

		} catch (FileNotFoundException e) {

			System.out.println("DB properties not found, please create a  DB properties file");
			System.exit(1);
			//e.printStackTrace();

		} catch (IOException e) {
			System.out.println("Could not read DB properties file");
			System.exit(1);
			//e.printStackTrace();
		}

		try {
			String driver = databaseProperties.getProperty("postgresql.driver");
			String password = databaseProperties.getProperty("postgresql.password");
			String username = databaseProperties.getProperty("postgresql.username");
			String url = databaseProperties.getProperty("postgresql.url");

			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);				

		}	
		catch (ClassNotFoundException ex) {	
			System.out.println("Driver not found");

		}
		catch (SQLException ex){
			//ex.printStackTrace();
		}

		if (conn != null){
			System.out.println("Database accessed");
		}

		else {
			System.out.println("Failed to make connection to the Database");
		}

	}	

	/**
	 * A method to close the connection to the database.
	 */
	private void closeConnection(){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * A method to check login details for a user.
	 * @param username which is the username entered in by the user.
	 * @param password which is a String entered by the user.
	 * @return a user object with contains information on whether the login has been successful or not.
	 */
	public User checkLogin(String username, String password){

		String chooseUserName = "SELECT * FROM USERS WHERE username = ?;";

		try{

			this.databaseConnection();

			// check to see if user has already been entered into the database
			if(!(conn == null || conn.isClosed())){

				conn.setAutoCommit(false);

				selectUserStatement = conn.prepareStatement(chooseUserName);//set prepared statement to select user statement
				selectUserStatement.setString(1, username); //Set the first placeholder in the preparedStatement to the username entered.
				resultSet = selectUserStatement.executeQuery(); //execute a query to return a resultSet

				//If resultSet empty return user object with message telling the user to register	
				if (!(resultSet.next())){

					System.out.println("No matches found for the username entered, please register to continue");					

					user = new User(0, username, password, false, "No matches found for the username entered, please register to continue", 0, 0 , 0);

				}
				//resultSet.getString(3).equals(password)
				else if(!(BCrypt.checkpw(password, resultSet.getString(3)))){ //password hashed
					System.out.println("Incorrect password entered, please try again"); //Password does not match the one stored.	
					System.out.println(resultSet.getInt("id"));
					user = new User(resultSet.getInt("id"), resultSet.getString("username"), password, false, "Incorrect password entered, please try again", 0, 0, 0);
				}

				else{
					System.out.println("Password accepted, login successful"); //login successful

					user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), true, "Password accepted, login successful", 
							resultSet.getInt("games_played"), resultSet.getInt("losses"), resultSet.getInt("wins"));
					System.out.println(user.getRating());
				}
			}
			conn.setAutoCommit(true); //commit sql statements

			this.closeConnection(); //close connection

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("SQLException when logging in");
		}
		return user;

	}
	/**
	 * A  method used to check a users registration details are correct and if so insert them into the database.
	 * @param username the username entered.
	 * @param password the password entered.
	 * @return a user object sent to the server indicating whether the registration has been successful or not
	 */
	public User register(String username, String password){

		PreparedStatement insertUserStatement = null;

		String chooseUserName = "SELECT * FROM USERS WHERE username = ?;"; //
		String insertUser = "INSERT INTO USERS (username, password, games_played, losses, wins, rating) VALUES (?, ?, ?, ?, ?, ?);";
		String regex = "^[a-zA-Z0-9]*$"; //username or password  must be in alphanumeric format

		try{

			this.databaseConnection();

			// check to see if user has already been entered into the database
			if(!(conn == null || conn.isClosed())){

				conn.setAutoCommit(false);

				selectUserStatement = conn.prepareStatement(chooseUserName);//set prepared statement to select user statement
				selectUserStatement.setString(1, username); //Set the first placeholder in the preparedStatement 
				resultSet = selectUserStatement.executeQuery();  //execute the select query

				if(resultSet.next()){

					System.out.println("Username already registered");			
					user = new User (resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), true, "Username already registered", 
							resultSet.getInt("games_played"), resultSet.getInt("losses"), resultSet.getInt("wins"));
				}

				else {
					if ((!username.matches(regex) || !password.matches(regex))){ 

						System.out.println("Username or password do not match the required format, please enter a username and password using only alphanumeric characters");			
						user = new User(0, username, password, false, "Username or password does not match the required format, please enter a username and password using only alphanumeric characters",
								0, 0, 0);
					}

					else {	

						String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());

						insertUserStatement = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);					 
						insertUserStatement.setString(1, username); //set the 1st placeholder to the username entered
						insertUserStatement.setString(2, pw_hash);//Set the second placeholder to the password entered
						insertUserStatement.setInt(3, 0);//Set the 3rd placeholder(games_played) to 0
						insertUserStatement.setInt(4, 0); //Set the 4th placeholder (losses) to 0
						insertUserStatement.setInt(5, 0);//Set the 5th placeholder (wins) to 0
						insertUserStatement.setInt(6, 0); //Set the 6th placeholder (rating) to 0
						insertUserStatement.executeUpdate(); //Execute update to the database

						conn.commit(); //Commit these changes						

						System.out.println("User registered successfully");								

						ResultSet generatedKeys = insertUserStatement.getGeneratedKeys();

						generatedKeys.next();

						int key = generatedKeys.getInt("id");

						user = new User(key, username, pw_hash, true, "User registered successfully", 0, 0, 0);

					}
				}
				conn.setAutoCommit(true);
			}
			this.closeConnection();
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("SQLException when registering");
		}

		return user;

	}

	// call the super.finalize after connection closed
	public void finalize(){
		this.closeConnection();
		try {
			super.finalize();  
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * A method to create the tables for the database.
	 */
	private void createTables(){

		try{
			this.databaseConnection();

			statement = conn.createStatement();

			String createUserTable ="CREATE TABLE USERS (" +
					"id SERIAL PRIMARY KEY," +
					"username  TEXT NOT NULL," +
					"password TEXT NOT NULL," +
					"games_played INT, " +
					"losses INT,  " +
					"wins INT, " +
					"rating INT)";

			String createGameTable = "CREATE TABLE GAMES ("
					+ "id SERIAL PRIMARY KEY,"
					+ "name TEXT NOT NULL,"
					+ "host_id INT references USERS(id),"
					+ "guest_id INT references USERS(id),"
					+ "current_players_turn_id INT references USERS(id),"
					+ "time_saved TIMESTAMP)";

			String createParticipantTable = "CREATE TABLE PARTICIPANTS (" +
					"id SERIAL PRIMARY KEY," +
					"user_id INT references users(id)," +
					"game_id INT references games(id))";

			String createCountryTable = "CREATE TABLE COUNTRIES (" +
					"id SERIAL PRIMARY KEY," +
					"name TEXT,"
					+"owned_by_participant INT references participants(id),"
					+"game_id INT references games(id),"
					+"number_of_troops INT )";


			statement.addBatch(createUserTable); 
			statement.addBatch(createGameTable);
			statement.addBatch(createParticipantTable);	
			statement.addBatch(createCountryTable);
			statement.executeBatch();

			System.out.println("Tables created");

			this.closeConnection();

		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * A method to reset the sequences automatically generated for the id column of each table
	 * when a record is inserted.
	 */
	private void resetSequences(){

		String alterGameSequence = "ALTER SEQUENCE games_id_seq RESTART with 1;";
		String alterCountrySequence = "ALTER SEQUENCE countries_id_seq RESTART with 1;";
		String alterParticipantSequence = "ALTER SEQUENCE participants_id_seq RESTART with 1;";	
		//String alterUserSequence = "ALTER SEQUENCE users_id_seq RESTART with 1;";
		try {	
			this.databaseConnection();
			statement =conn.createStatement();  //reset primary keys
			statement.addBatch(alterCountrySequence);
			statement.addBatch(alterGameSequence);
			statement.addBatch(alterParticipantSequence);
			//statement.addBatch(alterUserSequence);
			statement.executeBatch();

			this.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * A method to save a game into the database. If this is the first time the game has been saved a new game will be inserted.
	 * Otherwise the previous saved game will be updated by a series of update statements.
	 * @param game an arrayList containing all required info about the game.
	 */
	public void saveGameDatabase(ArrayList<String> game){

		PreparedStatement insertGameStmt= null;
		PreparedStatement selectParticipantStatement = null;
		PreparedStatement insertParticipantStatement = null;
		PreparedStatement countryStmt = null;
		PreparedStatement insertCountryStmt = null;
		PreparedStatement updateCountryStmt = null;
		PreparedStatement updateGameStmt = null;	
		PreparedStatement updateParticipantStatement = null;
		String gameName = null;
		String hostName = null;
		String hostTurn = null;
		String guestName = null;
		String countriesOwnedHostWithTroops = null;
		String [] splitHostCountriesWithTroops = null;
		String countriesOwnedGuestWithTroops = null;
		String [] splitGuestCountriesWithTroops = null;
		gameName = game.get(0); //get game Name from the arrayList
		hostName = game.get(1); //get host name from the arrayList	
		hostTurn = game.get(2); //get string indicating whether it's the host turn from array List			
		countriesOwnedHostWithTroops = game.get(3); //get countries owned by the host with the number of troops on them
		splitHostCountriesWithTroops = countriesOwnedHostWithTroops.split("\\s*,\\s*"); //split each value by a comma
		guestName = game.get(4);
		countriesOwnedGuestWithTroops = game.get(6);
		splitGuestCountriesWithTroops = countriesOwnedGuestWithTroops.split("\\s*,\\s*");		

		String selectGame = "SELECT * FROM GAMES WHERE  host_id = ?";
		String selectCountries = "SELECT * FROM COUNTRIES WHERE  game_id = ? AND name = ?;";
		String selectUser = "SELECT * FROM USERS WHERE username = ?";
		String selectParticipant = "SELECT * FROM PARTICIPANTS WHERE user_id = ? AND game_id = ? ";
		String insertGame = "INSERT INTO GAMES "
				+ "	(name,  host_id, guest_id, current_players_turn_id, time_saved) "
				+ "VALUES (?,  (SELECT id FROM USERS WHERE username = ? ), (SELECT id FROM USERS WHERE username = ?), "
				+ "(SELECT id FROM USERS WHERE username = ?), CURRENT_TIMESTAMP);"; 
		String insertParticipant = "INSERT INTO PARTICIPANTS (user_id, game_id) " + 
				"VALUES ((SELECT id FROM USERS WHERE username = ?), (SELECT id FROM GAMES WHERE name = ?));";
		String insertCountries = "INSERT INTO COUNTRIES (name, owned_by_participant, game_id, number_of_troops) "
				+ "VALUES (?, (SELECT id FROM PARTICIPANTS WHERE game_id = ? AND id = ?), (SELECT id FROM GAMES WHERE name = ?), ?);";
		String updateGame = "UPDATE GAMES SET name = ?,	guest_id = ?, current_players_turn_id = ?, time_saved = CURRENT_TIMESTAMP WHERE id =?;";
		String updateCountries = "UPDATE COUNTRIES SET owned_by_participant = ?, number_of_troops = ? WHERE name = ? AND game_id = ?;";
		String updateGuestParticipant = "UPDATE PARTICIPANTS SET user_id = ? WHERE id = ? AND game_id = ?";;


		try {

			this.databaseConnection();

			if(!(conn == null || conn.isClosed())){

				conn.setAutoCommit(false);	

				selectUserStatement = conn.prepareStatement(selectUser);
				selectUserStatement.setString(1, hostName);
				resultSet = selectUserStatement.executeQuery();
				resultSet.next();
				int hostId = resultSet.getInt("id");

				selectGameStmt = conn.prepareStatement(selectGame); //select game 
				selectGameStmt.setInt(1, hostId);
				resultSet = selectGameStmt.executeQuery();

				if(resultSet.next()){	 //if result found	

					int gameId = resultSet.getInt("id"); //get the game id
					int previousGuestId = resultSet.getInt("guest_id");

					//find currentplayersturn id
					selectUserStatement = conn.prepareStatement(selectUser);				
					if(hostTurn.equals("true")){
						selectUserStatement.setString(1, hostName);  
					}
					else{
						selectUserStatement.setString(1, guestName);
					}
					resultSet = selectUserStatement.executeQuery();
					resultSet.next();
					int currentPlayersTurnId = resultSet.getInt("id");

					// find guestid
					selectUserStatement = conn.prepareStatement(selectUser);
					selectUserStatement.setString(1, guestName);
					resultSet = selectUserStatement.executeQuery();
					resultSet.next();
					int guestId = resultSet.getInt("id");

					if(guestId!= previousGuestId){

						//select previous guests participant id
						selectParticipantStatement = conn.prepareStatement(selectParticipant);
						selectParticipantStatement.setInt(1,previousGuestId);
						selectParticipantStatement.setInt(2, gameId);
						resultSet = selectParticipantStatement.executeQuery();
						resultSet.next();
						int previousGuestParticipantId = resultSet.getInt("id");

						//update new guest as participant
						updateParticipantStatement = conn.prepareStatement(updateGuestParticipant);
						updateParticipantStatement.setInt(1, guestId);
						updateParticipantStatement.setInt(2, previousGuestParticipantId);
						updateParticipantStatement.setInt(3, gameId);
						updateParticipantStatement.executeUpdate();
					}

					//find if the guest is entered into the participants table for this game
					selectParticipantStatement = conn.prepareStatement(selectParticipant);
					selectParticipantStatement.setInt(1, guestId);
					selectParticipantStatement.setInt(2, gameId);
					resultSet = selectParticipantStatement.executeQuery();	
					resultSet.next();
					int guestParticipantId = resultSet.getInt("id");

					//update game with correct guest, players turn and gameId
					updateGameStmt = conn.prepareStatement(updateGame);
					updateGameStmt.setString(1, gameName);
					updateGameStmt.setInt(2, guestId);
					updateGameStmt.setInt(3, currentPlayersTurnId);
					updateGameStmt.setInt(4, gameId);
					updateGameStmt.executeUpdate();

					//get host participant id
					selectParticipantStatement = conn.prepareStatement(selectParticipant);
					selectParticipantStatement.setInt(1, hostId);
					selectParticipantStatement.setInt(2, gameId);
					resultSet = selectParticipantStatement.executeQuery();
					resultSet.next();
					int hostParticipantId = resultSet.getInt("id");

					for(int i = 0; i<splitHostCountriesWithTroops.length-1; i=i+2){
						//select countries
						countryStmt = conn.prepareStatement(selectCountries);
						countryStmt.setInt(1, gameId);
						countryStmt.setString(2, splitHostCountriesWithTroops[i]);
						resultSet = countryStmt.executeQuery();

						if(resultSet.next()){
							//update countries 					
							updateCountryStmt = conn.prepareStatement(updateCountries);
							updateCountryStmt.setInt(1,hostParticipantId);
							updateCountryStmt.setInt(2, Integer.parseInt(splitHostCountriesWithTroops[i+1]));
							updateCountryStmt.setString(3, splitHostCountriesWithTroops[i]);
							updateCountryStmt.setInt(4, gameId);
							updateCountryStmt.executeUpdate();
						}

						else{
							//insert countries
							insertCountryStmt =  conn.prepareStatement(insertCountries);
							insertCountryStmt.setString(1,splitHostCountriesWithTroops[i]);
							insertCountryStmt.setInt(2, hostParticipantId);
							insertCountryStmt.setInt(3, gameId);
							insertCountryStmt.setString(4, gameName);
							insertCountryStmt.setInt(5, Integer.parseInt(splitHostCountriesWithTroops[i+1]));
							insertCountryStmt.executeUpdate();
						}
					}
					for(int i = 0; i<splitGuestCountriesWithTroops.length-1; i=i+2){

						//select countries
						countryStmt = conn.prepareStatement(selectCountries);
						countryStmt.setInt(1, gameId);
						countryStmt.setString(2, splitGuestCountriesWithTroops[i]);
						resultSet = countryStmt.executeQuery();

						if(resultSet.next()){

							updateCountryStmt = conn.prepareStatement(updateCountries);
							updateCountryStmt.setInt(1, guestParticipantId);
							updateCountryStmt.setInt(2, Integer.parseInt(splitGuestCountriesWithTroops[i+1]));
							updateCountryStmt.setString(3, splitGuestCountriesWithTroops[i]);
							updateCountryStmt.setInt(4, gameId);
							updateCountryStmt.executeUpdate();
						}

						else{
							insertCountryStmt =  conn.prepareStatement(insertCountries);
							insertCountryStmt.setString(1,splitGuestCountriesWithTroops[i]);
							insertCountryStmt.setInt(2, guestParticipantId);
							insertCountryStmt.setInt(3, gameId);
							insertCountryStmt.setString(4, gameName);
							insertCountryStmt.setInt(5, Integer.parseInt(splitGuestCountriesWithTroops[i+1]));
							insertCountryStmt.executeUpdate();
						}
					}

					System.out.println("Game saved successfully");
				}	

				else{
					//insert game
					insertGameStmt = conn.prepareStatement(insertGame, Statement.RETURN_GENERATED_KEYS); //return generated keys from 
					insertGameStmt.setString(1, gameName);
					insertGameStmt.setString(2, hostName);
					insertGameStmt.setString(3, guestName);				

					if(hostTurn.equals("true")){
						insertGameStmt.setString(4, hostName);
					}
					else {
						insertGameStmt.setString(4, guestName);
					}
					insertGameStmt.executeUpdate();				
					//get gameId
					ResultSet insertGameGeneratedKeys = insertGameStmt.getGeneratedKeys();
					insertGameGeneratedKeys.next();
					int gameId = insertGameGeneratedKeys.getInt("id");	
					//insert host Participant
					insertParticipantStatement = conn.prepareStatement(insertParticipant, Statement.RETURN_GENERATED_KEYS);
					insertParticipantStatement.setString(1, hostName);
					insertParticipantStatement.setString(2, gameName);
					insertParticipantStatement.executeUpdate();
					//get host participant id
					ResultSet participantHostGeneratedKeys = insertParticipantStatement.getGeneratedKeys();
					participantHostGeneratedKeys.next();
					int hostParticipantId =  participantHostGeneratedKeys.getInt("id");	
					//insert guest Participant
					insertParticipantStatement.setString(1, guestName);
					insertParticipantStatement.setString(2, gameName);
					insertParticipantStatement.executeUpdate();
					ResultSet participantGuestGeneratedKeys = insertParticipantStatement.getGeneratedKeys();
					participantGuestGeneratedKeys.next();
					int guestParticipantId = participantGuestGeneratedKeys.getInt("id");	
					//insert countries
					countryStmt = conn.prepareStatement(insertCountries);
					for(int i = 0; i<splitHostCountriesWithTroops.length-1; i=i+2){
						countryStmt.setString(1, splitHostCountriesWithTroops[i]);
						countryStmt.setInt(2, gameId);
						countryStmt.setInt(3, hostParticipantId);
						countryStmt.setString(4, gameName);
						countryStmt.setInt(5, Integer.parseInt(splitHostCountriesWithTroops[i+1]));
						countryStmt.executeUpdate();
					}
					for(int i = 0; i<splitGuestCountriesWithTroops.length-1; i=i+2){
						countryStmt.setString(1, splitGuestCountriesWithTroops[i]);
						countryStmt.setInt(2, gameId);
						countryStmt.setInt(3, guestParticipantId);
						countryStmt.setString(4, gameName);				
						countryStmt.setInt(5, Integer.parseInt(splitGuestCountriesWithTroops[i+1]));
						countryStmt.executeUpdate();
					}



				}
				System.out.println("game saved successfully on the database");
				conn.setAutoCommit(true);
			}
			this.closeConnection();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Exception whilst saving a game");
			e.printStackTrace();
		}

	}

	/**
	 * A method to load a game from the database allocated to the host.
	 * The game is returned in the form of an ArrayList which contains the game name, host name, guest name,
	 * a boolean indicating whose turn it is as well as a String containing the names of the countries owned by each player 
	 * and the number of troops on each country.
	 * @param hostName is the name of host to which the game is registered.
	 */
	public ArrayList<String> loadGameFromDatabase(String hostName){

		PreparedStatement selectUserIdStmt = null;
		PreparedStatement selectCountriesAndTroopsStmt = null;
		PreparedStatement selectParticipantStmt = null;
		String selectId = "SELECT id FROM USERS WHERE username =?";
		String selectParticipantId = "SELECT id FROM PARTICIPANTS WHERE user_id = ?";
		String selectGame = "SELECT * FROM GAMES WHERE host_id = ?";
		String selectCountriesAndTroops = "SELECT * FROM COUNTRIES WHERE owned_by_participant = ?";
		String selectName = "SELECT username FROM USERS WHERE id =?";
		ArrayList<String> savedGame = new ArrayList<String>(); //saved game arrayList
		StringBuffer x = new StringBuffer(); //StringBuffer for countries owned by Host with troops
		StringBuffer y = new StringBuffer();
		String guestTurn = null;		

		try{			
			this.databaseConnection();

			if(!(conn == null ||conn.isClosed())){

				conn.setAutoCommit(false);

				selectUserIdStmt = conn.prepareStatement(selectId); //create object to execute select query for host id
				selectUserIdStmt.setString(1, hostName); //Set hostname to first parameterized value of the select statement
				resultSet = selectUserIdStmt.executeQuery(); //execute select query					


				if(!(resultSet.next())){
					return null;
				}

				else{		
					//resultSet.next();
					int hostId = resultSet.getInt("id");					
					selectGameStmt = conn.prepareStatement(selectGame); //create object to execute select query for game
					selectGameStmt.setInt(1, hostId);
					resultSet = selectGameStmt.executeQuery();

					if(!(resultSet.next())){
						return null;
					}
					else{					
						String gameName = resultSet.getString("name");	
						int guestId = resultSet.getInt("guest_id");
						savedGame.add(0, gameName); //add gameName to arrayList
						savedGame.add(1, hostName); //add hostName to arrayList

						int currentPlayersTurnId = resultSet.getInt("current_players_turn_id");
						if(currentPlayersTurnId == hostId){
							savedGame.add(2, "true");
							guestTurn = "false";
						}
						else{
							savedGame.add(2,"false");
							guestTurn = "true";
						}

						selectParticipantStmt = conn.prepareStatement(selectParticipantId);
						selectParticipantStmt.setInt(1,hostId);
						resultSet = selectParticipantStmt.executeQuery();
						resultSet.next();
						int hostParticipantId = resultSet.getInt("id");

						selectParticipantStmt = conn.prepareStatement(selectParticipantId);
						selectParticipantStmt.setInt(1,guestId);
						resultSet = selectParticipantStmt.executeQuery();
						resultSet.next();
						int guestParticipantId = resultSet.getInt("id");

						selectCountriesAndTroopsStmt = conn.prepareStatement(selectCountriesAndTroops);
						selectCountriesAndTroopsStmt.setInt(1, hostParticipantId);
						resultSet = selectCountriesAndTroopsStmt.executeQuery();

						while(resultSet.next()){
							x.append(resultSet.getString("name") + ", ");

							if(resultSet.isLast()){
								x.append(resultSet.getInt("number_of_troops"));
							}
							else{
								x.append(resultSet.getInt("number_of_troops") + ", ");
							}
						}
						savedGame.add(3, x.toString()); //add countries owned and troops on country to the arrayList

						selectUserStatement = conn.prepareStatement(selectName); //create object to execute selectName query
						selectUserStatement.setInt(1, guestId); //set parameterized value to the guestId
						resultSet = selectUserStatement.executeQuery();//execute query to retrieve guest username
						resultSet.next();
						String guestName = resultSet.getString("username");
						savedGame.add(4, guestName);

						selectCountriesAndTroopsStmt = conn.prepareStatement(selectCountriesAndTroops);
						selectCountriesAndTroopsStmt.setInt(1, guestParticipantId);
						resultSet = selectCountriesAndTroopsStmt.executeQuery();
						savedGame.add(5, guestTurn);

						while(resultSet.next()){

							y.append(resultSet.getString("name") + ", ");

							if(resultSet.isLast()){
								y.append(resultSet.getInt("number_of_troops"));
							}
							else{
								y.append(resultSet.getInt("number_of_troops") + ", ");
							}
						}
						savedGame.add(6, y.toString()); //add countries owned and troops on country by guest to an arrayList			
					}

					System.out.println("Game loaded successfully");
				}
				conn.setAutoCommit(true);

				this.closeConnection();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Exception whilst loading a game");
			e.printStackTrace();
		}
		return savedGame;
	}
	/**
	 * A method to update the stats for a the users after a game has finished.
	 * @param winnerUsername the username of the winner
	 * @param loserUsername the username of the loser
	 */
	public void updateStats(String winnerUsername, String loserUsername){

		String updateWins = "UPDATE USERS SET wins = ? WHERE username = ?";
		String updateLosses = "UPDATE USERS SET losses = ? WHERE username = ?";
		String getStats = "SELECT * FROM USERS WHERE username = ?";
		String updateGamesPlayed = "UPDATE USERS SET games_played = ? WHERE username = ?";
		String updateRating = "UPDATE USERS SET rating = ? WHERE username = ?";
		PreparedStatement updateWinsStmt = null;
		PreparedStatement updateLossesStmt = null;
		PreparedStatement updateGamesStmt = null;
		PreparedStatement getStatsStmt = null;
		PreparedStatement updateRatingStmt = null;

		try {

			this.databaseConnection();
			conn.setAutoCommit(false);

			//update Winner stats
			getStatsStmt = conn.prepareStatement(getStats);
			getStatsStmt.setString(1, winnerUsername);
			resultSet = getStatsStmt.executeQuery();
			resultSet.next();
			int newNumberOfWins = resultSet.getInt("wins") + 1;
			int newGamesPlayed = resultSet.getInt("games_played") + 1;
			//update wins
			updateWinsStmt = conn.prepareStatement(updateWins);
			updateWinsStmt.setInt(1, newNumberOfWins);
			updateWinsStmt.setString(2, winnerUsername);
			updateWinsStmt.executeUpdate();
			//update games played
			updateGamesStmt = conn.prepareStatement(updateGamesPlayed);
			updateGamesStmt.setInt(1, newGamesPlayed);
			updateGamesStmt.setString(2, winnerUsername);
			updateGamesStmt.executeUpdate();
			//update rating
			int newRating = (int) (newNumberOfWins*5/newGamesPlayed);
			updateRatingStmt = conn.prepareStatement(updateRating);
			updateRatingStmt.setInt(1, newRating);
			updateRatingStmt.setString(2, winnerUsername);
			updateRatingStmt.executeUpdate();

			//update Loser stats
			getStatsStmt = conn.prepareStatement(getStats);
			getStatsStmt.setString(1, loserUsername);
			resultSet = getStatsStmt.executeQuery();
			resultSet.next();
			int newNumberOfLosses = resultSet.getInt("losses") + 1;
			int newGamesPlayedLoser = resultSet.getInt("games_played") + 1;
			//update losses
			updateLossesStmt = conn.prepareStatement(updateLosses);
			updateLossesStmt.setInt(1, newNumberOfLosses);
			updateLossesStmt.setString(2, loserUsername);
			updateLossesStmt.executeUpdate();
			//update gamesPlayed
			updateGamesStmt = conn.prepareStatement(updateGamesPlayed);
			updateGamesStmt.setInt(1, newGamesPlayedLoser);
			updateGamesStmt.setString(2, loserUsername);
			updateGamesStmt.executeUpdate();	

			conn.setAutoCommit(true);
			this.closeConnection();
		} catch (SQLException e) {
			System.out.println("SQL Exception during updating stats");
			e.printStackTrace();
		}				
	}
	public static void main(String args[]){
		DatabaseManager d2 = new DatabaseManager();
		d2.resetSequences();
	}
}

