package messageBoxes;

/**
 * Created by margus@workstation on 14.11.2015.
 */

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GetNameMessageBox {

    //store answer
    static String answer = "";

    /**
     * returns user input as string.
     * @param title window title
     * @param message message to display abowe textField
     * @return String
     */
    @SuppressWarnings("Duplicates")
    public static String display(String title, String message){

        Stage window = new Stage();

        //block user interaction for other stages
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        //inputfield
        TextField textField = new TextField();
        textField.setPromptText("Notebook name");
        textField.setMaxWidth(180);
        textField.requestFocus();

        //create two buttons
        Button yesButton = new Button("OK");
        yesButton.setMinWidth(50);
        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);

        //action for yes button
        yesButton.setOnAction(event -> {
            answer = textField.getText();
            window.close();
        });

        //action for no button
        cancelButton.setOnAction(event -> {
            answer = "#cancel";
            window.close();
        });

        //textfield must react to ENTER and ESC keys
        textField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //enter key
                if (event.getCode() == KeyCode.ENTER){
                    answer = textField.getText();
                    window.close();
                }
                //esc key
                if (event.getCode() == KeyCode.ESCAPE){
                    answer = "#cancel";
                    window.close();
                }
            }
        });

        //layout for buttons
        HBox buttonLayout = new HBox(40);
        buttonLayout.getChildren().addAll(cancelButton, yesButton);
        buttonLayout.setAlignment(Pos.CENTER);

        //main layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
