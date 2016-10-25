package messageBoxes;

/**
 * Created by margus@workstation on 14.11.2015.
 */

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GetConfirmationBox {

    //store answer
    static boolean answer;

    /**
     * returns user input as string.
     * @param title window title
     * @param message message to display abowe textField
     * @return String
     */
    @SuppressWarnings("Duplicates")
    public static boolean display(String title, String message){

        Stage window = new Stage();

        //block user interaction for other stages
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle(title);
        window.setMinWidth(250);
        window.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //enter key
                if (event.getCode() == KeyCode.ENTER){
                    answer = true;
                    window.close();
                }
                //esc key
                if (event.getCode() == KeyCode.ESCAPE){
                    answer = false;
                    window.close();
                }
            }
        });
        Label label = new Label();
        label.setText(message);

        //create two buttons
        Button yesButton = new Button("Ok");
        yesButton.setMinWidth(50);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);

        //action for yes button
        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });

        //action for no button
        cancelButton.setOnAction(event -> {
            answer = false;
            window.close();
        });

        //layout for buttons
        HBox buttonLayout = new HBox(40);
        buttonLayout.getChildren().addAll(cancelButton, yesButton);
        buttonLayout.setAlignment(Pos.CENTER);

        //main layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
