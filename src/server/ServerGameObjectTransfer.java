package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import communicationObjects.Action;
import communicationObjects.LoadGame;
import communicationObjects.LoadGameAccept;
import communicationObjects.LoadGameDeny;
import communicationObjects.SaveGame;
import communicationObjects.SaveGameResponse;
import communicationObjects.StartGame;
import communicationObjects.StartGameAccept;
import communicationObjects.StartGameDeny;
import communicationObjects.User;
import database.DatabaseManager;

public class ServerGameObjectTransfer {

	/**
	 * This sends an object of type action to the other player in the game.
	 * @param sdc - ServerDataCollection
	 * @param user - the user sending the request
	 */
	public static void sendAction(ServerDataCollection sdc, User user, Action action) {
		
		User otherUser = null;
		
		/*
		 * Get the user who the action needs to be sent to.
		 */
		for(int i=0 ; i<sdc.getGameList().size() ; i++) {
			if(sdc.getGameList().get(i).getHost().equals(user)) {
				otherUser = sdc.getGameList().get(i).getGuest();
			} else if(sdc.getGameList().get(i).getGuest().equals(user)) {
				otherUser = sdc.getGameList().get(i).getHost();
			}
		}
		
		/*
		 * Get the output stream of the user who the action needs to be sent to.
		 */
		for(int i=0 ; i<ServerMain.threads.size() ; i++) {
			try{
				if(ServerMain.threads.get(i).getUser().equals(otherUser)) {
					try {
						ServerMain.threads.get(i).getOut().writeObject(action);
						ServerMain.threads.get(i).getOut().reset();
					} catch (IOException e) {
						System.err.println("Action not able to be sent to: " + otherUser.getUsername());
					}
				}
			} catch(NullPointerException e) {
			}
		}
	}

	/**
	 * Sends a game saved in the database back to the client requesting to join the lobby.
	 * @param d1 - database instance
	 * @param serverDataCollection - ServerDataCollection
	 * @param user - the user who requested the saving of the game
	 * @param out - the users ObjectoutputStream
	 * @param readIn - the LoadGame object
	 */
	public static boolean sendLoadableGame(DatabaseManager d1, ServerDataCollection serverDataCollection, User user, ObjectOutputStream out, LoadGame readIn) {
		ArrayList<String> savedGame = d1.loadGameFromDatabase(user.getUsername());
		
		//LoadGameFromDatabase() will return null if these is no game to load!
		if(savedGame==null) {
			try {
				out.writeObject(new LoadGameDeny("Sorry, you have no saved games"));
				return false;
			} catch (IOException e) {
				System.err.println("Tried but could not send LoadGameDeny to: " + user.getUsername());
			}
		} else {
			try {
				//sets the game data in lobby to the loaded game (savedGame)
				for(int i=0 ; i < serverDataCollection.getLobbyList().size() ; i++) {
					try {
						if(serverDataCollection.getLobbyList().get(i).getHost().equals(user)) {
							serverDataCollection.getLobbyList().get(i).setName(savedGame.get(0));
							serverDataCollection.getLobbyList().get(i).setGameData(savedGame);
							out.reset();
							out.writeObject(new LoadGameAccept("Game Loaded Succesfully", serverDataCollection.getLobbyList().get(i)));
							return true;
						}
					}catch(NullPointerException e) {
					}
				}
			} catch (IOException e) {
				System.err.println("Tried but could not send LoadGameAccept to: " + user.getUsername());
			}
		}
		return false;
	}

	/**
	 * This method starts a game. If a game is succesfully started the lobby list and game list are updated and new server data is sent. only 2 games can run at
	 * a time. priority is given to the top in the lobby list.
	 * @param serverDataCollection
	 * @param user
	 * @param out
	 * @param readIn
	 */
	public static void initiateStartGame(ServerDataCollection sdc, User user, ObjectOutputStream out, StartGame readIn) {
		int gameSize = sdc.getGameList().size();
		int userPriority = 1000; // if still 1000 must not let start game!
		User otherUser = null;
		
		//Check the host is not trying to start a new game when has no guest 
		for(int i=0 ; i<sdc.getLobbyList().size() ; i++) {
			if(sdc.getLobbyList().get(i).getHost()==null) {
			} else if(sdc.getLobbyList().get(i).getHost().equals(user)) {
				if(sdc.getLobbyList().get(i).getGuest()==null) {
					try {
						out.writeObject(new StartGameDeny("Sorry, you must wait until you have a guest"));
					} catch (IOException e) {
						System.err.println("Tried but could not send StartGameDeny to: " + user.getUsername());
					}
					return;
				}
			}
		}
		
		//get priority and find the other user.
		for(int i=0 ; i<sdc.getLobbyList().size() ; i++) {
			if(sdc.getLobbyList().get(i).getHost().equals(user) || sdc.getLobbyList().get(i).getGuest().equals(user)) {
				userPriority = sdc.getLobbyList().get(i).getIndex();
				if(sdc.getLobbyList().get(i).getHost().equals(user)) {
					otherUser = sdc.getLobbyList().get(i).getGuest();
				} else {
					otherUser = sdc.getLobbyList().get(i).getHost();
				}
			}
		}
		
		//Check that the player has the correct priority to start a game...
		if(gameSize >= 3) {
			try {
				out.writeObject(new StartGameDeny("Sorry you must wait to start a game until there is a space."));
			} catch (IOException e) {
				System.err.println("Tried but could not send StartGameDeny to: " + user.getUsername());
			}
		} else if((gameSize == 2 && userPriority != 0) || (gameSize == 1 && userPriority >= 1) || (gameSize == 0 && userPriority >= 2)) {
			try {
				out.writeObject(new StartGameDeny("Sorry you must wait to start a game until there is a space."));
			} catch (IOException e) {
				System.err.println("Tried but could not send StartGameDeny to: " + user.getUsername());
			}
		} else {
			
			//must now update the gameList and LobbyList!
			sdc.startGame(user);
			try {
				//send to the user 
				out.writeObject(new StartGameAccept("Start your game, and remember to take a cheeky risk..."));
				System.out.println("StartGameAccept sent to: " + user.getUsername());
				//Send to other user in lobby.
				for(int j=0 ; j<ServerMain.threads.size() ; j++) {
					try {
						if(ServerMain.threads.get(j).getUser().equals(otherUser)) {
							ServerMain.threads.get(j).getOut().writeObject(new StartGameAccept("The other player has started the game. Enjoy and remember to take a cheeky risk..."));
							System.out.println("StartGameAccept sent to: " + ServerMain.threads.get(j).getUser().getUsername() );
							return;
						}
					} catch(NullPointerException e) {
					}
				}
				
			} catch (IOException e) {
				System.err.println("Tried but could not send StartGameAccept to: " + user.getUsername());
			}
			
		}
	}

	/**
	 * This method sends the game object to save to the database, and sends a confirmation to the user who selected save game.
	 * @param d1 
	 * @param serverDataCollection
	 * @param user
	 * @param out
	 * @param readIn
	 */
	public static void saveCurrentGame(DatabaseManager d1, ServerDataCollection serverDataCollection, User user, ObjectOutputStream out, SaveGame game) {
		d1.saveGameDatabase(game.getGameData());
		try {
			out.writeObject(new SaveGameResponse("Game Saved"));
		} catch (IOException e) {
			System.err.println("tried to but failed to send SaveGameResponse to: " + user.getUsername());
		}
		
	}
	
	
	
	

}
