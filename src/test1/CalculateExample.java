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
public class CalculateExample extends Application{

    Stage window;
    TextField answer;
    Button getResult;
    Label calculationText;
    double question;

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
        Label instruction = new Label("Arvuta tulemus: ");
        calculationText = new Label();

        answer = new TextField();
        answer.setPromptText("Type your answer here");
        answer.requestFocus();

        getResult = new Button("Arvuta");
        getResult.setOnAction(event -> {
            processResult(calculationText, answer, question);
            answer.requestFocus();
        });

        question = getRandomQuestion(calculationText);


        //layout for the whole app
        VBox layout = new VBox();
        layout.getChildren().addAll(instruction, calculationText, answer, getResult);

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

    private void processResult(Label labelText, TextField resultField, double correctValue){
        double answer = 0;
        try{
            answer = Double.parseDouble(resultField.getText());
        }catch (Exception e){
            String text = "Sisesta number! \n" + labelText.getText();
            labelText.setText(text);
        }
        if(answer == correctValue){
            question = getRandomQuestion(calculationText);
            String text = "Õige! " + calculationText.getText();
            calculationText.setText(text);
        }else{
            String text = "Vale! Proovi uuesti " + labelText.getText();
            labelText.setText(text);
        }
    }

    private double getRandomQuestion(Label label){
        double value1 = Math.round((Math.random() * 10));
        double value2 = Math.round((Math.random() * 10));
        String[] mark = {" * ", " / ", " + ", " - "};
        int randMark = (int) (Math.random() * 4);
        String labelString = Double.toString(value1) + mark[randMark] + Double.toString(value2);
        label.setText(labelString);
        double result = 0;
        switch (randMark){
            case 0:
                result = value1 * value2;
                break;
            case 1:
                result = value1 / value2;
                break;
            case 2:
                result = value1 + value2;
                break;
            default:
                result = value1 - value2;
        }
        return result;
    }


}