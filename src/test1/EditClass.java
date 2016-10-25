package test1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by margus@tablet on 10.11.2015.
 */
public class EditClass extends Application{

    Stage window;
    Button replaceButton;
    TextArea sourceText, resultText;

    public static void main(String[] args){
        launch(args);

    }

    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Editor");
/*
        //runs when you press window close button
        window.setOnCloseRequest(e -> {
            //take over app close command.
            e.consume();
            closeProgram();
        });
*/
        sourceText = new TextArea();
        resultText = new TextArea();

        replaceButton = new Button("Go");
        replaceButton.setOnAction(event -> {
            //what to do when button is clicked
            replaceText();
        });


        //layout for the whole app
        VBox layout = new VBox();
        layout.getChildren().addAll(sourceText, replaceButton, resultText);
        VBox.setVgrow(resultText, Priority.ALWAYS);
        //set scene
        Scene scene = new Scene(layout, 800, 600);

        window.setScene(scene);
        window.show();
    }


    @SuppressWarnings("Duplicates")
    private void replaceText(){
        String[] lines = sourceText.getText().split("\n");
        StringBuilder builder = new StringBuilder();

        int counter = 0;

        for(int i = 0; i < lines.length; i++){

            switch (counter)
            {
                case 0:
                    builder.append(lines[i].toLowerCase() + "\b ");
                    counter++;
                    break;
                case 1:
                    builder.append(lines[i].toLowerCase() + "\n");
                    counter++;
                    break;
                case 2:
                    if (lines[i].length() > 0){
                        builder.append(lines[i].toLowerCase() + " ");
                    }else{
                        counter = 0;
                        builder.append("\n\n");
                    }
                    break;
                default:
                    break;
            }


        }
        resultText.setText(builder.toString());
    }


}