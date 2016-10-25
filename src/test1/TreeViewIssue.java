package test1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by margus@workstation on 17.11.2015.
 */
public class TreeViewIssue extends Application{

    public static void main(String[] args){
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        // set window title
        window.setTitle("My Note App");

        //main layout
        BorderPane mainLayout = new BorderPane();

        //top
        MenuBar topMenuBar = new MenuBar();
        //button
        Menu fileButton = new Menu("file");
        topMenuBar.getMenus().addAll(fileButton);

        //left
        VBox leftSideBar = new VBox();
        //tree
        TreeItem<String> root = new TreeItem<>("root");
        TreeView<String> tree = new TreeView<>(root);
        TreeItem<String> treeItem = new TreeItem<>("rida 1");
        root.getChildren().add(treeItem);
        root.setExpanded(true);

        //tree into separate box
        VBox leftBox = new VBox();
        leftBox.getChildren().addAll(tree);

        //label
        Label label = new Label("textLabel");
        //label into Vbox
        VBox labelBox = new VBox();
        labelBox.getChildren().addAll(label);

        //add both VBox-es into sidebar
        leftSideBar.getChildren().addAll(leftBox, labelBox);




        //bottom
        HBox messageBoxLayout = new HBox();
        messageBoxLayout.getChildren().addAll(new TextField("textfield"));

        //center
        HBox noteBoxArea = new HBox();
        noteBoxArea.getChildren().addAll(new TextArea("ala"));

        mainLayout.setTop(topMenuBar);
        mainLayout.setLeft(leftSideBar);
        mainLayout.setBottom(messageBoxLayout);
        mainLayout.setCenter(noteBoxArea);




        Scene scene = new Scene(mainLayout, 800, 500);
        scene.getStylesheets().add("/mainPackage/stylesheet.css");
        // set scene for the main stage
        window.setScene(scene);
        // display stage
        window.show();
    }
}
