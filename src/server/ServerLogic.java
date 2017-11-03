package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import communicationObjects.Action;
import communicationObjects.CreateLobby;
import communicationObjects.CreateLobbyAccept;
import communicationObjects.CreateLobbyDeny;
import communicationObjects.DirectMessage;
import communicationObjects.GlobalMessage;
import communicationObjects.JoinLobby;
import communicationObjects.JoinLobbyAccept;
import communicationObjects.JoinLobbyDeny;
import communicationObjects.KickGuest;
import communicationObjects.LeaveLobby;
import communicationObjects.LeaveLobbyResponse;
import communicationObjects.LoadGame;
import communicationObjects.LoadGameDeny;
import communicationObjects.Lobby;
import communicationObjects.LobbyMessage;
import communicationObjects.Logout;
import communicationObjects.NewGame;
import communicationObjects.NewGameResponse;
import communicationObjects.ResendLast;
import communicationObjects.Result;
import communicationObjects.SaveGame;
import communicationObjects.StartGame;
import communicationObjects.User;
import database.DatabaseManager;
/**
 * Class ServerLogic handles all the possible input messages the user may send. Client Logic doesnt handle any of the requests only redirects them to the relevant methods else where.
 * @author Mark Alston
 * @version 2017-03-17
 */
public class ServerLogic {

	/**
	 * This method is responsible for redirecting and dealing with messages sent by the client.
	 * @param in - clients ObjectInputStream
	 * @param out - Clients ObjectOutputStream
	 * @param serverDataCollection - The data collected about the server.
	 * @param user - the User corrisponding to the client calling this method
	 * @param d1 - the database of the game.
	 * @return - true, if the user wishes to logout this method returns true and false if the method should scan for inputs again.
	 */
	public static boolean checkForInput(ObjectInputStream in, ObjectOutputStream out, ServerDataCollection serverDataCollection, User user, DatabaseManager d1) {
		
		try {
			//Read object sent
			Object readIn = in.readObject();
			
			/* ************ *
			 * CREATE LOBBY *
			 * ************ */
			if(readIn instanceof CreateLobby) {
				//Firstly check there is enough space for another lobby.
				boolean successful = serverDataCollection.isOkToCreateLobby();
				if(successful) {
					out.writeObject(new CreateLobbyAccept("Lobby succesfully created", new Lobby(serverDataCollection.getNewLobbyIndex(),"New Lobby",user,null)));
				} else {
					out.writeObject(new CreateLobbyDeny("Lobby cannot succesfully be created, too many already exist"));
				}
				
				//Secondly read in newGame object
				Object next = in.readObject();
				if(next instanceof NewGame) {
					System.out.println(((NewGame) next).getMessage());
					serverDataCollection.createLobby(((NewGame) next).getGameName(), user);
					//Sendback response
					NewGameResponse ngr = new NewGameResponse("New Lobby Created",((NewGame) next).getGameName());
					out.writeObject(ngr);
				} else if(next instanceof LeaveLobby) {
					out.writeObject(new LeaveLobbyResponse("You have cancelled joining the lobby"));
				} else if(next instanceof LoadGame) {
					serverDataCollection.createLobby("Loaded Game", user);
					Boolean accept = ServerGameObjectTransfer.sendLoadableGame(d1, serverDataCollection, user, out, (LoadGame) next);
					if(!accept) {
						serverDataCollection.removeLobby("Loaded Game");
					} else if(accept) {
						serverDataCollection.reIndexLobbyList();
					}
				}
			
			/* ******** *
			 * NEW GAME *
			 * ******** */	
			} else if(readIn instanceof NewGame) {
				System.out.println(((NewGame) readIn).getMessage());
				serverDataCollection.createLobby(((NewGame) readIn).getGameName(), user);
				//Sendback response
				NewGameResponse ngr = new NewGameResponse("New Lobby Created",((NewGame) readIn).getGameName());
				out.writeObject(ngr);
				
			/* ********** *
			 * JOIN LOBBY *
			 * ********** */
			} else if(readIn instanceof JoinLobby) {
				Lobby successful = serverDataCollection.JoinLobby((JoinLobby) readIn, user);
				if(successful != null) {
					out.writeObject(new JoinLobbyAccept("Joined Lobby Succesfully", successful));
				} else {
					out.writeObject(new JoinLobbyDeny("Cannot Join the lobby!"));
				}
				
			/* *********** *
			 * LEAVE LOBBY *
			 * *********** */
			} else if(readIn instanceof LeaveLobby) {
				if(serverDataCollection.inGame(user)) {
					serverDataCollection.LeaveGame(user, in, out);
				} else {
					serverDataCollection.LeaveLobby(user, in, out);
				}
			
			/* ****** *
			 * RESULT *
			 * ****** */
			} else if(readIn instanceof Result) {
				serverDataCollection.sendResult((Result) readIn, user, in, out, d1);
				
			/* ********** *
			 * Kick Guest *
			 * ********** */
			} else if(readIn instanceof KickGuest) {
				
				System.out.println(((KickGuest) readIn).getMessage());
				serverDataCollection.kickGuest(user, in, out);
				
			/* ************ *
			 *    LOGOUT    *
			 * ************ */
			} else if(readIn instanceof Logout) {
				return true;
			
			/* ************** *
			 * DIRECT MESSAGE *
			 * ************** */
			} else if(readIn instanceof DirectMessage) {
				ServerMessagingManager.sendDirectMessage(((DirectMessage) readIn));
				
			/* ************* *
			 * LOBBY MESSAGE *
			 * ************* */
			}else if(readIn instanceof LobbyMessage) {
				ServerMessagingManager.sendLobbyMessage(((LobbyMessage) readIn), serverDataCollection, user);
			
			/* ************** *
			 * GLOBAL MESSAGE *
			 * ************** */
			} else if(readIn instanceof GlobalMessage) {
				ServerMessagingManager.sendGlobalMessage(((GlobalMessage) readIn), user);
				
			/* *********** *
			 * RESENT LAST *
			 * *********** */	
			} else if(readIn instanceof ResendLast) {
				System.err.println("Ooops something went wrong. Recieved a resend last object from: " + user.getUsername() + ". " + ((ResendLast) readIn).getMessage());
			
			/* ******** *
			 *  ACTION  *
			 * ******** */
			} else if(readIn instanceof Action) {
				ServerGameObjectTransfer.sendAction(serverDataCollection, user, (Action) readIn);
				
			/* ********** *
			 * START GAME *
			 * ********** */
			} else if(readIn instanceof StartGame) {
				System.out.println(((StartGame) readIn).getMessage());
				ServerGameObjectTransfer.initiateStartGame(serverDataCollection, user, out, (StartGame) readIn);
				
			/* ********* *
			 * SAVE GAME *	
			 * ********* */
			} else if(readIn instanceof SaveGame) {
				ServerGameObjectTransfer.saveCurrentGame(d1, serverDataCollection, user, out, (SaveGame) readIn);
			
			/* ******** *
			 * LoadGame *
			 * ******** */
			} else if(readIn instanceof LoadGame) {
				// a deny is sent because load game request has already been denied once!
				out.writeObject(new LoadGameDeny("Sorry, you have no saved games"));
			}
			
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		//Returns false if the user didn't send a logout request!
		return false;
	}
}
