import java.io.*;
import java.net.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientHandler implements Runnable{
	private Socket client;
	private BufferedReader input;
	private PrintWriter output;
	private String username;
	
	public ClientHandler(Socket clientSocket, String username) throws IOException {
		this.client = clientSocket;
		this.username = username;
		input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		output = new PrintWriter(client.getOutputStream(),true);
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public void run() {
		try {
			boolean usernameSet = false;
			while(true) {
				try {
					String clientResponse = input.readLine();
					JSONParser parser = new JSONParser();
					JSONObject json = null;
					try {
						if(clientResponse == null){
							json.put("message","---");
						} else if(clientResponse != "Server Stoped!") {
							json = (JSONObject) parser.parse(clientResponse);
						} else{
							json.put("message",clientResponse);
						}
						String message = "";
						message = (String) json.get("message");
						if(!usernameSet) {
							username = (String) json.get("username");
							sendMessage("toUser", ("Welcome " + username));
							System.out.println(username + " joined!");
							usernameSet = !usernameSet;
							sendMessage("connection", (username + " joined!"));
						} else {
							sendMessage("toAll", message);	
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println(username + " left!");
						sendMessage("connection", (username + " left!"));
						try {
							output.println("Server is shutting down!");
							output.close();
							input.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
							break;
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					break;
				}
			}
		}finally {
			try {
				output.println("Server is shutting down!");
				output.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
	}

	private void sendMessage(String type, String message) {
		switch(type) {
		case "toUser":	
			output.println(message);
			break;
		case "toAll":
			for(ClientHandler aClient: Server.clients) {
				if(aClient.username != this.username && aClient.username != "test") {
					aClient.output.println(String.format("[%s] ", this.username)+ message);
				} else {
					aClient.output.println("[You] "+ message);
				}
		 	}
			break;
		case "connection":
			for(ClientHandler aClient: Server.clients) {
				if(aClient.username != this.username && aClient.username != "test") {
					aClient.output.println( message);
				}
		 	}
			break;
		default:
			//TODO
			break;
		}
	}

}
