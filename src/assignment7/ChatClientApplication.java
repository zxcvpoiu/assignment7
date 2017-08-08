package assignment7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatClientApplication extends Application {
	//connection
	private BufferedReader reader;
	private PrintWriter writer;
	private String id="none";
	
	//ChatBox
	private TextArea chatBox;
	private ScrollPane container;
	private Button send;
	
	//InputBox
	private GridPane inputBox;
	private TextField outgoing;
	private Label nameIndicator;
	
	//display
	private GridPane display;
	private Scene scene;
	private Stage stage1;
	
	//OneOne Chat
	private String id2 = "none";
	private GridPane oneOne;
	private ComboBox<String> usersDropDown;
	private Button startOneOne;
	
	private ArrayList<OneOneChatClient> privateChats;
	private HashSet<String> users;
	private ArrayList<String> messages;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public ChatClientApplication() {
	}
	
	public void setUpGUI(){
		nameIndicator = new Label("        ");
		outgoing = new TextField();
		send = new Button("send");
		send.setDefaultButton(true);
		inputBox = new GridPane();
		inputBox.add(nameIndicator, 0, 0);
		inputBox.add(outgoing, 1, 0);
		inputBox.add(send, 2, 0);
		
		chatBox = new TextArea();
		chatBox.setEditable(false);
		chatBox.setWrapText(true);
		container = new ScrollPane();
	    container.setContent(chatBox); 
		messages = new ArrayList<String>();
		users = new HashSet<String>();
		 send.setOnAction(evt->{
			 	String outgoingFinal ="0 " + id + " : " + outgoing.getText();
				writer.println(outgoingFinal);
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
		    });
		
		display = new GridPane();
		display.add(container,0, 0);
	    display.add(inputBox, 0, 1);
	    addOneToOneFunctionality();
	    display.setPadding(new Insets(10, 10, 10, 10));
	    scene = new Scene(display,500,360);
	}
	
	private void addOneToOneFunctionality(){
		oneOne = new GridPane();
		usersDropDown = new ComboBox<String>();
		startOneOne = new Button("Private");
		startOneOne.setOnAction(evt->{
			id2 = usersDropDown.getValue();
			if(id2.length()>5)
			{
				writer.println("GET_CHANNEL " + id + " " + id2);
				writer.flush();
			}
	    });
		oneOne.add(usersDropDown, 0, 0);
		oneOne.add(startOneOne, 1, 0);
	    display.add(oneOne, 0, 2);
	}
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4248);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		privateChats = new ArrayList<OneOneChatClient>();
		System.out.println("networking established");
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					final String messagetemp = message;
					Platform.runLater(() -> {
					String[] cut = messagetemp.split(" ");
					int type = Integer.parseInt(cut[0]);
					
					//user has joined
					if(type==1){
						if(id.equals("none"))
							firstTimeInstantiation(cut);
						else{
							users.add("User " + cut[cut.length-1]);
							usersDropDown.getItems().add("User " + cut[cut.length-1]);	
							usersDropDown.getSelectionModel().selectFirst();
						}
						chatBox.appendText(cut[1] + " " +  cut[2] + " " + cut[3] + " " + cut[4] + "\n");
					}
					
					//group chat
					else if(type ==0){
						messages.add(messagetemp.substring(2));
						chatBox.appendText(messagetemp.substring(2) + "\n");
					}
					
					//add channel
					else if(type == 2){
						boolean open = false;
						String tempID1 = "User " + cut[3];
						String tempID2 = "User " + cut[5];
						if(id.equals(tempID1)){;
							open = WaitingPopup.display(tempID2);
						}
						else if(id.equals(tempID2)){
							open = InvitationPopup.display(tempID1);
							if(open){
								OneOneChatClient newClient = new OneOneChatClient(tempID2,tempID1,writer);
								privateChats.add(newClient);
								newClient.display();
								privateChats.get(privateChats.size()-1).setChannel(Integer.parseInt(cut[1]));
								writer.println("INVITATION_RESPONSE " + "ACCEPTED " + tempID1 + " " +tempID2 + " " + cut[1]);
							}
							else
								writer.println("INVITATION_RESPONSE " + "REJECTED " + tempID1 + " " +tempID2 + " " + cut[1]);
							writer.flush();
						}
					
								
					}
					//private chat with channel
					else if(type == 3){
						int channel = Integer.parseInt(cut[1]);
						for(OneOneChatClient pClient: privateChats){
							if(pClient.getChannel()==channel)
								pClient.printToClient(messagetemp.substring(2));
						}
					}
					
					//response to invite
					else if (type == 4){
						if(id.equals("User " + cut[3])){
							if(cut[1].equals("ACCEPTED")){
								OneOneChatClient newClient = new OneOneChatClient("User " + cut[3],"User " + cut[5],writer);
								privateChats.add(newClient);
								newClient.display();
								privateChats.get(privateChats.size()-1).setChannel(Integer.parseInt(cut[6]));
								WaitingPopup.close(true);
							}
							else{
								WaitingPopup.close(false);
								WaitingPopup.rejected();
							}
						}
					}
					else if (type ==5){
						users.remove("User " + cut[2]);
						usersDropDown.getItems().remove("User " + cut[2]);

						messages.add(messagetemp.substring(2));
						chatBox.appendText(messagetemp.substring(2) + "\n");
					}
					});
				}
			} 
			catch(SocketException e){
				System.out.println("you have left from some error. WTF????");
				System.exit(0);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void firstTimeInstantiation(String[] cut){
			if(id.equals("none")){
				id = cut[1] + " " + cut[2];
				stage1.setTitle(id);
				nameIndicator.setText(id + " :  ");
				for (int i = 6; i<cut.length; i+=2)
					users.add("User " + cut[i]);
				for(String remoteID: users)
					if (!id.equals(remoteID))
						usersDropDown.getItems().add(remoteID);	
				usersDropDown.getSelectionModel().selectFirst();
			}
		}
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		setUpNetworking();
		setUpGUI();
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		stage1 = primaryStage;
		primaryStage.setTitle(id);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
					writer.println("DISCONNECTED " + id);
					writer.flush();
					System.out.println("you have left.");
	          }
		});
	}

}