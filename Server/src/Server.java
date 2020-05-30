import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static final int PORT = 8080;
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	private static ServerSocket server = null;
	private static String username = "test";
	
	public static void main(String[] args) throws IOException{
		server = new ServerSocket(PORT);
		System.out.println("[SERVER] Stared and is waiting for connection...");
		while(true) {
			try {
				Socket client = server.accept();
				System.out.println("[SERVER] Client connected!");
				ClientHandler clientThread = new ClientHandler(client, username);
				clients.add(clientThread);
				pool.execute(clientThread);
			} catch(IOException e) {
				System.out.println(e);
			}
		}
	}
}
