package assignment7;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WaitingPopup {
	static Stage popup;
	static Stage popup2;
	static boolean accept = false;
	public static boolean display(String user){
		popup = new Stage();
		popup.setTitle("Invite");
		popup.initModality(Modality.APPLICATION_MODAL);
		Label text = new Label("Waiting for response from " + user + "...");
		text.setAlignment(Pos.CENTER);
		Scene scene = new Scene(text, 300, 100);
		popup.setScene(scene);
		popup.showAndWait();
		return accept; 
	}
	public static void close(Boolean b){
		accept = b;
		popup.close();
	}
	public static void rejected(){
		popup2 = new Stage();
		popup2.setTitle("Invite");
		Button b = new Button("OK");
		 b.setOnAction(e->{
			 	popup2.close();
		    });
		Label text = new Label("Your invitation was denied!");
		VBox layout = new VBox();
		layout.getChildren().addAll(text, b);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 300, 100);
		popup2.setScene(scene);
		popup2.show();
	}
}
