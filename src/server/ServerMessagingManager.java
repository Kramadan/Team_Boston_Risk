package server;

import java.io.IOException;
import java.util.ArrayList;

import communicationObjects.DirectMessage;
import communicationObjects.GlobalMessage;
import communicationObjects.Lobby;
import communicationObjects.LobbyMessage;
import communicationObjects.User;

public class ServerMessagingManager {

	/**
	 * Sends the object message to only one other user logged onto the server. The specific user has a user name defined in the DirectMessage.
	 * @param message - an object containing the message
	 */
	public static void sendDirectMessage(DirectMessage message) {
		System.out.println(ServerMain.threads.size());
		for(int i=0 ; i<ServerMain.threads.size() ; i++) {
			if(ServerMain.threads.get(i).getUser() == null) {
				//this may happen if a user is currently logging in.
			}else if(ServerMain.threads.get(i).getUser().getUsername().equals(message.getUserToMessage())) {
				try {
					ServerMain.threads.get(i).getOut().writeObject(message);
				} catch (IOException e) {
					System.err.println("The direct message to " + ServerMain.threads.get(i).getUser().getUsername() + " could not be sent");
				}
			}
		}
	}
	
	/**
	 * Global message object gets sent to all users logged onto the server.
	 * @param message - an object containing the message
	 * @param requestingUser - user who requests to send the message
	 */
	public static void sendGlobalMessage(GlobalMessage message, User requestingUser) {
		for(int i=0 ; i<ServerMain.threads.size() ; i++) {	
			if(ServerMain.threads.get(i).getUser() == null) {
				//This may happen if a user is currently logging in.
			}else if(ServerMain.threads.get(i).getUser().equals(requestingUser)) {
				//do nothing
			} else {
				try {
					ServerMain.threads.get(i).getOut().writeObject(message);
				} catch (IOException e) {
					System.err.println("The global message failed to be sent to " + ServerMain.threads.get(i).getUser().getUsername());
				}
			}
		}
	}
	
	/**
	 * LobbyMessage sends the LobbyMessage object to all users logged in and in a lobby.
	 * @param message - an object containing the message
	 * @param user - the user sending the message
	 * @param serverDataCollection - data collected from the server
	 */
	public static void sendLobbyMessage(LobbyMessage message, ServerDataCollection sdc, User user) {
		
		//Add all lobbies into a list.
		ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();
		lobbyList = sdc.getLobbyList();
		for(int i=0 ; i<sdc.getGameList().size() ; i++) {
			lobbyList.add(sdc.getGameList().get(i));
		}
		
		for(int i=0 ; i<lobbyList.size() ; i++) {
			//Check for the lobby that needs to receive the LobbyMessage
			if(lobbyList.get(i).getName().equals(message.getLobbyToMessage())) {
				
				//Check if the person who sent the message is the host or guest of the lobby. 
				if(user.equals(lobbyList.get(i).getHost())) {
					//send message to guest
					for(int j=0 ; j<ServerMain.threads.size() ; j++) {
						try {
							if(ServerMain.threads.get(j).getUser().equals(lobbyList.get(i).getGuest())) {
								try {
									ServerMain.threads.get(j).getOut().writeObject(message);
									return;
								} catch (IOException e) {
									System.err.println("The Lobby message wasnt able to be sent to " + user.getUsername());
								}
							}
						} catch(NullPointerException e) {
						}
					}
				//Check if the user is a guest of the lobby and then send to the host.
				} else if(user.equals(lobbyList.get(i).getGuest())) {
					//send message to host
					for(int j=0 ; j<ServerMain.threads.size() ; j++) {
						try {
							if(ServerMain.threads.get(j).getUser().equals(lobbyList.get(i).getHost())) {
								try {
									ServerMain.threads.get(j).getOut().writeObject(message);
									return;
								} catch (IOException e) {
									System.err.println("The Lobby message wasnt able to be sent to " + user.getUsername());
								}
							}
						} catch(NullPointerException e) {
						}
					}
				//outsider sending message to full lobby list.
				} else {
					//Send message to both members of the lobby.
					for(int j=0 ; j<ServerMain.threads.size() ; j++) {
						try {
							if(ServerMain.threads.get(j).getUser().equals(lobbyList.get(i).getHost()) || ServerMain.threads.get(j).getUser().equals(lobbyList.get(i).getGuest())) {
								try {
									ServerMain.threads.get(j).getOut().writeObject(message);
								} catch (IOException e) {
									System.err.println("The Lobby message wasnt able to be sent to " + user.getUsername());
								}
							}
						} catch(NullPointerException e) {
						}
					}
					return;
				}
			}
		}
	}
}
