package pingPongServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LocalServer extends Thread {
	private int port;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	
	public LocalServer(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			clientSocket = serverSocket.accept();
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			System.out.println("Client connected from: " + serverSocket.getInetAddress());
			
			this.startPinging(clientSocket, out, in);
			
		} catch (SocketException e) {
			System.out.println("An error occured in the underlying protocol.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occured whilst creating the socket on port " + port + ". Or an error occured whilst waiting for a connection on the socket.");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("There is no such port: " + port + ". Port number must be between 0 and 65535, inclusive.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("The server thread was interrupted!");
			e.printStackTrace();
		}
	}
	
	public void startPinging(Socket socket, DataOutputStream out, DataInputStream in) throws InterruptedException, SocketException {
		String lineIn = null;
		int timeouts = 1;
		socket.setSoTimeout(5000); //sets the timeout period, i.e. how long the input stream will wait intil throwing a SocketTimeoutException
		double sent = 0;
		double received = 0;
		
		try {
			while (true) {
				try {
					out.writeUTF("ping");
					sent = System.nanoTime();
					System.out.println("Sent ping");
					lineIn = in.readUTF();
					
					if (lineIn.equals("pong")) {
						received = System.nanoTime();
						System.out.println("Received pong after " + (received - sent)/1000000000 + "seconds");
					} else {
						if (timeouts < 4) {
							System.out.println("Unexpected response from client: " + lineIn + ", resending ping");
							timeouts ++;
						} else {
							System.out.println("Too many unexpected responses from client, terminating connection!");
							try { socket.close(); } catch (IOException e) {}
							return;
						}
					}
				} catch (SocketTimeoutException t) {
					if (timeouts < 4) {
						System.out.println("No response from client, resending ping, attempt: " + timeouts);
						timeouts ++;
						continue;
					} else {
						System.out.println("The client has timed out after " + timeouts + " attempts, terminating connection!");
						socket.close();
						return;
					}
				}
				
				Thread.sleep(5000);
			}
		} catch (IOException e) {
			System.out.println("An error occured whilst attempting to ping the client, either the socket was unexpectedly closed or the port became unavailable.");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Thread thread = new LocalServer(6000);
		thread.start();
	}
}
