package messageBoxes;

/**
 * Created by margus@workstation on 14.11.2015.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import noteStuff.NoteBook;

import java.util.ArrayList;
import java.util.List;

public class MoveNoteDialogBox {

    private static NoteBook result = null;

    @SuppressWarnings("Duplicates")
    public static NoteBook display(ArrayList<NoteBook> noteBooks, String curBook){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UTILITY);
        window.setResizable(false);
        window.setTitle("Choose notebook");
        window.setMinWidth(250);
        window.setMaxHeight(400);

        Label label = new Label();
        label.setText("Choose a notebook for destination");
        label.setStyle("-fx-font-weight: bold");

        VBox dialogBox = new VBox(10);
        //listview for notebooks
        ListView<NoteBook> noteBookListView = new ListView<>();
        dialogBox.getChildren().addAll(label, noteBookListView);
        //add items
        for (NoteBook book : noteBooks){
            if (!book.toString().equals(curBook) && !book.toString().equals("#Trash")){
                noteBookListView.getItems().add(book);
            }
        }
        //select first item
        noteBookListView.getSelectionModel().selectFirst();

        //create two buttons
        Button yesButton = new Button("Move");
        yesButton.setMinWidth(50);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);
        cancelButton.requestFocus();

        yesButton.setOnAction(event -> {
            result = noteBookListView.getSelectionModel().getSelectedItem();
            window.close();
        });

        cancelButton.setOnAction(event -> {
            result = null;
            window.close();
        });

        //layout for buttons
        HBox buttonLayout = new HBox(40);
        buttonLayout.getChildren().addAll(cancelButton, yesButton);
        buttonLayout.setAlignment(Pos.CENTER);

        //main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 0, 10));
        layout.getChildren().addAll(dialogBox, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return result;
    }
}
