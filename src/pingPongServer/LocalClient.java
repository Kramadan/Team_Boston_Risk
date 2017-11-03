package pingPongServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class LocalClient {
	private static int PORT = 6000;
	private static String SERVER = "localhost";
	
	public LocalClient() {
		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		
		try {
			socket = new Socket(SERVER, PORT);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			System.out.println("Connected to server: " + SERVER);
			
			startPonging(socket, out, in);
			
		} catch (IOException e) {
			System.out.println("No access to: " + SERVER);
		}
	}
	
	public void startNoPonging(Socket socket, DataOutputStream out, DataInputStream in) { while(true) {} }
	
	public void startPonging(Socket socket, DataOutputStream out, DataInputStream in) throws IOException {
		String lineIn = null;
		socket.setSoTimeout(15000);
		int timeouts = 1;
		double sent = 0;
		double received = 0;
		
		try {
			System.out.println("Waiting for initial ping");
			lineIn = in.readUTF();
			if (lineIn.equals("ping")) {
				System.out.println("Received initial ping");
			} else {
				System.out.println("Received an unexpected message from server, terminating connection!");
				socket.close();
				return;
			}
		} catch (SocketTimeoutException t) {
			System.out.println("Did not receive a ping from the server, terminating connection!");
			socket.close();
			return;
		}
		
		socket.setSoTimeout(10000);
		
		while (true) {
			try {
				out.writeUTF("pong");
				sent = System.nanoTime();
				System.out.println("Sent pong");
				lineIn = in.readUTF();
				
				if (lineIn.equals("ping")) {
					received = System.nanoTime();
					System.out.println("Received ping after " + (received - sent)/1000000000 + "seconds");
				} else {
					if (timeouts < 4) {
						System.out.println("Unexpected response from server: " + lineIn + ", resending pong, attempt: " + timeouts);
						timeouts ++;
					} else {
						System.out.println("Too many unexpected responses from server, terminating connection!");
						socket.close();
						return;
					}
				}
				
			} catch (SocketTimeoutException t) {
				if (timeouts < 4) {
					System.out.println("No response from server, resending pong, attempt: " + timeouts);
					timeouts ++;
				} else {
					System.out.println("The server has timed out after " + timeouts + " attempts, terminating connection!");
					socket.close();
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new LocalClient();
	}
}
