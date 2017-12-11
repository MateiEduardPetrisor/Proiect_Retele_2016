package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import common.Coordinates;
import server.Server;

public class Client {
	private static String username; // each client has a name
	private static int numberOfPlanes; // each client must know the number of remaining planes

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Client.username = username;
	}

	public static int getNumberOfPlanes() {
		return numberOfPlanes;
	}

	public static void setNumberOfPlanes(int numberOfPlanes) {
		Client.numberOfPlanes = numberOfPlanes;
	}

	public static void sendCoordinates(Socket sock) throws Exception // send coordinates function
	{
		Coordinates c = new Coordinates(); // create an empty coordinates object
		InputStreamReader inputStreamReader = new InputStreamReader(System.in); // create an inputStreamReader from
																				// System.in->input from keyboard
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // create a bufferedReader
		System.out.print("X="); // print X=
		c.setxC(Integer.parseInt(bufferedReader.readLine())); // read and set x coordinate
		System.out.print("Y="); // print Y=
		c.setyC(Integer.parseInt(bufferedReader.readLine())); // read and set y coordinate

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(sock.getOutputStream()); // get
																								// objectOutputStream
																								// from socket
		objectOutputStream.writeObject(username + "," + c.toString()); // write an object of String to socket
		objectOutputStream.flush(); // flush the objectOutputStream
	}

	public static String receiveResult(Socket sock) throws Exception // receive result function
	{
		DataInputStream dataInputStream = new DataInputStream(sock.getInputStream()); // get the dataInputStream from
																						// socket
		String result = dataInputStream.readUTF(); // read utf from socket
		return "Result: " + result; // return Result:, + result
	}

	public static String readUsername() throws Exception // read username from keyboard function
	{
		System.out.print("Input Username: "); // display a message
		InputStreamReader inputStreamReader = new InputStreamReader(System.in); // create a inputStreamReader from
																				// System.in->input from keyboard
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // create a bufferedReader
		Client.username = bufferedReader.readLine(); // read a line an set the username

		return Client.username; // return the username
	}

	public static void sendUser(String user, Socket sock) throws Exception // send username function
	{
		DataOutputStream dataOutputStream = new DataOutputStream(sock.getOutputStream()); // get the dataOutputStream
																							// from socket
		dataOutputStream.writeUTF(user); // write name with utf encoding to socket
	}

	public static void parseResult(String receivedResult) // parese result function
	{
		String values[] = receivedResult.split(" "); // split receivedResult ,
		if (values[1].equals("X")) // if result contains "X"
		{
			Client.setNumberOfPlanes(Client.getNumberOfPlanes() - 1); // decrease the numberOfPlanes by 1
		}
	}

	public static String receiveWinner(Socket sock) throws Exception // receive winner function
	{
		DataInputStream dataInputStream = new DataInputStream(sock.getInputStream()); // get dataInputStream from the
																						// socket
		String winner = dataInputStream.readUTF(); // read the winner in utf encoding from the socket
		return winner; // return the winner
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		Socket s = new Socket("localhost", 6349);
		Client.sendUser(Client.readUsername(), s); // send username to the server
		Client.setNumberOfPlanes(Server.getM().getNumberOfPlanes()); // get number of planes from the server
		while (true) // execute infinitely read coordinates and send them to server and receive
						// results from the server
		{
			try {
				if (Client.getNumberOfPlanes() == 0) // if number of planes becames 0
				{
					String winner = receiveWinner(s); // receive the winner from server
					System.out.println(winner); // display the winner
					Client.setNumberOfPlanes(3); // reset the number of planes to 3 each config has 3 planes on 10x10
													// matrix
					//
					Client.sendCoordinates(s); // read and send coordinates to server
					String res2 = Client.receiveResult(s); // receive the result of the shot from server
					Client.parseResult(res2); // parse the result if it receives "X" decrease numberOfPlanes by 1
					System.out.println(res2); // display the result
				} else {
					Client.sendCoordinates(s); // read and send coordinates to server
					String res = Client.receiveResult(s); // receive result of the shot from server
					Client.parseResult(res); // parse result if it receives "X" decrease numberOfPlanes by 1
					System.out.println(res); // display the result
				}
			} catch (Exception e) {

			}
		}
	}

}