package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import common.*;

public class Server implements Runnable {
	private static ServerSocket servSock; // server has a ServerSocket
	private static ArrayList<String> configs = new ArrayList<>(); // server holds a list of configs
	private static Matrix m; // server holds a matrix of 10x10 containing 3 planes
	private static List<String> lstClients = new ArrayList<String>(); // server holds a list of connected clients
	private static String currentUser; // server retains currentUser who shot the planes

	public static ServerSocket getServSock() {
		return servSock;
	}

	public static void setServSock(ServerSocket servSock) {
		Server.servSock = servSock;
	}

	public static ArrayList<String> getConfigs() {
		return configs;
	}

	public static void setConfigs(ArrayList<String> configs) {
		Server.configs = configs;
	}

	public static Matrix getM() {
		return m;
	}

	public static void setM(Matrix m) {
		Server.m = m;
	}

	public static List<String> getLstClients() {
		return lstClients;
	}

	public static void setLstClients(List<String> lstClients) {
		Server.lstClients = lstClients;
	}

	public static String getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(String currentUser) {
		Server.currentUser = currentUser;
	}

	public Server() {

	}

	public static String receiveCoordinates(Socket sock) throws Exception // receive coordinates
	{
		ObjectInputStream objectInputStream = new ObjectInputStream(sock.getInputStream()); // get the objectInputStream
																							// from the socket
		String received = (String) objectInputStream.readObject(); // read String object received from the client
		String values[] = received.split(","); // split received string ,
		Server.setCurrentUser(values[0]); // set currentUser with values[0] first value from the received after it is
											// splitted by ,
		int x = Integer.parseInt(values[1]); // get x coordinate from the client
		int y = Integer.parseInt(values[2]); // get y coordinate from the client
		@SuppressWarnings("static-access") // supress warnings
		String result = Server.m.shot(x, y); // create the result of the shot Matrix.shot return 1->plane hit,0->plane
												// not hit,X->plane killed

		return result; // return the result
	}

	public static void sendResult(Socket sock, String res) throws Exception // send result of shoting at coordinates
	{
		DataOutputStream dataOutputStream = new DataOutputStream(sock.getOutputStream()); // get the dataOutputStream
																							// from the socket
		dataOutputStream.writeUTF(res); // write with utf encoding the result of the shot
		dataOutputStream.flush(); // flush the dataOutputStream
	}

	public static void receiveUsername(Socket sock) throws Exception // receive username
	{
		DataInputStream dataInputStream = new DataInputStream(sock.getInputStream()); // get the dataInputStream from
																						// the socket
		String user = dataInputStream.readUTF(); // read with utf encoding the username received from the client

		Server.lstClients.add(user); // add username in list of cients
	}

	public static String getRandomConfig(ArrayList<String> lstConfigs) // get a new random config
	{
		Random r = new Random(); // define a Random
		int numberOfConfigs = configs.size(); // get number of configs
		int configNumber = r.nextInt(numberOfConfigs); // generate a randomInt between 0 and numberOfConfigs
		String randomConfigPath = lstConfigs.get(configNumber); // define the randomPath of randomConfig
		return randomConfigPath; // return path generated
	}

	public static void sendWinner(Socket sock) throws Exception // anounce the winner of the game
	{
		DataOutputStream dataOutputStream = new DataOutputStream(sock.getOutputStream()); // get the dataOutputStream
																							// from the socket
		dataOutputStream.writeUTF("Winner " + currentUser); // write with utf wncoding winner of the game
	}

	@SuppressWarnings("static-access")
	public static ArrayList<String> aquireConfigs(Matrix m) // compose paths of all configs amd add them in an ArrayList
	{
		ArrayList<String> configs = new ArrayList<String>(); // create an empty arrayList
		for (int i = 0; i < m.getNumberofconfigs(); i++) {
			String path = "src\\configs\\config" + i + ".txt"; // compose paths of the configs files
			configs.add(path); // add in collection the path
		}
		return configs; // return the collection
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while (!servSock.isClosed()) {
			try {
				Socket sock = servSock.accept(); // accept connections from clients
				Server.receiveUsername(sock); // receive the username and add it to list
				while (true) {
					if (Server.m.getNumberOfPlanes() == 0) // if planeNumber reach 0 the game is over
					{
						Server.sendWinner(sock); // server send the winner to client
						Server.setM(new Matrix(getRandomConfig(configs))); // reload a random config file
						Server.m.setNumberOfPlanes(3); // reset the numberOfPlanes
						String res = Server.receiveCoordinates(sock); // receive coordinates from client and retain the
																		// result of the shot in a string
						Server.sendResult(sock, res); // send result of the shot back to client
					} else {
						String result = Server.receiveCoordinates(sock); // receive coordinates from the client and
																			// retain the result in a string
						Server.sendResult(sock, result); // send the result back to client
					}
				}
			} catch (Exception e) {

			}
		}
	}

	public static void main(String[] args) throws Exception {
		Server.servSock = new ServerSocket(6349); // create a serverSocket on port 6349
		Server server = new Server(); // create a new server
		Thread t = new Thread(server); // create a new thread
		t.start(); // start the thdread
		System.out.println("Server On"); // display a message server on
		try {
			Server.setConfigs(Server.aquireConfigs(getM())); // set config list
			String randomPath = Server.getRandomConfig(Server.getConfigs()); // get a random path of a config file
			Server.setM(new Matrix(randomPath)); // first matrix is set with a random config file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}