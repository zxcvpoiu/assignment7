package assignment7;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class ChatClient extends Application implements Observer {
	private BufferedReader reader;
	private PrintWriter writer;
	private OutputStream streamWriter;
	
	private GridPane root = new GridPane();
	private Scene scene;
	
	//InputBox
	private Button send;// = new Button("Send");
	private TextField outgoing;// = new TextField();
	private GridPane inputBox = new GridPane();
	
	//ChatBox
	private TextArea chatBox;// = new TextArea();
	private ScrollPane container;// = new ScrollPane();
	private List<String> messages = new ArrayList<String>();
	private int index = 0;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public ChatClient(){
	}
	public ChatClient(OutputStream outputStream){
		streamWriter = outputStream;
	}
	@Override
	public void start(Stage stage) throws Exception {	
		try {
			setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    initInputBox();
	    initChatBox();
	    root.add(container,0, 0);
	    root.add(inputBox, 0, 1);
	    root.setPadding(new Insets(10, 10, 10, 10));
	    scene = new Scene(root,470,470);
	    stage.setTitle("Chat With Me");
	    stage.setScene(scene);
	    stage.show();
		
	}
	private void initChatBox(){
		//messages = new ArrayList<String>();
		chatBox = new TextArea();
		chatBox.setEditable(false);
		container = new ScrollPane();
	    container.setPrefSize(450, 400);
	    chatBox.setPrefWidth(445);
	    //chatBox.setAlignment(Pos.BASELINE_LEFT);
	    container.setContent(chatBox); 

	    //chatBox.getStyleClass().add("chatbox");

	    send.setOnAction(evt->{
	    	Label message = new Label(outgoing.getText());
	    	//message.setMaxWidth(Double.MAX_VALUE);
	    	//message.setAlignment(Pos.BASELINE_LEFT);
	        //messages.add(message);
	        
			//printMessage(message);
			writer.println(message.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
	        /*
	        if(index%2==0){
	            messages.get(index).setAlignment(Pos.CENTER_LEFT);
	            System.out.println("1");

	        }else{

	            messages.get(index).setAlignment(Pos.CENTER_RIGHT);
	            System.out.println("2");

	        }
	        */

	    });
	}
	
	private void initInputBox(){
		send = new Button("Send");
		outgoing = new TextField();
		outgoing.setPrefWidth(390);
		send.setPrefWidth(60);
		inputBox.add(outgoing, 0, 0);
		inputBox.add(send, 1, 0);
		inputBox.setPadding(new Insets(10, 0, 10, 0));
		
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4248);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		//Thread readerThread = new Thread(new IncomingReader());
		//readerThread.start();
	}

	private synchronized void printMessage(String message){	
		
		System.out.println(message);
		messages.add(message);
		//JFXPanel fxPanel = new JFXPanel();
		chatBox = new TextArea();
		chatBox.setEditable(false);
		chatBox.appendText(message + "\n");	
	    container.setContent(chatBox); 

        //chatBox.getChildren().add(messages.get(index));
        index++;
	}

	@Override
	public void update(Observable server, Object message) {
		printMessage((String)message);
		System.out.println("it should reach here");
		
	}

	
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
						final String message1 = message;
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								printMessage(message1);
							}
							
						});
			        	//chatBox.getChildren().add(messages.get(index));
						//incoming.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
