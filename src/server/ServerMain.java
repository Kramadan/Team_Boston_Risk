package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import communicationObjects.Accept;
import communicationObjects.AcceptLogin;
import communicationObjects.Authentification;
import communicationObjects.Deny;
import communicationObjects.DenyLogin;
import communicationObjects.Lobby;
import communicationObjects.Login;
import communicationObjects.LoginResponse;
import communicationObjects.Register;
import communicationObjects.RegisterResponse;
import communicationObjects.ResendLast;
import communicationObjects.Response;
import communicationObjects.ServerData;
import communicationObjects.ServerDataResponse;
import communicationObjects.User;
import database.DatabaseManager;

/**
 *
 * To start the server this script must be executed. For the application to work correctly the server must be started first, before any clients.
 * The ServerMain class is a constantly listening for new client connections. Once a client connects an instance of ClientThread inner class is created. ClientThyread
 * impliments runnable, and is run as a separate thread. The number of client threads have been limited to 18. The check to see if the maximum number of clientThreads
 * are running occurs during authentification.
 * 
 * @author Mark Alston
 * @version 2017-03-17
 */
public class ServerMain extends Observable {

	//Field Variables
	int port = 6000;
	int portForPing = 6001;
	ServerSocket pingSocket;
	ServerSocket sSocket;
	DatabaseManager d1;
	ServerDataCollection serverDataCollection;
	
	//public variable
	public static ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
	/**
	 * Server Main constructor creates a thread pool of size 2. As a client requests a connection with the post a new socket is allocated and the new thread in the tread pool is executed.
	 * If the thread pool is full, then any further requests will remain in a queue until the run() methods on the 2 currently executing threads terminates.
	 */
	public ServerMain() {
		//Establish a database connection
		this.d1 = new DatabaseManager();
		//Establish the serverDataCollection information
		this.serverDataCollection = new ServerDataCollection(new ArrayList<Lobby>() , new ArrayList<User>(), new ArrayList<Lobby>());
		//create the ServerDataGui
		ServerDataGUI window = new ServerDataGUI(this);
		addObserver(window);
		//Create Thread pool waiting to accept new clients
		ExecutorService tasks = Executors.newFixedThreadPool(100);
		
		try {
			//Create socket for new client
			sSocket = new ServerSocket(port);
			pingSocket = new ServerSocket(portForPing);
			while(true) {
				//Accept a requesting client
				Socket socket = sSocket.accept();
				System.out.println("Client connected from "+ socket.getInetAddress());
				//execute the client thread and add it to the serverDataCollection observers list
				ClientThread client = new ClientThread(socket, pingSocket, this);
				this.serverDataCollection.addObserver(client);
				tasks.execute(client);
				ServerMain.threads.add(client);
				this.setChanged();
				this.notifyObservers();
			}
		} catch(IOException e) {
			System.out.println("Socket not succesfully made for new client!");
		}
	}
	
	/**
	 * ClientThread class runs for each connected client simultaneously. It has 3 main sections; 1) Authentificate 2) Log in 3) Listen for other input.
	 * The Client will not be authentificated if there are already 18 logged in players. ClientThread, works with the ServerPing to keep an eye on if the 
	 * client is still connected. If in the case the ping pong'ing terminates the ClientThread will end, and call a cleanup method ServerDataCollection.removeUser().
	 * This method removes the client from all games, lobbies, and logged in players list. All clients who have an active thread, are stored in the ArrayList
	 * ServerMain.threads of type ClientThreads.
	 * @author Mark Alston
	 * @version 2017-03-17
	 */
	public class ClientThread  implements Runnable, Observer {
		
		//field variables
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private int pingPongPort;
		private ServerSocket pingSocket;
		private User user;
		private ServerMain serverMain;
		
		//constructor
		public ClientThread(Socket socket, ServerSocket pingSocket, ServerMain serverMain) {
			try {
				Random random = new Random();
				this.user = new User(0, "" + random.nextInt(100), 0,0,0);
				this.in = new ObjectInputStream(socket.getInputStream());
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.pingPongPort = 6001;
				this.pingSocket = pingSocket;
				this.serverMain = serverMain;
			} catch(IOException e) {
				System.err.println("input/output sream unable to be created");
			}
		}
		
		private ServerSocket getPingSocket() {
			return this.pingSocket;
		}
		
		private ObjectInputStream getIn() {
			return this.in;
		}
		
		public ObjectOutputStream getOut() {
			return this.out;
		}
		
		public int getPingPongPort() {
			return this.pingPongPort;
		}
		
		public User getUser() {
			return this.user;
		}
		
		public void setUser(User user) {
			this.user=user;
		}
		
		
		@Override
		public boolean equals(Object args0) {
			if(this.user != null && args0 instanceof ClientThread && ((ClientThread) args0).user != null) {
				return this.user.getUsername().equals(((ClientThread) args0).user.getUsername());
			} else {
				return false;
			}
		}
		
		@Override
		/*
		 * Parent thread for each user. Initiates login, and controls the existence of all children threads. In the case of a loss of connection
		 * this thread will terminate all child threads.
		 */
		public void run() {
			
			//Get input / output streams
			ObjectInputStream in = this.getIn();
			ObjectOutputStream out = this.getOut();
			
			
			/* ******************************************* *
			 *  Authentification between client and server *
			 * ******************************************* */
			
			try {
				boolean authentificationSuccesful = authentificate(in,out);
				//respond to client with appropriate message
				if(authentificationSuccesful == true) {
					if(ServerMain.threads.size() < 19) {
						Response r = new Accept("Succesful connection made to the server");
						out.writeObject(r);
					} else {
						Response r = new Deny("Sorry, there are currently to many players connected to the server.");
						out.writeObject(r);
						return;
					}
				} else {
					Response r = new Deny("Sorry the authentification key was incorrect");
					out.writeObject(r);
					return;
				}
			} catch(SocketException e) {
				System.err.println("Socket Timed out during Authentification between client and server");
			} catch(IOException e) {
				System.err.println("objects on output stream unable to be sent during authentification");
			}
				
			
			/* *********************** *
			 * START THE PING PONG'ING *
			 * *********************** */
			Thread pingPong = new ServerPing(this.getPingSocket());
			pingPong.start();
			
			
			/*
			 * This while loop is labled. If errors occur the while loop is terminated using a 'break loop;'. However if no errors occur, and the user
			 * selects logout, they will be returned to the top of the loop ready to be logged in again.
			 */
			loop:
			while(true) {
				
				/* ********************* *
				 * Login / Register Loop *
				 * ********************* */
				
				//if the login details are incorrect, the user can have another go.
				incorrectLoginLoop:
				while(true) {
				
					//Read in object...
					Object m = null;
					
					try {
						m = in.readObject();
					} catch (ClassNotFoundException e) {
						break loop;
					} catch(IOException e) {
						serverDataCollection.removeUser(user, in, out, "Closed Window before login");
						this.serverMain.setChanged();
						this.serverMain.notifyObservers();
						System.out.println("A user has been disconected from the server before login");
						break loop;
					}
					
					try {
						/*
						 * Check if the user is selecting login or register...
						 */
						if(m instanceof Login) {
							
							//check login details are correct
							User aUser = d1.checkLogin(((Login) m).getUserName(), ((Login) m).getPassword());
							
							/*
							 * Add the user to the logged in players list, as long as they have legitimate login details
							 * and they are not already currently logged in. If this is all correct then a acceptLogin object
							 * is sent back the user.
							 */
							if(aUser.isUser() && NotLoggedIn(((Login) m).getUserName())) {
								this.setUser(aUser);
								LoginResponse r = new AcceptLogin(this.getUser().getMessage(),new User(this.getUser().getUserId(), this.getUser().getUsername(), this.getUser().getNumberOfGamesPlayed(), this.getUser().getNumberOfLosses(), this.getUser().getNumberOfWins()));
								out.writeObject(r);
								serverDataCollection.addUser(user);
								try {
									Object requestForData = in.readObject();
									
									if(requestForData instanceof ServerData) {
										out.writeObject(
												new ServerDataResponse(
														"Welcome " + this.getUser().getUsername(),serverDataCollection.getGameList(), serverDataCollection.getPlayersList(), serverDataCollection.getLobbyList()));
									} else {
										System.err.println("Unknown object sent by " + user.getUsername() + ". Expecting ServerData object");
									}
									
								} catch(ClassNotFoundException e) {
								}
								break incorrectLoginLoop;
								/*
								 * Otherwise if already logged in, or details are incorrect then DenyLogin object sent back.
								 */
							} else {
								if(NotLoggedIn(((Login) m).getUserName()) == false) {
									aUser.setMessage("This user is already logged in");
									LoginResponse r = new DenyLogin(aUser.getMessage());
									out.writeObject(r);
								} else {
									LoginResponse r = new DenyLogin(aUser.getMessage());
									out.writeObject(r);
								}
							}
					
						/*
						 * If the user clicks register... 
						 */
						} else if(m instanceof Register) {
					
							User aUser = d1.register(((Register) m).getUserName(), ((Register) m).getPassword());
							RegisterResponse r = new RegisterResponse(aUser.getMessage());
							out.writeObject(r);
							
						/*
						 * Will be sent after clean up method is run if client doesn't terminate.
						 */
						} else if(m instanceof ResendLast) {
							//Do nothing let the loop run again!
						/*
						 * If the recieved object is neither login or register, then an error must have
						 * occured and the client is terminated.	
						 */
						} else {
							System.out.println(m.getClass().getSimpleName());
							System.err.println("Unrecognised object sent on the data stream! Thread Terminated!");
							break loop;
						}
						
					} catch(IOException e) {
						System.err.println("objects on output stream cannot be sent during login / registration");
						e.printStackTrace();
					}
				}
				
			
				/* ****************************** *
				 * Where the game logic is passed *
				 * ****************************** */
				
				boolean logout = false;
			
				pingPong:	
				while(pingPong.isAlive()) {
					logout = ServerLogic.checkForInput(in,out,serverDataCollection, user, d1);
					
					if(logout) {
						System.out.println("Session ended with: " + user.getUsername());
						serverDataCollection.removeUser(user,in,out,"Logout");
						//exit the run while loop
						break pingPong;
					}
				}
				
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
				}
				
				
				/* ******** *
				 * CLEAN UP *
				 * ******** */
				
				if(pingPong.isAlive() == false || logout == false) {
					serverDataCollection.removeUser(user, in, out, "Closed the window");
					this.serverMain.setChanged();
					this.serverMain.notifyObservers();
					break loop;
				}
				
			}
			
			/* ********************************************************************* *
			 * CLEAN UP - in the event the while loop 'loop:' terminated prematurely *
			 * ********************************************************************* */
			try {
				serverDataCollection.removeUser(user, in, out, "Closed the window");
				this.serverMain.setChanged();
				this.serverMain.notifyObservers();
				in.close();
				out.close();
				System.out.print("Session ended with: " + user.getUsername());
			} catch (IOException e) {
			} catch (NullPointerException e) {
			}
		}

		@Override
		/**
		 * Send every client an update of the ServerData. This includes a list of all players, and lobbies. 
		 */
		public void update(Observable o, Object arg) {
			if(user != null) {
				try {
					ObjectOutputStream out = this.getOut();
					ServerDataResponse serverDataResponse = new ServerDataResponse(
							"New Players and Lobby Info...",serverDataCollection.getGameList(), serverDataCollection.getPlayersList(), serverDataCollection.getLobbyList());
					out.writeObject(serverDataResponse);
					out.reset();
					System.out.println("The update method has succesfully sent new ServerData to the user: " + user.getUsername());
				} catch(IOException e) {
				}
			}
			this.serverMain.setChanged();
			this.serverMain.notifyObservers();
		}
	}
	

	/**
	 * method authenticate checks a shared secret between the server and client. If the shared secret / key matches then the server accepts
	 * the clients connection.
	 * @param in - the clients input stream
	 * @param out - the clients output stream
	 * @return true / false - depending on if the shared secret matches or not.
	 */
	public static boolean authentificate(ObjectInputStream in, ObjectOutputStream out) throws SocketException {
		
		Authentification key;
		
		try {
			
			key = (Authentification) in.readObject();
			/*
			 * EXPECTING AN AUTHENTIFICATION MESSAGE "IAMTEAMBOSTONRISKCLIENT" 
			 */
			if(key.getMessage().equals("IAMTEAMBOSTONRISKCLIENT")) {
				return true;
			} else {
				return false;
			}
			
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException - authentificate method (Server Main)");
			return false;
		} catch (IOException e) {
			System.err.println("IOException - authentificate method (Server Main)");
			return false;
		}
	}
	
	/**
	 * Returns false if this user is already logged in. True if user not already logged in.
	 * @param username
	 * @return true/ false - true if the user is NOT logged in and False if the user is logged in.
	 */
	public boolean NotLoggedIn(String username) {
		for(int i=0 ; i<threads.size() ; i++) {
			if(threads.get(i).getUser()==null) {
				//continue
			}else if(threads.get(i).getUser().getUsername().equals(username)) {
				return false;
			}
		}
		return true; 		
	}

	/*
	 * Main method. This is the parent server class and all should be started by running this main method first!
	 */
	public static void main(String[] args) {
		new ServerMain();
	}

}