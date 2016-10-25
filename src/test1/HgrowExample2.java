package test1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mainPackage.Main;

public class HgrowExample2 extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    Stage window;

    @Override

    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("the new title");

        //top
        Button button1 = new Button("Button1");
        Button button2 = new Button("Button2");
        button1.setMaxWidth(Double.MAX_VALUE);
        button2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(button1, Priority.ALWAYS);
        HBox.setHgrow(button2, Priority.ALWAYS);
        HBox topBox = new HBox();
        topBox.getChildren().addAll(button1, button2);





        //main
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBox);
        mainLayout.setLeft(new Left().leftL());
        mainLayout.setBottom(new Bottom().bottmoBox());
        mainLayout.setCenter(new Center().centerBox());

        Scene scene = new Scene(mainLayout, 300, 200);
        window.setScene(scene);

        window.show();
    }

    private class Center{
        public VBox centerBox(){
            VBox mBox = new VBox();

            VBox topBar = new VBox();
            Button nBtn = new Button("New note");
            topBar.getChildren().addAll(nBtn);

            HBox bottomBox = new HBox();

            ListView<String> list = new ListView<>();
            list.getItems().addAll("Item1", "item2");

            VBox rightSide = new VBox();
            TextField heading = new TextField();
            TextArea area = new TextArea();
            VBox.setVgrow(area, Priority.ALWAYS);
            rightSide.getChildren().addAll(heading, area);

            HBox.setHgrow(rightSide, Priority.ALWAYS);
            bottomBox.getChildren().addAll(list, rightSide);

            VBox.setVgrow(bottomBox,Priority.ALWAYS);
            mBox.getChildren().addAll(topBar, bottomBox);

            return mBox;
        }
    }


    private class Left{

        public VBox leftL(){
            //left
            VBox leftBox = new VBox();
            HBox buttonBox = new HBox();
            buttonBox.getChildren().addAll(new Button("b1"), new Button("b2"));
            TreeItem<String> root = new TreeItem<>("root");
            TreeItem<String> item1 = new TreeItem<>("item1");
            TreeItem<String> itme2 = new TreeItem<>("item2");
            root.getChildren().addAll(item1, itme2);
            TreeView<String> treeView = new TreeView<>(root);
            treeView.showRootProperty().set(false);
            //elements above treeView
            VBox topElements = new VBox();
            Label descriptionLabel = new Label("NoteBooks:");
            descriptionLabel.setFont(Font.font("Times", 15));

            //buttons sub-layout
            HBox topButtonsLayout = new HBox();
            //add notebook button
            Button addButton = new Button("+");
            // remove notebook button
            Button removeButton = new Button("-");
            topButtonsLayout.getChildren().addAll(addButton, removeButton);
            //separator
            Separator separator = new Separator();
            separator.setOrientation(Orientation.HORIZONTAL);

            topElements.getChildren().addAll(topButtonsLayout, descriptionLabel, separator);


            VBox.setVgrow(treeView, Priority.ALWAYS);
            leftBox.getChildren().addAll(topElements, treeView);
            return leftBox;
        }


    }

    private class Bottom{

        public HBox bottmoBox(){
            HBox box = new HBox();
            Label label = new Label("MessageBox");
            box.getChildren().addAll(label);
            return box;
        }
    }
}
