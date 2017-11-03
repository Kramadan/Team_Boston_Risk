package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import communicationObjects.Accept;
import communicationObjects.AcceptLogin;
import communicationObjects.Action;
import communicationObjects.Attack;
import communicationObjects.Authentification;
import communicationObjects.CreateLobbyAccept;
import communicationObjects.CreateLobbyDeny;
import communicationObjects.CreateLobbyResponse;
import communicationObjects.Deny;
import communicationObjects.DenyLogin;
import communicationObjects.DirectMessage;
import communicationObjects.Draft;
import communicationObjects.EndTurn;
import communicationObjects.Forfeit;
import communicationObjects.Fortify;
import communicationObjects.GlobalMessage;
import communicationObjects.JoinLobbyAccept;
import communicationObjects.JoinLobbyDeny;
import communicationObjects.JoinLobbyResponse;
import communicationObjects.KickGuestResponse;
import communicationObjects.LoadGameAccept;
import communicationObjects.LoadGameDeny;
import communicationObjects.LoadGameResponse;
import communicationObjects.LobbyMessage;
import communicationObjects.LoginResponse;
import communicationObjects.Message;
import communicationObjects.NewGameResponse;
import communicationObjects.Request;
import communicationObjects.ResendLast;
import communicationObjects.Response;
import communicationObjects.Result;
import communicationObjects.SaveGame;
import communicationObjects.ServerData;
import communicationObjects.ServerDataResponse;
import communicationObjects.SkipAttack;
import communicationObjects.StartGameAccept;
import communicationObjects.StartGameDeny;
import communicationObjects.StartGameResponse;
import riskGame.Country;
import riskGame.Game;
import riskGame.Map;
import riskGame.Player;

/**
 * public class Client extends Thread.
 * This is the main client class that encapsulates all other classes used by the client, including the game and the game GUI and contains the main method for running the client.
 * Methods in this class deal with sending a receiving Java objects to and from the server.
 * This is the only class that can interact with the server, all other classes on the client communicate with the server through this class.
 * @author Oliver Kamperis
 */
public class Client extends Thread {
	private static final String AUTHENTIFICATIONCODE = "IAMTEAMBOSTONRISKCLIENT";
	private final String server;
	private final int port;
	private final Socket socket;
	private final ObjectOutputStream objOut;
	private final ObjectInputStream objIn;
	private final Data data;
	private final ClientGui clientGui;

	private String waitingOn;
	public static final String LOGINRESPONSE = "LoginResponse";
	public static final String LOGOUTRESPONSE = "LogoutResponse";
	public static final String REGISTERRESPONSE = "RegisterResponse";
	public static final String SERVERDATARESPONSE = "ServerDataResponse";
	public static final String JOINLOBBYRESPONSE = "JoinLobbyResponse";
	public static final String CREATELOBBYRESPONSE = "CreateLobbyResponse";
	public static final String NEWGAMERESPONSE = "NewGameResponse";
	public static final String LOADGAMERESPONSE = "LoadGameResponse";
	public static final String LEAVELOBBYRESPONSE = "LeaveLobbyResponse";
	public static final String STARTGAMERESPONSE = "StartGameResponse";
	public static final String KICKGUESTRESPONSE = "KickGuestResponse";
	public static final String SAVEGAMERESPONSE = "SaveGameResponse";
	public static final String NOTHING = "Nothing";

	/**
	 * Constructs a new Client object.
	 * The contructor will connect to the server specified by the given server name and port combination and attempt to authentificate with it.
	 * If the authentification is accepted the the client will initialise all of its internal data structures and start the GUI and the ponging thread.
	 * @param server - The String of the host name or IP address of the host server
	 * @param port - The int of the port to connest to
	 * @throws AuthentificationFailException - If the client failed to authenticate itself with the server
	 * @throws IOException - If any IOException is throw whilst connecting to the server
	 */
	public Client(String server, int port) throws AuthentificationFailException, IOException {
		this.server = server;
		this.port = port;
		this.waitingOn = Client.NOTHING;

		try {
			this.socket = new Socket(server, port);
			this.objOut = new ObjectOutputStream(socket.getOutputStream());
			this.objIn = new ObjectInputStream(socket.getInputStream());
			System.out.println("Connected to server: " + server + ". On port " + port + ".");

		} catch (UnknownHostException e) {
			throw new IOException("The IP address of the host " + server + " could not be determined.", e);
		} catch (IllegalArgumentException e) {
			throw new IOException("There is no such port: " + port + ". Port number must be between 0 and 65535, inclusive.", e);
		} catch (IOException e) {
			throw new IOException("An IOException occured whilst creating the socket on port " + port + " or whilst creating the output or input streams.", e);
		}

		Response response = sendAuthentification();
		socket.setSoTimeout(10000);

		if (response instanceof Accept) {
			System.out.println("The authentification was accepted by the server. The server said: " + response.getMessage());

			try {
				new PongingThread(this);
			} catch (IOException e) {
				throw new IOException("An exception occured whilst creating the PongingThread.", e);
			}

			this.data = new Data();
			this.clientGui = new ClientGui(this);
			clientGui.setVisible(true);

		} else if (response instanceof Deny) {
			throw new AuthentificationFailException("The authentification was denied by the server. The server said: " + response.getMessage());
		} else {
			throw new AuthentificationFailException("The received object was of an unexpected type.");
		}
	}

	/** 
	 * This is the client's run method which loops continuously whilst the client is running, to listen for incoming messages.
	 * The method blocks until it recives and input from the server, or after 10 seconds of waiting it checks to see if it is waiting on nothing, if it is not it sets the sets the client to be waiting on nothing and informs the user it did not receive a response in 10 seconds so that they can send the request again.
	 * It determines which of the three possible super type protocols that it can receive from the server it is recieving.
	 * It will then invoke the relevant method depending on what is recieved.
	 * If it recieves a unknown type it will send a ResendLast back to the server.
	 * The method will invoke the client's disconnected(String stackTrace) method if an IOException is thrown anywhere within this method.
	 */
	@Override
	public void run() {
		while (true) {
			Object obj = null;
			try {
				obj = objIn.readObject();
			} catch (SocketTimeoutException e) {
				if (!waitingOn.equals(Client.NOTHING)) {
					data.setText("Didn't receive a response from the server within 10 seconds."
							+ "\n" + "You may now attempt to re-send the request.");
					waitingOn = Client.NOTHING;
				}
				continue;
			} catch (ClassNotFoundException e) {
				sendRequest(new ResendLast("The received object was of an unknown, non-serializable or primitive type."));
				continue;
			} catch (IOException e) {
				disconnected("The client has disconnected from the server for an unknown reason whilst attempting to read from the ObjectInputStream:"
						+ "\n" + e.getMessage());
			}

			if (obj instanceof Response) {
				processResponse((Response)obj);
			} else if (obj instanceof Action) {
				processAction((Action)obj);
			} else if (obj instanceof Message) {
				processMessage((Message)obj);
			} else {
				sendRequest(new ResendLast("Recieved an object of type " + obj.getClass().getName() + " the only accepted types are, Response, Action and Message."));
			}
		}
	}

	/**
	 * Processes a Response object recieved from the server.
	 * The text contained in the Response is displayed on the display panel at the top of the client if it is not null.
	 * This method then determines whether the received response is a type that the client was expecting.
	 * Or is a special type that can be received at any time.
	 * Then invokes the relevant method for processing the Response.
	 * If the Response was invalid, i.e. wasn't the response the client was expecting then a ResendLast object is sent back to the client.
	 * If a ClassNotFoundException is thrown in this method the client's crash(String stackTrace) method will be invoked.
	 * @param response - The Response to be processed
	 */
	public void processResponse(Response response) {
		if (response.getMessage() != null) {
			data.setText(response.getMessage());
		}
		try {
			if (!waitingOn.equals(Client.NOTHING) && Class.forName("communicationObjects." + waitingOn).isAssignableFrom(response.getClass())) {
				switch (waitingOn) {
				case Client.LOGINRESPONSE:
					processLogin((LoginResponse)response);
					break;
				case Client.LOGOUTRESPONSE:
					logout();
					break;
				case Client.REGISTERRESPONSE:
					waitingOn = Client.NOTHING;
					break;
				case Client.SERVERDATARESPONSE:
					processServerData((ServerDataResponse)response);
					break;
				case Client.JOINLOBBYRESPONSE:
					processJoinLobby((JoinLobbyResponse)response);
					break;
				case Client.CREATELOBBYRESPONSE:
					processCreateLobby((CreateLobbyResponse)response);
					break;
				case Client.NEWGAMERESPONSE:
					processNewGame((NewGameResponse)response);
					break;
				case Client.LOADGAMERESPONSE:
					processLoadGame((LoadGameResponse)response);
					break;
				case Client.LEAVELOBBYRESPONSE:
					leaveLobby();
					break;
				case Client.KICKGUESTRESPONSE:
					waitingOn = Client.NOTHING;
					break;
				case Client.STARTGAMERESPONSE:
					processStartGame((StartGameResponse)response);
					break;
				case Client.SAVEGAMERESPONSE:
					waitingOn = Client.NOTHING;
					break;
				}
			} else if (response instanceof ServerDataResponse) { // ServerDataResponse must be handled differently since server data can be sent at any time (every time something changes on the server) so the client won't be expecting it.
				processServerData((ServerDataResponse)response);
			} else if (response instanceof KickGuestResponse) { // KickGuestResponse must be handled differently since if a guest is kicked the guest themselves won't actually be expecting the kick.
				kickedByHost();
			} else if (response instanceof StartGameResponse) { // StartGameResponse must be handled differently since if the other user in your lobby starts the game you won't be expecting it.
				processStartGame((StartGameResponse)response);
			} else {
				sendRequest(new ResendLast("Got a response of type " + response.getClass().getName() + " when was expecting a response of type " + waitingOn + "."));
			}
		} catch (ClassNotFoundException e) {
			crash("Whilst attempting to determine the response type being waited on the type could "
					+ "not be determined therefore the response could not be processed.");
		}
	}

	/**
	 * Processes a Action received from the server.
	 * This method simply determines the type of the Action, either Draft, Attack, SkipAttack, Fortify or Endturn and then invokes the relevant method on the game.
	 * Else the Action was not of the five above types a ResendLast is sent back to the server.
	 * @param action - The Action object to be processed
	 */
	public void processAction(Action action) {
		if (action.getMessage() != null) {
			data.setText(action.getMessage());
		}
		if (action instanceof Draft) {
			data.getGame().processDraft((Draft)action);
		} else if (action instanceof Attack) {
			data.getGame().processAttack((Attack)action);
		} else if (action instanceof SkipAttack) {
			data.getGame().processSkipAttack();
		} else if (action instanceof Fortify) {
			data.getGame().processFortify((Fortify)action);
		} else if (action instanceof EndTurn) {
			data.getGame().processEndTurn();
		} else if (action instanceof Forfeit) {
			data.getGame().processForfeit();
		} else {
			sendRequest(new ResendLast("Recieved an Action of an unknown type: " + action.getClass().getSimpleName() + "."));
		}
	}

	/**
	 * Processes a Message received from the server.
	 * If it was a GlobalMessage the message is added to the client's data object's global chat.
	 * Else if it was a LobbyMessage it is added to the lobby chat.
	 * Else if it was a DirectMessage it is added to the direct chat.
	 * Else the LoginResponse was not of the three above types a ResendLast is sent back to the server.
	 * @param message - The Message object to be processed
	 */
	public void processMessage(Message message) {
		if (message instanceof GlobalMessage) {
			data.addToGlobalChat(message.getMessage());
		} else if (message instanceof LobbyMessage) {
			data.addToLobbyChat(message.getMessage());
		} else if (message instanceof DirectMessage) {
			data.addToDirectChat(message.getMessage());
		} else {
			sendRequest(new ResendLast("Recieved an Message of an unknown type: " + message.getClass().getSimpleName() + "."));
		}
	}

	/**
	 * Processes a LoginResponse received from the server.
	 * If the received response was a AcceptLogin then the User object stored within the AcceptLogin object is stored in the client's data object such that it's content is is available to be viewed by the user.
	 * The view state is changed to the main view and the client is set to be waiting on nothing.
	 * Else if it was a DenyLogin this method does nothing except reset the client to be waiting on nothing.
	 * If the LoginResponse was not of the two above types a ResendLast is sent back to the server.
	 * @param loginResponse - The LoginResponse to be processed
	 */
	public void processLogin(LoginResponse loginResponse) {
		if (loginResponse instanceof AcceptLogin) {
			data.setUser(((AcceptLogin)loginResponse).getUser());
			data.setDesiredViewState(ClientGui.MAINVIEW);
			clientGui.getLoginRegisterView().clearTextFields();
			waitingOn = Client.NOTHING;
			sendRequest(new ServerData(""));
		} else if (loginResponse instanceof DenyLogin) {
			waitingOn = Client.NOTHING;
		} else {
			sendRequest(new ResendLast("Recieved a " + loginResponse.getClass().getSimpleName() + " was expecting a AcceptLogin or DenyLogin."));
		}
	}

	/**
	 * Logs the user out and returns the client to the login/register view.
	 * The client's data object is cleared of the user's information by calling the client's data object's clearAll() method such that it is ready to be populated with a new user's information without conflict.
	 * Finally the client is set to be waiting on nothing.
	 */
	public void logout() {
		data.setDesiredViewState(ClientGui.LOGINREGISTERVIEW);
		data.clearAll();
		waitingOn = Client.NOTHING;
	}

	/**
	 * Processes a ServerDataResponse received from the server.
	 * If the ServerDataResponse object contains no null entries then the client's data field's setServerData(ServerDataResponse serverData) method is invoked, sending the ServerDataResponse objects to it as an argument.
	 * Else if there was null entries then a ResendLast is sent back to the server.
	 * If the client was waiting on a ServerDataResponse then the client is set to be waiting on nothing.
	 * @param serverData - The ServerDataResponse to be processed
	 */
	public void processServerData(ServerDataResponse serverData) {
		if (serverData.getPlayersList() != null && serverData.getLobbyList() != null && serverData.getGameList() != null) {
			if (waitingOn.equals(Client.SERVERDATARESPONSE)) {
				waitingOn = Client.NOTHING;
			}
			data.setServerData(this, serverData);
		} else {
			sendRequest(new ResendLast("The recieved ServerData contained null entries."));
		}
	}

	/**
	 * Processes a JoinLobbyResponse received from the server.
	 * If the received response was a JoinLobbyAccept then the lobby data stored within the JoinLobbyAccpet object is stored in the client's data object such that it's content is is available to be viewed by the user.
	 * The view state is changed to the lobby view and the client is set to be waiting on nothing.
	 * Else if it was a JoinLobbyDeny this method does nothing except reset the client to be waiting on nothing.
	 * If the JoinLobbyResponse was not of the two above types a ResendLast is sent back to the server.
	 * @param joinLobbyResponse - The JoinLobbyResponse to be processed
	 */
	public void processJoinLobby(JoinLobbyResponse joinLobbyResponse) {
		if (joinLobbyResponse instanceof JoinLobbyAccept) {
			data.setMyLobby(((JoinLobbyAccept)joinLobbyResponse).getLobby());
			data.setDesiredViewState(ClientGui.LOBBYVIEW);
			waitingOn = Client.NOTHING;
		} else if (joinLobbyResponse instanceof JoinLobbyDeny) {
			waitingOn = Client.NOTHING;
		} else {
			sendRequest(new ResendLast("Recieved a " + joinLobbyResponse.getClass().getSimpleName() + " was expecting a JoinLobbyAccept or JoinLobbyDeny."));
		}
	}

	/**
	 * Processes a CreateLobbyResponse received from the server.
	 * If the received response was a CreateLobbyAccept then the lobby data stored within the CreateLobbyAccpet object is stored in the client's data object such that it's content is is available to be viewed by the user.
	 * The view state is changed to the create lobby view and the client is set to be waiting on nothing.
	 * Else if it was a CreateLobbyDeny this method does nothing except reset the client to be waiting on nothing.
	 * If the CreateLobbyResponse was not of the two above types a ResendLast is sent back to the server.
	 * @param createLobbyResponse - The CreateLobbyResponse to be processed
	 */
	public void processCreateLobby(CreateLobbyResponse createLobbyResponse) {
		if (createLobbyResponse instanceof CreateLobbyAccept) {
			data.setMyLobby(((CreateLobbyAccept)createLobbyResponse).getLobby());
			data.setDesiredViewState(ClientGui.CREATELOBBYVIEW);
			waitingOn = Client.NOTHING;
		} else if (createLobbyResponse instanceof CreateLobbyDeny) {
			waitingOn = Client.NOTHING;
		} else {
			sendRequest(new ResendLast("Recieved a " + createLobbyResponse.getClass().getSimpleName() + " was expecting a CreateLobbyAccept or CreateLobbyDeny."));
		}
	}

	/**
	 * Processes a NewGameResponse received from the server.
	 * The desired game type is not changed since it is set by default to "new game".
	 * The users' lobby is renamed to the requested name specified by the user.
	 * The view state is changed to the lobby view and the client is set to be waiting on nothing.
	 * @param newGameResponse - The NewGameResponse to be processed
	 */
	public void processNewGame(NewGameResponse newGameResponse) {
		data.getMyLobby().setName(newGameResponse.getGameName());
		data.setDesiredViewState(ClientGui.LOBBYVIEW);
		waitingOn = Client.NOTHING;
	}

	/**
	 * Processes a LoadGameResponse received from the server.
	 * If the received response was a LoadGameAccept then the Lobby object contained within the LoadGameAccept object is retreved and stored in the client's data object.
	 * The Lobby object contains the desired game type which will be set to "load game", the name of the loaded game and the actual game data, ready for when the game is started by the user.
	 * The view state is changed to the lobby view and the client is set to be waiting on nothing.
	 * Else if it was a LoadGameDeny this method does nothing except reset the client to be waiting on nothing.
	 * If the LoadGameResponse was not of the two above types a ResendLast is sent back to the server.
	 * @param loadGameResponse - The LoadGameResponse to be processed
	 */
	public void processLoadGame(LoadGameResponse loadGameResponse) {
		if (loadGameResponse instanceof LoadGameAccept) {
			data.setMyLobby(((LoadGameAccept)loadGameResponse).getLobby());
			data.setDesiredViewState(ClientGui.LOBBYVIEW);
			waitingOn = NOTHING;
		} else if (loadGameResponse instanceof LoadGameDeny) {
			waitingOn = NOTHING;
		} else {
			sendRequest(new ResendLast("Recieved a " + loadGameResponse.getClass().getSimpleName() + " was expecting a LoadGameAccept or LoadGameDeny."));
		}
	}

	/**
	 * Returns the client to the main view if they are in the lobby view or playing the game.
	 * In both cases their lobby is set to null, if they were in a game the game GUI is closed and set to null and their game is also set to null.
	 * The client is then set to be waiting on nothing.
	 */
	public void leaveLobby() {
		data.setDesiredViewState(ClientGui.MAINVIEW);
		data.setMyLobby(null);
		if (data.getGame() != null) {
			clientGui.stopGameGui();
			data.stopGame();
		}
		waitingOn = Client.NOTHING;
	}

	/**
	 * This method is only ever invoked if the client receives a KickGuestResponse when it wan't expecting one.
	 * It checks to see whether the user is in a lobby, whether there is a guest in the lobby and they are the guest of that lobby.
	 * If all of these conditions are met the client's leaveLobby() method is invoked.
	 * Else a ResendLast object is sent back to the server.
	 */
	public void kickedByHost() {
		if (data.getMyLobby() == null) { // Check to see if the user is in a lobby
			sendRequest(new ResendLast("Got a response of type KickGuestResponse when the user was not in a lobby."));
		} else if (data.getMyLobby().getGuest() == null) { // Check to see if there is a guest in the lobby
			sendRequest(new ResendLast("Got a response of type KickGuestResponse when there was no guest to kick."));
		} else if (!data.getUser().equals(data.getMyLobby().getGuest())) { // Check to see if the user is the guest of their lobby and hence can be kicked from it
			sendRequest(new ResendLast("Got a response of type KickGuestResponse when was not waiting for one and the user was the host of their lobby."));
		} else {
			leaveLobby();
		}
	}

	/**
	 * Processes a StartGameResponse received from the server.
	 * If the received response was a StartGameAccept then the game is started, the game GUI is constructed and the view state is changed to the game GUI.
	 * Else if it was a StartGameDeny this method does nothing except reset the client to be waiting on nothing.
	 * If the StartGameResponse was not of the two above types a ResendLast is sent back to the server.
	 * @param startGameResponse - The StartGameResponse to be processed
	 */
	public void processStartGame(StartGameResponse startGameResponse) {
		if (startGameResponse instanceof StartGameAccept) {
			data.startGame(this);
			clientGui.startGameGui();
			data.setDesiredViewState(ClientGui.RISKGUI);
			waitingOn = Client.NOTHING;
		} else if (startGameResponse instanceof StartGameDeny) {
			waitingOn = Client.NOTHING;
		} else {
			sendRequest(new ResendLast("Recieved a " + startGameResponse.getClass().getSimpleName() + " was expecting a StartGameAccept or StartGameDeny."));
		}
	}

	/**
	 * Converts an ArrayList&ltString&gt into a Game object.
	 * @return game - The game object created
	 * @author Matt Byrne and Oliver Kamperis
	 */
	public Game loadGame() {
		ArrayList<String> gameData = data.getMyLobby().getGameData();
		Player me;
		Player other;
		String hostName = gameData.get(1);
		String isHostTurn = gameData.get(2);		
		Boolean hostTurn = Boolean.parseBoolean(isHostTurn);		
		String[] hostCountriesAndTroops = gameData.get(3).split("\\s*,\\s*");
		String guestName = gameData.get(4);
		String isGuestTurn = gameData.get(5);
		Boolean guestTurn = Boolean.parseBoolean(isGuestTurn);
		String[] guestCountriesAndTroops = gameData.get(6).split("\\s*,\\s*");

		TreeMap<Country, Integer> hostTerritoriesOwned = new TreeMap<>();
		TreeMap<Country, Integer> guestTerritoriesOwned = new TreeMap<>();

		for (int i = 0; i < hostCountriesAndTroops.length - 1; i = i + 2) {
			Country c1 = Map.findCountryObject(hostCountriesAndTroops[i]);
			hostTerritoriesOwned.put(c1, Integer.parseInt(hostCountriesAndTroops[i+1]));
		}

		for (int i = 0; i < guestCountriesAndTroops.length - 1; i = i + 2) {
			Country c2 = Map.findCountryObject(guestCountriesAndTroops[i]);
			guestTerritoriesOwned.put(c2, Integer.parseInt(guestCountriesAndTroops[i+1]));
		}
		me = new Player(hostName, true, hostTurn, this, hostTerritoriesOwned);
		other = new Player(guestName, false, guestTurn, this, guestTerritoriesOwned);
		return new Game(me, other);
	}


	/**
	 * Converts into a Game object into an ArrayList&ltString&gt in a format that can be stored in the database.
	 * And then sends it to the server as part of a SaveGame request.
	 * @author Matt Byrne and Oliver Kamperis
	 */
	public void saveGame() {
		Game game = data.getGame();
		ArrayList<String> savedGame = new ArrayList<>(); // Create an arrayList to store the saved game
		Set<Country> gameCountries = game.getGameMap().getMapSet(); // Store the set of countries in the game.		
		StringBuffer countriesOwnedHost = new StringBuffer(); // StringBuffer for countries owned by the host
		StringBuffer countriesOwnedGuest = new StringBuffer(); // StringBuffer for countries owned by the guest		
		Player host, guest;		
		
		if (game.getMe().isHost()) {
			
			host = game.getMe(); // Player object of the user on this machine
			guest = game.getOther(); //Player object of the user on remote machine
			
		} else {
			host = game.getOther();
			guest = game.getMe();		
		}
	
		String hostName = host.getUserID(); // Host username
		String guestName = guest.getUserID();
		String gameName = data.getMyLobby().getName();
		savedGame.add(0, gameName); // Add game name to the list
		savedGame.add(1, hostName); // Add the hostName to the arrayList at position 1
		boolean hostTurn = host.IsTurn();
		savedGame.add(2, String.valueOf(hostTurn)); //add a boolean to indicate whether it's the hosts turn

		for (Country country: gameCountries) {
			if (host.getOwnedTerritories().containsKey(country)) { //if hosts map of countries contains a Key for the current country 
				// add the country name along with the number of troops to the StringBuffer for the host.

				countriesOwnedHost.append(country.getCountryName() + ", ");	
				countriesOwnedHost.append(Integer.toString(host.getOwnedTerritories().get(country))+ ", ");
			}				
		}	
		if (countriesOwnedHost.length() != 0) {
			countriesOwnedHost.setLength(countriesOwnedHost.length() - 2); //remove last comma from the StringBuffer
		}
		savedGame.add(3, countriesOwnedHost.toString());  //add the String with all of the countries names and troops allocated, to the arrayList.
		savedGame.add(4, guestName); //add the guestName at position 3 in the arrayList		
		boolean guestTurn = guest.IsTurn(); 
		savedGame.add(5, String.valueOf(guestTurn)); //convert boolean value to a string indicating whether it's the guest turn or not

		for (Country country: gameCountries) { 
			if(guest.getOwnedTerritories().containsKey(country)){ //if map with guest territories contains the current country as a key then append the country name and troops allocated to the list.
				countriesOwnedGuest.append(country.getCountryName() + ", "); //append country name to StringBuffer
				countriesOwnedGuest.append(Integer.toString(guest.getOwnedTerritories().get(country)) +  ", "); //append number of troops in country to StringBuffer
			}
		}

		if (countriesOwnedGuest.length() != 0) {
			countriesOwnedGuest.setLength(countriesOwnedGuest.length()-2); //remove last comma from the StringBuffer 
		}
		savedGame.add(6, countriesOwnedGuest.toString()); //add countrieOwned by guest and troops in those countries to the arrayList
		sendRequest(new SaveGame("", savedGame));
	}

	/**
	 * Sends the specified Request object to the server on the client's ObjectOutputStream.
	 * Invokes the client's disconnected(String stackTrace) method if an IOException is thrown whilst attempting to send the Action object.
	 * This method will only send the request if the request is of type ResendLast or the client is not currently waiting for a response.
	 * It will also automatically set the client to be waiting on the correct response type for the request type being sent.
	 * Invokes the client's crash(String stackTrace) method if the request type could not be determined whilst attempting to send the Request object.
	 * @param request - The Request object to be sent
	 */
	public void sendRequest(Request request) {
		try {
			if (request instanceof ResendLast || waitingOn.equals(Client.NOTHING)) {
				switch (request.getClass().getSimpleName()) {
				case "Login":
					waitingOn = Client.LOGINRESPONSE;
					break;
				case "Logout":
					waitingOn = Client.LOGOUTRESPONSE;
					break;
				case "Register":
					waitingOn = Client.REGISTERRESPONSE;
					break;
				case "ServerData":
					waitingOn = Client.SERVERDATARESPONSE;
					break;
				case "JoinLobby":
					waitingOn = Client.JOINLOBBYRESPONSE;
					break;
				case "CreateLobby":
					waitingOn = Client.CREATELOBBYRESPONSE;
					break;
				case "NewGame":
					waitingOn = Client.NEWGAMERESPONSE;
					break;
				case "LoadGame":
					waitingOn = Client.LOADGAMERESPONSE;
					break;
				case "LeaveLobby":
					waitingOn = Client.LEAVELOBBYRESPONSE;
					break;
				case "KickGuest":
					waitingOn = Client.KICKGUESTRESPONSE;
					break;
				case "StartGame":
					waitingOn = Client.STARTGAMERESPONSE;
					break;
				case "SaveGame":
					waitingOn = Client.SAVEGAMERESPONSE;
					break;
				case "ResendLast":
					break;
				case "Result":
					break;
				default:
					crash("Whilst attempting to send a request to the server the request type could not be determined"
							+ "and therefore the waitingOn variable cannot be set which could lead to unexpected errors.");
				}

				objOut.writeObject(request);

			} else {
				data.setText("Cannot send another request at this time as the client is currently waiting for a response of type: " + waitingOn 
						+ ". Please wait a moment and try again.");
			}
		} catch (IOException e) {
			disconnected("Whilst attempting to send a Request of type: " + request.getClass().getSimpleName()
					+ "\n" + "With message: " + request.getMessage()
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * Sends the specified Action object to the server on the client's ObjectOutputStream.
	 * Invokes the client's disconnected(String stackTrace) method if an IOException is thrown whilst attempting to send the Action object.
	 * @param action - The Action object to be sent
	 */
	public void sendAction(Action action) {
		try {
			objOut.reset();
			objOut.writeObject(action);
		} catch (IOException e) {
			disconnected("Whilst attempting to send a Action of type: " + action.getClass().getSimpleName()
					+ "\n" + "With message: " + action.getMessage()
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * Sends the specified Message object to the server on the client's ObjectOutputStream.
	 * Invokes the client's disconnected(String stackTrace) method if an IOException is thrown whilst attempting to send the Message object.
	 * @param message - The Message object to be sent
	 */
	public void sendMessage(Message message) {
		try {
			objOut.writeObject(message);
		} catch (IOException e) {
			disconnected("Whilst attempting to send a Message of type: " + message.getClass().getSimpleName()
					+ "\n" + "With message: " + message.getMessage()
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * This method causes the Client to close due to being disconnected from the server displaying a message explaining what caused the disconnect and then closing the program after 10 seconds.
	 * This method is only ever invoked when an IOException is thrown, it is also crucially invoked if the ponging thread throws a StoppedPingingException.
	 * @param stackTrace - The reason why the disconnect occurred
	 */
	public void disconnected(String stackTrace) {
		data.setText("The client has disconnected from the server and must close:" + "\n" + stackTrace);
		System.out.println("The client has disconnected from the server and must close:" + "\n" + stackTrace);
		try { Thread.sleep(10000); } catch (InterruptedException e) { }
		System.exit(0);
	}

	/**
	 * This method causes the Client to "crash" displaying a message explaining what caused the crash and then closing the program after 10 seconds.
	 * This method is only ever invoked in certain places within the code for the Client and ClientGui where an unexpected condition has arisen in the running of the code.
	 * It is only invoked when this erroneous condition could only have occurred due to a fault on the client side.
	 * These are conditions that should never arise if all the code is correct however is implemented for means of debugging the program.
	 * If it was not implemented then serious problems could arise when testing the code that could, in the worst case, cause issues on the server and database.
	 * It is therefore a fail safe.
	 * @param stackTrace - The reason why the crash occurred
	 */
	public void crash(String stackTrace) {
		data.setText("The client has encountered a fatal error and must close:" + "\n" + stackTrace);
		System.out.println("The client has encountered a fatal error and must close:" + "\n" + stackTrace);
		try { Thread.sleep(10000); } catch (InterruptedException e) { }
		System.exit(-1);
	}

	/**
	 * Sends an Request of type Authentification to the server and waits up to 2 seconds for a response.
	 * @return The Response object received from the server
	 * @throws AuthentificationFailException - If the authentification failed for any reason
	 */
	public Response sendAuthentification() throws AuthentificationFailException {
		try {
			objOut.writeObject(new Authentification(Client.AUTHENTIFICATIONCODE));
		} catch (IOException e) {
			throw new AuthentificationFailException("An IOException was thrown by the underlying ObjectOutputStream.", e);
		}

		try {
			socket.setSoTimeout(2000);
		} catch (SocketException e) {
			throw new AuthentificationFailException("An error occured in the underlying protocol.", e);
		}

		Object response;
		try {
			response = objIn.readObject();
		} catch (SocketTimeoutException e) {
			throw new AuthentificationFailException("Did not receive a response from the server.", e);
		} catch (ClassNotFoundException e) {
			throw new AuthentificationFailException("The received object was of an unknown, non-serializable or primitive type.", e);
		} catch (IOException e) {
			throw new AuthentificationFailException("An IOException was thrown by the underlying ObjectInputStream.", e);
		}

		if (response instanceof Response) {
			return (Response)response;
		} else {
			throw new AuthentificationFailException("The received object was of an unexpected type.");
		}
	}

	public String getServer() {
		return this.server;
	}

	public int getPort() {
		return this.port;
	}

	public Data getData() {
		return this.data;
	}

	public ClientGui getClientGui() {
		return clientGui;
	}

	public String getWaitingOn() {
		return waitingOn;
	}

	/**
	 * Main method that runs the client.
	 * If args.length == 2 then the client will attempt to connect to the host name specified by the first argument and the port specified by the second.
	 * Else it will by default connect to localHost on port 6000.
	 * @param args - A String array of input arguments
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 2) {
				new Client(args[0], Integer.parseInt(args[1])).start();
			} else {
				new Client("localhost", 6000).start();
			}
		} catch (AuthentificationFailException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
