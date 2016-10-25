package messageBoxes;

/**
 * Created by margus@workstation on 14.11.2015.
 */

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import noteStuff.Note;

public class NoteInfoBox {

    /**
     * displays info about given note
     * @param note Note
     */
    @SuppressWarnings("Duplicates")
    public static void display(Note note, Stage mainStage, Button button){

        Stage window = new Stage();
        Point2D point = button.localToScene(0.0,  0.0);
        window.setX(mainStage.getX() + point.getX() - 200);
        window.setY(mainStage.getY() + point.getY() + 60);
        window.initStyle(StageStyle.UTILITY);
        //block user interaction for other stages
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Note \"" + note.getName() + "\"");
        window.setMinWidth(250);

        //create two buttons
        Button yesButton = new Button("close");
        yesButton.setMinWidth(50);
        yesButton.setStyle("-fx-background-color: darkgray");
        yesButton.setMaxWidth(Double.MAX_VALUE);
        yesButton.requestFocus();
        yesButton.setOnAction(event -> {
            window.close();
        });
        //creating gridpane and its elements
        GridPane table = new GridPane();
        Label nameLabel = getLabel("Heading", 0, 0);
        Label nameValue = getLabel(note.getName(), 1, 0);
        Label parentLabel = getLabel("ParentBook    ", 0, 1);
        Label parentValue = getLabel(note.getParentBranch(), 1, 1);
        Label idLabel = getLabel("Note ID", 0, 2);
        Label idValue = getLabel(Integer.toString(note.getNoteID()), 1, 2);
        Label createdLabel = getLabel("Time created", 0, 3 );
        Label createdValue = getLabel(note.getTimeCreated(), 1, 3);
        Label modifiedLabel = getLabel("Last modified   ", 0, 4);
        Label modifiedValue = getLabel(note.getTimeModified(), 1, 4);
        Label tagsLabel = getLabel("Tags", 0, 5);
        Label tagsValue = getLabel("None", 1, 5);
        try{
            tagsValue = getLabel(note.getTagsAsString(), 1, 5);
        }catch (NullPointerException e){}

        table.getChildren().addAll(nameLabel, nameValue, parentLabel, parentValue);
        table.getChildren().addAll(idLabel, idValue, createdLabel, createdValue);
        table.getChildren().addAll(modifiedLabel, modifiedValue, tagsLabel, tagsValue);

        //main layout
        VBox layout = new VBox(10);
        VBox.setVgrow(yesButton, Priority.ALWAYS);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(table, yesButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * set up label parameters and position in gridpane
     * @param name label text
     * @param xPos label x position
     * @param yPos label y position
     * @return Label
     */
    private static Label getLabel(String name, int xPos, int yPos){
        Label label = new Label(name);
        GridPane.setConstraints(label, xPos, yPos);
        if (xPos == 0){
            label.setStyle("-fx-font-weight: bold");
        }
        return label;
    }

}
