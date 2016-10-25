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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class GetNoteSaveOptionsBox {

    private static List<Boolean> options;

    @SuppressWarnings("Duplicates")
    public static List<Boolean> display(){
        options = new ArrayList<>();

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UTILITY);
        window.setResizable(false);
        window.setTitle("Options");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Choose data to save along with note content");
        label.setStyle("-fx-font-weight: bold");

        VBox optionsBox = new VBox(10);

        CheckBox boxHeading = new CheckBox("Note heading");
        boxHeading.setSelected(true);
        CheckBox boxParent = new CheckBox("Notebook name");
        CheckBox boxID = new CheckBox("Note ID");
        CheckBox boxCreated = new CheckBox("Time created");
        CheckBox boxModified = new CheckBox("Time modified");
        CheckBox boxTags = new CheckBox("Note tags");

        optionsBox.getChildren().addAll(label, boxHeading, boxParent, boxID, boxCreated, boxModified, boxTags);

        //create two buttons
        Button yesButton = new Button("Save");
        yesButton.setMinWidth(50);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(50);
        cancelButton.requestFocus();

        yesButton.setOnAction(event -> {
            options.add(boxHeading.isSelected());
            options.add(boxParent.isSelected());
            options.add(boxID.isSelected());
            options.add(boxCreated.isSelected());
            options.add(boxModified.isSelected());
            options.add(boxTags.isSelected());
            window.close();
        });

        cancelButton.setOnAction(event -> {
            window.close();
        });

        //layout for buttons
        HBox buttonLayout = new HBox(40);
        buttonLayout.getChildren().addAll(cancelButton, yesButton);
        buttonLayout.setAlignment(Pos.CENTER);

        //main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 0, 10));
        layout.getChildren().addAll(optionsBox, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return options;
    }
}
