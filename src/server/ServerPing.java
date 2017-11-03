package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
/**
 * ServerPing checks to see if the server and client are connected. In the event that they disconnect the thread ServerPing will be terminated and the ServerMain will end
 * it's connection with the client.
 * @author Oliver Kamperis
 * @author Mark Alston
 */
public class ServerPing extends Thread {
	//field variables
	private ServerSocket pingSocket = null;
	private Socket clientSocket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private boolean gameStillRunning;
	
	//Constructor
	public ServerPing(ServerSocket pingSocket) {
		this.pingSocket = pingSocket;
		this.gameStillRunning = true;
	}
	
	//getters / setters
	private ServerSocket getPingSocket() {
		return this.pingSocket;
	}
	
	public boolean getGameStillRunning() {
		return this.gameStillRunning;
	}
	
	public void setGameStillRunning(boolean gameStillRunning) {
		this.gameStillRunning = gameStillRunning;
		System.out.println("PingPong Died!");
	}
	
	@Override
	public void run() {
		try {
			clientSocket = this.getPingSocket().accept();
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			
			this.startPinging(clientSocket, out, in);
			
		} catch (SocketException e) {
			this.cleanUp();
		} catch (IOException e) {
			this.cleanUp();
		} catch (IllegalArgumentException e) {
			this.cleanUp();
		} catch (InterruptedException e) {
			this.cleanUp();
		}
	}
	
	/**
	 * Closes all conections and 
	 */
	private void cleanUp() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
		}
	}

	public void startPinging(Socket socket, DataOutputStream out, DataInputStream in) throws InterruptedException, SocketException {
		String lineIn = null;
		int timeouts = 1;
		socket.setSoTimeout(5000); //sets the timeout period, i.e. how long the input stream will wait until throwing a SocketTimeoutException
		double sent = 0;
		double received = 0;
		
		try {
			loop:
			while (this.getGameStillRunning()) {
				try {
					out.writeUTF("ping");
					sent = System.nanoTime();
					lineIn = in.readUTF();
					
					if (lineIn.equals("pong")) {
						received = System.nanoTime();
					} else {
						if (timeouts < 4) {
							timeouts ++;
						} else {
							try { socket.close(); } catch (IOException e) {}
							this.setGameStillRunning(false);
							return;
						}
					}
				} catch (SocketTimeoutException t) {
					if (timeouts < 4) {
						timeouts ++;
						continue;
					} else {
						socket.close();
						this.setGameStillRunning(false);
						return;
					}
				}
				
				Thread.sleep(400);
			}
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
			}
		}
	}
}
