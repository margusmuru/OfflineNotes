package messageBoxes;

/**
 * Created by margus@workstation on 14.11.2015.
 */

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainPackage.Main;

public class AboutBox {

    /**
     * displays info about the APP
     */
    public static void display(){

        Stage window = new Stage();
        window.setX(Main.getWindow().getX() + 50);
        window.setY(Main.getWindow().getY() + 50);
        window.initStyle(StageStyle.UTILITY);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setMinHeight(300);
        window.setMinWidth(550);
        window.setTitle("About");

        // load the image
        Image image = new Image("Images/Logo.png");
        // simple displays ImageView the image as is
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(280);
        imageView.setFitWidth(280);

        //info layout
        VBox infoBox = new VBox(10);
        Label nameLabel = new Label("OfflineNotes");
        nameLabel.setStyle("-fx-font-size: 300%; -fx-font-weight: bold; -fx-text-fill: #0095c8");

        Label versionLabel = new Label("\nVersion 1.0");
        versionLabel.setStyle("-fx-font-size: 150%");

        Label creatorLabel = new Label("Created by Margus Muru");
        creatorLabel.setStyle("-fx-font-size: 150%");

        infoBox.getChildren().addAll(nameLabel, versionLabel, creatorLabel);

        //main layout
        HBox layout = new HBox(10);
        layout.getChildren().addAll(imageView, infoBox);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


}
