package test1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by margus@workstation on 09.11.2015.
 */
public class ConfirmBox {

    //store answer
    static boolean answer;

    @SuppressWarnings("Duplicates")
    public static boolean display(String title, String message){

        Stage window = new Stage();

        //block user interaction for other stages
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        //create two buttons
        Button yesButton = new Button("yes");
        Button noButton = new Button("no");

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(event -> {
            answer = false;
            window.close();
        });


        //spaced out by 20 pixels
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
