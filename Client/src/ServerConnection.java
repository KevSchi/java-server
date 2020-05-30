import java.net.*;

import javax.swing.JTextArea;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.*;
public class ServerConnection implements Runnable{
	private Socket server;
	private BufferedReader input;
	private JTextArea chatBox;
	//private PrintWriter output;
	
	public ServerConnection(Socket s, JTextArea cB) {
		chatBox = cB;
		server = s;
		try {
			input = new BufferedReader(new InputStreamReader(server.getInputStream()));
			System.out.println("Connecetion established!");
			chatBox.setText(chatBox.getText() + "Connecetion established!\n");
			//output = new PrintWriter(server.getOutputStream(),true);
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void run() {
			try {
				while(true) {
					String serverResponse = input.readLine();
					if(serverResponse == null) break;
					System.out.println(serverResponse);
					chatBox.setText(chatBox.getText() + serverResponse +"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Server Stoped!");
			} finally{
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			
		}
	}

}
