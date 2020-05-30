import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import org.json.simple.JSONObject;

public class Client {
	private static Socket socket = null;
	private static String SERVER_IP = "127.0.0.1";
	private static int SERVER_PORT = 8080;
	private static String username = null;
	//private static String password = null;
	
	private static String message = null;
	
	private static boolean createFrame = true;
	
	private static JFrame cFrame = null;
	
	private static JTextField addressInput = null;
	private static JTextField portInput = null;
	private static JTextField usernameInput = null;
	private static JTextField passwordInput = null;
	private static JButton connectBtn = null;
	
	private static JTextArea chatBox = null;
	private static JTextField messageInput = null;
	private static JButton sendBtn = null;
	
	private static JTextField placeholder = null;
	
	private static PrintWriter output = null;
	private static JSONObject json = null;;
	
	public static void main(String[] args) {
		if(createFrame) {
	        cFrame = new JFrame("Client");  
	        
	        addressInput = new JTextField("127.0.0.1");
	        cFrame.add(addressInput);
	        addressInput.setBounds(5, 5, 100, 20);
	        portInput = new JTextField("8080");
	        cFrame.add(portInput);
	        portInput.setBounds(110, 5, 100, 20);
	        usernameInput = new JTextField("username");
	        cFrame.add(usernameInput);
	        usernameInput.setBounds(5, 30, 100, 20);
	        passwordInput = new JTextField("password");
	        cFrame.add(passwordInput);
	        passwordInput.setBounds(110, 30, 100, 20);
	        connectBtn = new JButton("connect");
	        cFrame.add(connectBtn);
	        connectBtn.setBounds(225, 5, 100, 45);
	        connectBtn.addActionListener(new ActionListener(){
	        	@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e){
	        		SERVER_IP = addressInput.getText();
	        		SERVER_PORT = Integer.parseInt(portInput.getText());
	        		username = usernameInput.getText();
	        		try {
						socket = new Socket(SERVER_IP, SERVER_PORT);
						
						ServerConnection serverConn = new ServerConnection(socket, chatBox);
		        		new Thread(serverConn).start();
		        		
		        		try {
			        		json = new JSONObject();
				        	message = messageInput.getText();
				        	json.put("username", username);
				        	json.put("message", message);
				        	
			        		output = new PrintWriter(socket.getOutputStream(), true);
				    		output.println(json);
		        		}
		        		catch(Error e1) {
		        			System.out.println(e1);
		        		}
					} catch (IOException e1) {
						chatBox.setText("No Server found (check PORT)!\n");
					}
	        	}
	        });
	        
	        chatBox = new JTextArea();
	        cFrame.add(chatBox);
	        chatBox.setBounds(5, 55, 480, 100);
	        
	        messageInput = new JTextField("message");
	        cFrame.add(messageInput);
	        messageInput.setBounds(5, 160, 100, 20);
	        sendBtn = new JButton("send");
	        cFrame.add(sendBtn);
	        sendBtn.setBounds(110, 160, 100, 20);
	        sendBtn.addActionListener(new ActionListener(){
	        	@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e){
	        		if(socket != null) {
	        			if(e.getSource() == sendBtn) {
	        				JSONObject json = new JSONObject();
	        				message = messageInput.getText();
	        				json.put("message", message);
	        				try {
	        					output = new PrintWriter(socket.getOutputStream(), true);
	        					output.println(json);

	        				} catch (IOException e1) {}
	        			}
	        		}else {
						chatBox.setText("You arn't connected to  a Server!");
					}
	        	}
	        });
	        
	        placeholder = new JTextField("nothing");
	        cFrame.add(placeholder);
	        placeholder.setVisible(false);
	        
	        cFrame.addWindowListener(new WindowListener() {
	        	public void windowClosing(WindowEvent e){
	        		if(socket != null) {
	        			try {
	        				socket.close();
	        			} catch (IOException e1) {}
	        			System.out.println("terminating program...");
	        			System.exit(0);
	        		}
		        }
				@Override
				public void windowOpened(WindowEvent e) {
				}
				@Override
				public void windowClosed(WindowEvent e) {
				}
				@Override
				public void windowIconified(WindowEvent e) {
				}
				@Override
				public void windowDeiconified(WindowEvent e) {	
				}
				@Override
				public void windowActivated(WindowEvent e) {	
				}
				@Override
				public void windowDeactivated(WindowEvent e) {
				}
	        });
	        cFrame.setSize(500, 500);
	        cFrame.setVisible(true);
	        createFrame = false;
		}
	}	
}