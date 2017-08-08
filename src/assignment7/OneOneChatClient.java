package assignment7;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OneOneChatClient{
	//connection
	private PrintWriter writer;
	private String id;
	private String id2;
	private int channel;
	
	//ChatBox
	private TextArea chatBox;
	private ScrollPane container;
	private Button send;
	
	//InputBox
	private Label nameIndicator;
	private GridPane inputBox;
	private TextField outgoing;
	
	//display
	private GridPane display;
	private Scene scene;
	private Stage stage2;
	
	private ArrayList<String> messages;
	public OneOneChatClient(String id, String id2, PrintWriter writer){
		this.writer = writer;
		this.id = id;
		this.id2 = id2;
	}
	public void display(){
		nameIndicator = new Label("        ");
		stage2 = new Stage();
		outgoing = new TextField();
		send = new Button("send");
		send.setDefaultButton(true);
		inputBox = new GridPane();
		inputBox.add(nameIndicator, 0, 0);
		inputBox.add(outgoing, 0, 0);
		inputBox.add(send, 1, 0);
		
		chatBox = new TextArea();
		chatBox.setEditable(false);
		chatBox.setWrapText(true);
		container = new ScrollPane();
	    container.setContent(chatBox); 
		messages = new ArrayList<String>();
		 send.setOnAction(evt->{
			 	String outgoingFinal = "3 " + channel + " " + id + " : " + outgoing.getText();
				writer.println(outgoingFinal);
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
		    });
		
		display = new GridPane();
		display.add(container,0, 0);
	    display.add(inputBox, 0, 1);
	    display.setPadding(new Insets(10, 10, 10, 10));
	    scene = new Scene(display,470,470);
	    stage2.setTitle("Private chat with: " + id2 + " (channel " + channel + ")");
		stage2.setScene(scene);
		stage2.show();
	    

	}
	
	public void setChannel(int i){
		channel = i;
	}
	
	public int getChannel(){
		return channel;
	}
	
	public void printToClient(String message){
		messages.add(message.substring(2));
		chatBox.appendText(message.substring(2) + "\n");
		
	}


}
