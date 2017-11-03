package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import communicationObjects.JoinLobby;
import communicationObjects.KickGuestResponse;
import communicationObjects.LeaveLobbyResponse;
import communicationObjects.Lobby;
import communicationObjects.LogoutResponse;
import communicationObjects.Result;
import communicationObjects.User;
import database.DatabaseManager;
/**
 * ServerDataCollection collects data about the state of all the clients. Clients are put into one of the 3 lists including;
 * 		1) playersList - an ArrayList of all the User's logged on
 * 		2) lobbyList - An ArrayList of all the Lobbies currently waiting to start a game
 * 		3) gameList - An ArrayList od all the Lobbies currently playing a game.
 * Note that all clietns are part of the playersList, but cannot be part of both LobbyList or GameList.
 * 
 * @author Mark Alston
 * @version2017-03-17
 */
public class ServerDataCollection extends Observable {
	
	//Field variables
	private ArrayList<Lobby> gameList;
	private ArrayList<User> playersList; 
	private ArrayList<Lobby> lobbyList;
	
	//Constructor
	public ServerDataCollection(ArrayList<Lobby> gameList, ArrayList<User> playersList,ArrayList<Lobby> lobbyList) {
		this.gameList = gameList;
		this.playersList = playersList;
		this.lobbyList = lobbyList;
	}
	
	//Getters and setters 
	public synchronized ArrayList<Lobby> getGameList() {
		return gameList;
	}

	public synchronized ArrayList<User> getPlayersList() {
		return playersList;
	}

	public synchronized ArrayList<Lobby> getLobbyList() {
		return lobbyList;
	}
	
	public synchronized void setGameList(ArrayList<Lobby> gameList) {
		this.gameList = gameList;
		this.setChanged();
		this.notifyObservers();
	}

	public synchronized void setPlayersList(ArrayList<User> playersList) {
		this.playersList = playersList;
		this.setChanged();
		this.notifyObservers();
	}

	public synchronized void setLobbyList(ArrayList<Lobby> lobbyList) {
		this.lobbyList = lobbyList;
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * addUser() adds a User to the PlayersList. The PLayers are indexed according to there position in the List.
	 * Once a User is added there index's are updated by calling reIndexPlayerList().
	 * @param user - the User object corresponding to the client thread
	 */
	public synchronized void addUser(User user) {
		this.getPlayersList().add(user);
		this.reIndexPlayersList();
	}

	/**
	 * Checks the number of current lobbies waiting. If more than 6 then will return false.
	 * @return true if the number of waiting lobbies is 6 or less. False otherwise.
	 */
	public synchronized boolean isOkToCreateLobby() {
		if(this.getLobbyList().size() < 7) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a lobby to lobby ArrayList. It is assumed that before calling this method ,that the isOkToCreateLobby, was called before hand.
	 * @param name - name of the lobby
	 * @param host - User of the host
	 */
	public synchronized void createLobby(String name, User host) {
			Lobby lobby = new Lobby(this.getLobbyList().size(), name, host, null);
			this.getLobbyList().add(lobby);
			if(name.equals("Loaded Game") == false) {
				this.reIndexLobbyList();
			}
	}

	/**
	 * Changes the guest from null to the actual user.
	 * @param readIn - the Lobby that the user wishes to join!
	 * @param user - the user associated with the client thread, requesting to joing the lobby as a guest.
	 * @return - the new lobby with guest != null is returned.
	 */
	public synchronized Lobby JoinLobby(JoinLobby readIn, User user) {
		//add user to lobby...
		this.getLobbyList().remove(readIn.getLobbyToJoin());
		Lobby lobbyToJoin = readIn.getLobbyToJoin();
		
		if(lobbyToJoin.getGuest() == null) {
			lobbyToJoin.setGuest(user);
		} else {
			//this will occur if the lobby already has 2 players in it.
			return null;
		}
		
		this.getLobbyList().add(lobbyToJoin);
		this.reIndexLobbyList();
		return lobbyToJoin;
	}

	/**
	 * This method reindexes the Lobbies List to update the queue priority.
	 */
	public synchronized void reIndexLobbyList() {
		
		ArrayList<Lobby> list = this.getLobbyList();
		ArrayList<Lobby> newList = new ArrayList<Lobby>();
		//populate a new lobbyList
		for(int i=0 ; i<list.size() ; i++) {
			Lobby lobby = new Lobby(i,list.get(i).getName(), list.get(i).getHost(), list.get(i).getGuest());
			newList.add(lobby);
		}
		//set the changes
		this.setLobbyList(newList);	
	}
	
	/**
	 * Re indexes the players list. This method should be run everytime the playersList is altered.
	 */
	private synchronized void reIndexPlayersList() {
		ArrayList<User> list = this.getPlayersList();
		ArrayList<User> newList = new ArrayList<User>();
		//populate a new lobbyList
		for(int i=0 ; i<list.size() ; i++) {
			User user = new User(i,list.get(i).getUsername(), list.get(i).getNumberOfGamesPlayed(), list.get(i).getNumberOfLosses(), list.get(i).getNumberOfWins());
			newList.add(user);
		}
		//set the changes
		this.setPlayersList(newList);
	}
	
	/**
	 * Re indexes the gamesList. This method should be run everytime the playersList is altered.
	 */
	private synchronized void reIndexGameList() {
		ArrayList<Lobby> list = this.getGameList();
		ArrayList<Lobby> newList = new ArrayList<Lobby>();
		//populate a new lobbyList
		for(int i=0 ; i<list.size() ; i++) {
			Lobby lobby = new Lobby(-1-i,list.get(i).getName(), list.get(i).getHost(), list.get(i).getGuest());
			newList.add(lobby);
		}
		//set the changes
		this.setGameList(newList);
	}

	/**
	 * The next lobby index is returned.
	 * @return the next available lobby priority index
	 */
	public synchronized int getNewLobbyIndex() {
		return this.getLobbyList().size();
	}

	/**
	 * When a user is no longer connected to the game. This methods removes all appearances of their name.
	 * @param user - the User associated with the client thread
	 * @param in - the users objectInputStream
	 * @param out - the users objectOutputStream
	 * @param reason - this string effects whether or not the player is removed from ServerMain.threads. If the reason is anything but Logout the user will be
	 * removed from the ServerMain.threads arrayList.
	 */
	public synchronized void removeUser(User user, ObjectInputStream in, ObjectOutputStream out, String reason) {
		ArrayList<User> playersList = this.getPlayersList();
		
		/* 
		 * Sort Players list out 
		 */
		for(int i=0 ; i<playersList.size() ; i++) {
			if(playersList.get(i).equals(user)) {
				playersList.remove(i);
				this.reIndexPlayersList();
			}
		}
		
		/*
		 * Sort LobbyList / game list out
		 */
		if(this.inGame(user)) {
			this.LeaveGame(user, in, out);
			this.reIndexGameList();
		} else {
			this.LeaveLobby(user, in, out);
			this.reIndexLobbyList();
		}
		
		//Inform user they have been logged out.
		if(reason.equals("Logout")) {
			try {
				out.writeObject(new LogoutResponse("Goodbye :'("));
			} catch (IOException e) {
				System.err.println("LogoutResponse not sent to user");
			}
		}
		
		//PUT USER TO A RANDOM ID IF LOGGING OUT OR REMOVE THEM FROM THREAD POOL.
		for(int i=0 ; i<ServerMain.threads.size() ; i++) {
			try {
				if(ServerMain.threads.get(i).getUser().equals(user)) {
					if(reason.equals("Logout")) {
						Random random = new Random();
						ServerMain.threads.get(i).getUser().setUserId(1000);
						ServerMain.threads.get(i).getUser().setUsername("" + random.nextInt(100));
						ServerMain.threads.get(i).getUser().setNumberOfGamesPlayed(0);
						ServerMain.threads.get(i).getUser().setNumberOfLosses(0);
						ServerMain.threads.get(i).getUser().setNumberOfWins(0);
						
					} else {
						ServerMain.threads.remove(i);
					}
				}
			} catch(NullPointerException e) {
			}
		}

		//UPDATE ALL CHANGES...
		this.reIndexPlayersList();
		this.reIndexLobbyList();
		this.reIndexGameList();
	}

	/**
	 * Used when the host wants to remove a guest from within his lobby
	 * @param user - this is the host of a lobby trying to remove a guest.
	 * @param out - users ObjectOutputStream
	 * @param in - users ObjectInputStream
	 */
	public synchronized void kickGuest(User user, ObjectInputStream in, ObjectOutputStream out) {
		
		ArrayList<Lobby> lobbyList = this.getLobbyList();
		for(int i=0 ; i<lobbyList.size() ; i++) {
			if(lobbyList.get(i).getHost().equals(user) && lobbyList.get(i).getGuest() != null) {
				
				//send kick guest response to guest
				for(int j=0 ; j<ServerMain.threads.size() ; j++) {
					if(lobbyList.get(i).getGuest().equals(ServerMain.threads.get(j).getUser())) {
						try {
							ServerMain.threads.get(j).getOut().writeObject(new KickGuestResponse("The guest has kicked you out of his lobby"));
						} catch (IOException e) {
							System.err.println("KickGuestResponse not sent to guest user");
						}
					}
				}
				
				// set the guest to null
				lobbyList.get(i).setGuest(null);
				
				//send kick guest response to host
				try {
					out.writeObject(new KickGuestResponse("You have succesfully kicked out the guest"));
				} catch (IOException e) {
					System.err.println("KickGuestResponse not sent to host user");
				}
				
				//update the server data
				this.reIndexLobbyList();
				
			}
		}
		
		
	}

	/**
	 * Called when a user selects Leave Lobby button on gui.
	 * @param user - the User associated with the client thread
	 * @param in - users ObjectInputStream
	 * @param out - users ObjectOutputStream
	 */
	public synchronized void LeaveLobby(User user, ObjectInputStream in, ObjectOutputStream out) {
		
		boolean sent = false;
		
		runner:
		for(int i=0 ; i<lobbyList.size() ; i++) {
			try {
				Lobby lobby = lobbyList.get(i);
				//if the lobby has the user as a host then delete the lobby
				if(lobby.getHost().equals(user)) {
					
					// send guest KickGuestResponse
					try {
					
						for(int j=0 ; j<ServerMain.threads.size() ; j++) {
							if(lobby.getGuest() != null && lobby.getGuest().equals(ServerMain.threads.get(j).getUser())) {
								ServerMain.threads.get(j).getOut().writeObject(new KickGuestResponse("The lobby host has logged out"));
							}
						}
						//send confirmation message
						out.writeObject(new LeaveLobbyResponse("You succesfuly left the lobby"));
						sent = true;
						
					} catch (IOException e) {
					}
					
					// delete lobby from list
					lobbyList.remove(i);
					this.reIndexLobbyList();
					break runner;
					
					//if the lobby has the user as a guest	
				} else if(lobby.getGuest().equals(user)) {
					lobby.setGuest(null);
					try {
						out.writeObject(new KickGuestResponse("You have left the lobby"));
						sent = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
					break runner;
				}
			} catch(NullPointerException e) {
			}
		}
		
		/*
		 * sent will still be false in the case the user clicks cancel, when creating a lobby, as they will not be present
		 * in any other lobbies.
		 */
		if(sent == false) {
			try {
				out.writeObject(new LeaveLobbyResponse("You succesfuly left the lobby"));
			} catch (IOException e) {
			}
		}
		
		//update
		this.reIndexLobbyList();
	}
	
	/**
	 * If a player wishes to leave a game this method informs the other player.
	 * @param user - the User associated with the client thread
	 * @param in - users ObjectInputStream
	 * @param out - users ObjectOutputStream
	 */
	public synchronized void LeaveGame(User user, ObjectInputStream in, ObjectOutputStream out) {
		
		if(this.gameList.isEmpty()) {
			return;
		}
		
		runner:
		for(int i=0 ; i<this.gameList.size() ; i++) {
			try {
				Lobby lobby = gameList.get(i);
				//if the lobby has the user as a host then delete the lobby
				if(lobby.getHost().equals(user)) {
					
					// send guest KickGuestResponse
					try {
					
						for(int j=0 ; j<ServerMain.threads.size() ; j++) {
							if(lobby.getGuest() != null && lobby.getGuest().equals(ServerMain.threads.get(j).getUser())) {
								ServerMain.threads.get(j).getOut().writeObject(new KickGuestResponse("The lobby host has logged out"));
							}
						}
					
						//send confirmation message
						out.writeObject(new LeaveLobbyResponse("You succesfuly left the lobby"));
					
					} catch (IOException e) {
					}
					
					// 1) delete lobby from list
					this.gameList.remove(i);
					this.reIndexGameList();
					break runner;
					
				//if the lobby has the user as a guest	
				} else if(lobby.getGuest().equals(user)) {
					
					
					//Added in code here...
					this.gameList.remove(i);
					this.reIndexGameList();
					//STOP
					
					
					
					//lobby.setGuest(null);
					//try {
					//	out.writeObject(new KickGuestResponse("You have left the Game"));
					//} catch (IOException e) {
					//}
				}
			} catch(NullPointerException e) {
			}
		}
		this.reIndexGameList();
	}

	/**
	 * This method changes the lobby index to -1 which has started the game. It also adds is to the GameList and removes it from the lobbyList.
	 * @param user - the User associated with the client thread
	 */
	public synchronized void startGame(User user) {
		
		for(int i=0 ; i < this.getLobbyList().size() ; i++) {
			try {
				if(this.getLobbyList().get(i).getHost().equals(user) || this.getLobbyList().get(i).getGuest().equals(user)) {
					Lobby lobby = this.getLobbyList().get(i);
					Lobby newLobby = new Lobby(-1, lobby.getName(), lobby.getHost(), lobby.getGuest());
					this.getGameList().add(newLobby);
					this.getLobbyList().remove(i);
					System.out.println("Lobby moved to the Games List");
				}
			} catch(NullPointerException e) {
			}
		}
		this.reIndexLobbyList();
		this.reIndexGameList();
	}
	
	/**
	 * This method checks to see if a user is in a game.
	 * @param user - the User associated with the client thread
	 * @return true, if the user is in a game. False if not.
	 */
	public synchronized boolean inGame(User user) {
		
		if(this.gameList.isEmpty()) {
			return false;
		}
		
		for(int i=0 ; i < this.gameList.size() ; i++) {
			try {
				if(this.getGameList().get(i).getHost().equals(user)) {
					return true;
				} else if(this.gameList.get(i).getGuest().equals(user)) {
					return true;
				}
			} catch(Exception e) {
			}
		}
		return false;
	}

	/**
	 * Removes any lobby with the given name.
	 * @param string - name of lobby you wish to remove
	 */
	public synchronized void removeLobby(String name) {
		for(int i=0 ; i<this.lobbyList.size() ; i++) {
			if(this.lobbyList.get(i).getName().equals(name)) {
				this.lobbyList.remove(i);
			}
		}
	}

	/**
	 * This method updates the user stats after as game is finished and removes both players from the games list.
	 * @param result
	 * @param user
	 * @param in
	 * @param out
	 * @param d1
	 */
	public synchronized void sendResult(Result result, User user, ObjectInputStream in, ObjectOutputStream out, DatabaseManager d1) {
		
		String host = result.getUsername();
		String guest = null;
		for(int i=0 ; i<this.getGameList().size() ; i++) {
			if(this.getGameList().get(i).getHost().getUsername().equals(host)) {
				guest = this.getGameList().get(i).getGuest().getUsername();
			}
		}
		
		// update the result
		if(result.getResult()) {
			d1.updateStats(host, guest);
		} else {
			d1.updateStats(guest, host);
		}
		
		//LeaveGame
		this.LeaveGame(user, in, out);
	}
}
