package assignment7;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class IPPrompt {
	public static String[] display(){
		String[] results = new String[2];
		Stage popup = new Stage();
		popup.setTitle("ChatClientLogin");
		
		TextField ipPrompt = new TextField();
		TextField portPrompt = new TextField();
		Button confirm = new Button("Confirm");
		Label text1 = new Label("Enter IP: ");
		Label text2 = new Label("Enter Port: ");
		
		confirm.setOnAction(e->{
			 results[0] = ipPrompt.getText();
			 results[1] = portPrompt.getText();
			 popup.close();
		    });
		 GridPane inputs = new GridPane();
		 inputs.add(text1, 0, 0);
		 inputs.add(ipPrompt, 0, 1);
		 inputs.add(text2, 0, 2);
		 inputs.add(portPrompt, 0, 3);
		 inputs.add(confirm, 0, 4);
		 Scene scene = new Scene(inputs, 350, 300);
		 popup.setScene(scene);
			popup.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
						System.out.println("you have left.");
						System.exit(0);
		          }
			});
		 popup.showAndWait();
		 return results;
	}
	
	public static void displayError(){
		Stage popup2 = new Stage();
		popup2.setTitle("ChatClientLogin");
		popup2.initModality(Modality.APPLICATION_MODAL);
		

		Button confirm = new Button("Try Again");
		Label text1 = new Label("Could not Connect!");
		confirm.setOnAction(e->{
			 popup2.close();
		    });
		VBox layout = new VBox();
		layout.getChildren().addAll(text1, confirm);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 300, 100);
		popup2.setScene(scene);
		popup2.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
					System.out.println("you have left.");
					System.exit(0);
	          }
		});
		popup2.showAndWait();
	}
}
