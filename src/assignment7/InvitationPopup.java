package assignment7;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InvitationPopup {
	static boolean accept;

	public static boolean display(String user){
		Stage popup = new Stage();
		popup.setTitle("Invite");
		popup.initModality(Modality.APPLICATION_MODAL);
		accept = false;
		Button yes = new Button("Yes");
		Button no = new Button("No");
		Label text = new Label(user + " has invited you to a private chat! Do you accept?");
		
		 yes.setOnAction(e->{
			 	accept = true;
			 	popup.close();
		    });
		 no.setOnAction(e->{
			 	accept = false;
			 	popup.close();
		    });
		 VBox layout = new VBox();
		 HBox select = new HBox();
		 select.getChildren().addAll(yes, no);
		 select.setAlignment(Pos.CENTER);
		 layout.getChildren().addAll(text, select);
		 layout.setAlignment(Pos.CENTER);
		 Scene scene = new Scene(layout, 350, 100);
		 popup.setScene(scene);
		 popup.showAndWait();
		 return accept; 
	}
}
