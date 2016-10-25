package test1;

import com.sun.javafx.event.RedirectedEvent;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Created by margus@workstation on 21.11.2015.
 */
public class PopupExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Popup Example");
        final Popup popup = new Popup();
        popup.getScene().getWindow().setEventDispatcher((event, tail) -> {
            if (event.getEventType() == RedirectedEvent.REDIRECTED) {
                //  RedirectedEvent is a box that contains original event from other target
                RedirectedEvent ev = (RedirectedEvent) event;
                if (ev.getOriginalEvent().getEventType() == MouseEvent.MOUSE_PRESSED) {
                    popup.hide();
                }
            }else {
                //  if click in the popup window. handle the event by default
                tail.dispatchEvent(event);
            }
            return null;
        });
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                popup.hide();
            }

        });

        popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));

        Button show = new Button("Show");
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.show(primaryStage);
                Point2D point = show.localToScene(0.0,  0.0);
                popup.setX(primaryStage.getX() + point.getX());
                popup.setY(primaryStage.getY() + point.getY() + 40);
            }
        });

        Button hide = new Button("Hide");
        hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
            }
        });

        HBox layout = new HBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(show, hide);
        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.show();
    }
}
