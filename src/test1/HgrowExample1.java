package test1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HgrowExample1 extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.WHITE);

        HBox hbox = new HBox(8);//space
        Button button1 = new Button("Add               ");
        Button button2 = new Button("Remove   ");
        //HBox.setHgrow(button1, Priority.ALWAYS);
        //HBox.setHgrow(button2, Priority.ALWAYS);
        button1.setMaxWidth(Double.MAX_VALUE);
        button2.setMaxWidth(Double.MAX_VALUE);
        hbox.getChildren().addAll(button1, button2);

        hbox.setPrefWidth(400);

        root.getChildren().add(hbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
