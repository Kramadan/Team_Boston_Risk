package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * public class PongingThread extends Thread.
 * This class runs a simple thread that listens for pings from a server on a given host name and port number combination and sends pongs back.
 * If the thread stops receiving pings after a specified number of retries then it disconnects the client.
 * @author Oliver Kamperis
 */
public class PongingThread extends Thread {
	private final Client client;
	private final Socket socket;
	private final DataOutputStream out;
	private final DataInputStream in;
	
	/**
	 * Constructs a new Ponging thread for the specified client.
	 * Connecting to the same host name as the client and the port number + 1.
	 * @param client - The client to create the PongingThread for
	 * @throws IOException - If any IOExceptions are thrown whilst connecting to the server
	 */
	public PongingThread(Client client) throws IOException {
		this.client = client;
		
		try {
			this.socket = new Socket(client.getServer(), client.getPort() + 1);
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			throw new IOException("The IP address of the host " + client.getServer() + " could not be determined.", e);
		} catch (IllegalArgumentException e) {
			throw new IOException("There is no such port: " + (client.getPort() + 1) + ". Port number must be between 0 and 65535, inclusive.", e);
		} catch (IOException e) {
			throw new IOException("An unknown IOException occured whilst creating the socket on port " + client.getServer() + " or whilst creating the output or input streams.", e);
		}
		
		this.start();
	}
	
	/**
	 * Run method that simply calls the startPonging() method and waits for a StoppedPingingException to be thrown by the method.
	 * When/if it is thrown the client's disconnected(String stackTrace) method is called.
	 */
	@Override
	public void run() {
		try {
			startPonging();
		} catch (StoppedPingingException e) {
			client.disconnected("The client stopped recieving pings from the server and the Ponging thread has terminated." + "\n" + e.getMessage());
		}
	}
	
	/**
	 * Listens for pings from the server and sends pongs back immediately after recieving a ping.
	 * Initially waits up to 15 seconds for an initial ping, which if it doesn't receive throws a StoppedPingingException.
	 * After the initial ping the method will wait up to 5 seconds for the next ping.
	 * If it does not recieve 3 pings in a row then it throws a StoppedPingingException.
	 * If any Socket or IO Exceptions are thrown the method throws a StoppedPingingException.
	 * @throws StoppedPingingException - If for whatever reason ping/ponging between the server/client stops
	 */
	public void startPonging() throws StoppedPingingException {
		try {
			String lineIn;
			socket.setSoTimeout(15000);
			int timeouts = 1;
			
			try {
				lineIn = in.readUTF();
				if (lineIn.equals("ping")) {
				} else {
					socket.close();
					throw new StoppedPingingException("Received an unexpected message from server.");
				}
			} catch (SocketTimeoutException e) {
				socket.close();
				throw new StoppedPingingException("Did not receive an initial ping from the server.", e);
			}
			
			socket.setSoTimeout(5000);
			
			while (true) {
				try {
					out.writeUTF("pong");
					lineIn = in.readUTF();
					
					if (lineIn.equals("ping")) {
						timeouts = 1;
					} else {
						if (timeouts < 4) {
							timeouts ++;
						} else {
							socket.close();
							throw new StoppedPingingException("Too many unexpected responses from server.");
						}
					}
					
				} catch (SocketTimeoutException e) {
					if (timeouts < 4) {
						timeouts ++;
					} else {
						socket.close();
						throw new StoppedPingingException("The server has timed out after " + timeouts + " attempts.", e);
					}
				}
			}
		} catch (SocketException e) {
			throw new StoppedPingingException("An error occured in the underlying protocol.", e);
		} catch (IOException e) {
			throw new StoppedPingingException("An error occured whilst attempting to ping the client, either the socket was unexpectedly closed or either of the input or output streams were unexpectedly closed or the port became unavailable.", e);
		}
	}
}
