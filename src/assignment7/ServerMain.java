package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class ServerMain extends Observable {
	private static ArrayList<String> usersServer = new ArrayList<String>();
	private int id = 1;
	private int channels =0;
	private int portNum = 4242;
	private String joined = "";
	
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		System.out.println("MY IP address is " + InetAddress.getLocalHost().getHostAddress());
		System.out.println("my port number is " + portNum);
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(portNum);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			
			joined = "1" + " User " + id + " has joined. ";
			usersServer.add("User " + id);
			for(String s: usersServer){
				joined+=s + " ";
			}
			setChanged();
			notifyObservers(joined);
			id++;
			System.out.println("got a connection");
		}
	}

	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					String cut[] = message.split(" ");
					if(cut[0].equals("GET_CHANNEL")){
						channels++;
						setChanged();
						notifyObservers("2 " + channels + " User " + cut[2] + " User " + cut[4]);	
					}
					else if(cut[0].equals("INVITATION_RESPONSE")){
						setChanged();
						notifyObservers("4 " + cut[1] + " User " + cut[3] + " User " + cut[5] + " " + cut[6]);	
						
					}
					else if(cut[0].equals("DISCONNECTED")){
						usersServer.remove("User " + cut[2]);
						setChanged();
						notifyObservers("5 User " + cut[2] + " has disconnected");
					}
					else{
						final String sendString = message;
						System.out.println("server read "+message);
						setChanged();
						notifyObservers(sendString);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
