package test1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * Created by margus@tablet on 10.11.2015.
 */
public class SearchClass extends Application{

    Stage window;
    Button searchButton, replaceButton, replaceAllButton;
    TextField searchTerm, replaceTerm;
    TextArea textArea;

    public static void main(String[] args){
        launch(args);

    }

    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("SearchTest");
/*
        //runs when you press window close button
        window.setOnCloseRequest(e -> {
            //take over app close command.
            e.consume();
            closeProgram();
        });
*/
        //searchField
        Label searchTermLabel = new Label("Enter search term:");
        searchTerm = new TextField();
        searchTerm.setPromptText("Search term");

        //replacefield
        Label replaceTermLabel = new Label("Replace with");
        replaceTerm = new TextField();
        replaceTerm.setPromptText("Leave blank if you want search only");

        //textarea
        Label textAreaLabel = new Label("Enter text here: ");
        textArea = new TextArea();
        textArea.setPromptText("Will be searched from this text here");

        searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            //what to do when button is clicked
            searchForTerm(searchTerm, textArea);
        });
        replaceButton = new Button("Replace Next");
        replaceButton.setOnAction(event -> {
            //what to do when button is clicked
            replaceTerm(searchTerm, replaceTerm, textArea, false);
        });
        replaceAllButton = new Button("Replace All");
        replaceAllButton.setOnAction(event -> {
            replaceTerm(searchTerm, replaceTerm, textArea, true);
        });


        //layout for buttons
        HBox buttonLayout = new HBox();
        buttonLayout.getChildren().addAll(searchButton, replaceButton, replaceAllButton);

        //layout for the whole app
        VBox layout = new VBox();
        layout.getChildren().addAll(searchTermLabel, searchTerm, replaceTermLabel, replaceTerm, buttonLayout, textAreaLabel, textArea);

        //set scene
        Scene scene = new Scene(layout, 300, 250);

        window.setScene(scene);
        window.show();
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("title", "sure u want to exit?");
        if (answer){
            window.close();
        }
    }

    @SuppressWarnings("Duplicates")
    private void searchForTerm(TextField field, TextArea textField){
        String searchTerm = field.getText();
        String textSource = textArea.getText();

        int startIndex = textSource.indexOf(searchTerm, 0);
        int endIndex = startIndex + searchTerm.length();
        //System.out.println(textSource.substring(startIndex, endIndex));
        //text exists
        if (startIndex >= 0){
            textField.selectRange(startIndex, endIndex);
        }

    }

    @SuppressWarnings("Duplicates")
    private void replaceTerm(TextField searchField, TextField replaceField, TextArea textField, boolean replaceAll){
        String searchTerm = searchField.getText();
        String replaceTerm = replaceField.getText();
        String textSource = textField.getText();

        int startIndex = 0;
        int endIndex = 0;

        while (startIndex >= 0 && startIndex < textSource.length()){
            //where to start the search
            startIndex = textSource.indexOf(searchTerm, endIndex);
            //where to end replacing
            endIndex = startIndex + searchTerm.length();

            if (startIndex >= 0){
                textField.replaceText(startIndex, endIndex, replaceTerm);
            }
            if (!replaceAll){
                textField.selectRange(startIndex, endIndex);
                break;
            }
        }


    }


}