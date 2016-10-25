package mainPackage;

import databaseManagers.DataBase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import layouts.LeftSideBar;
import layouts.NoteBoxArea;
import layouts.TopMenuBar;

/**
 * Created by margus@workstation on 13.11.2015.
 */
public class Main extends Application {

    private static LeftSideBar leftSideBarObj;
    private static TopMenuBar topMenuBarObj;
    private static NoteBoxArea noteBoxAreaObj;
    private static Label messageBox;
    private static DataBase dataBase;
    private static Stage window;
    private static Scene scene;
    private static BorderPane mainLayout;

    public static void main(String[] args){
        launch(args);
    }

    /**
     * Starts application
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        // set window title
        this.window.setTitle("OfflineNotes");
        this.window.getIcons().add(new Image("Images/Logo.png"));
        this.window.setOnCloseRequest(e -> {
            //take over app close command.
            e.consume();
            closeProgram();
        });
        // create new topMenuBar object
        this.topMenuBarObj = new TopMenuBar();
        // get a MenuBar object from class topMenuBar
        final MenuBar topMenuBar = this.topMenuBarObj.getLayout();

        this.leftSideBarObj = new LeftSideBar();
        final VBox leftSideBar = this.leftSideBarObj.getLayout();
        //leftSideBar.setMinWidth(200);
        //leftSideBar.setMaxWidth(200);

        //messageBox
        final HBox messageBoxLayout = new HBox();
        this.messageBox = new Label("");
        messageBoxLayout.getChildren().add(this.messageBox);

        //notebox
        this.noteBoxAreaObj = new NoteBoxArea();
        final VBox noteBoxArea = this.noteBoxAreaObj.getLayout();

        // main layout for the main window
        mainLayout = new BorderPane();
        mainLayout.setTop(topMenuBar);
        mainLayout.setLeft(leftSideBar);
        mainLayout.setBottom(messageBoxLayout);
        mainLayout.setCenter(noteBoxArea);

        //start database
        this.dataBase = new DataBase();
        this.dataBase.readDataFromDisk();
        this.dataBase.readSettingsFromDisk();

        //update tags list
        getLeftSideBarObj().getTagBrowserLayout().createTagTree();

        // create the main scene with mainLayout
        //reade window size and position from settings
        this.scene = new Scene(mainLayout, this.dataBase.getSettings().getStageW(), this.dataBase.getSettings().getStageH());
        //if window position is not available
        if (this.dataBase.getSettings().getStageX() != 0 && this.dataBase.getSettings().getStageY() != 0){
            this.window.setX(this.dataBase.getSettings().getStageX());
            this.window.setY(this.dataBase.getSettings().getStageY());
        }

        scene.getStylesheets().add("/mainPackage/stylesheet.css");
        // set scene for the main stage
        this.window.setScene(scene);
        // display stage
        this.window.show();
    }

    /**
     * calls data write to disk before closing the app
     */
    public static void closeProgram(){
        getDataBase().saveDataToDisk();
        getWindow().close();
    }

    /**
     * closes program without saving changes
     */
    public static void closeProgramWithoutSaving(){
        getWindow().close();
    }

    //==============Getters&Setters============================================================

    public static void setMessageBoxVisibility(boolean display){
        if (display){
            mainLayout.setBottom(messageBox);
        }else{
            mainLayout.setBottom(null);
        }
    }

    public static boolean getMessageBoxVisibility(){
        if (mainLayout.getBottom() == null){
            return false;
        }else{
            return true;
        }
    }

    public static LeftSideBar getLeftSideBarObj() {
        return leftSideBarObj;
    }

    public static TopMenuBar getTopMenuBarObj() {
        return topMenuBarObj;
    }

    public static NoteBoxArea getNoteBoxAreaObj() {
        return noteBoxAreaObj;
    }

    public static void setMessage(String message, boolean alert){
        messageBox.setText(message);
        if (alert){
            messageBox.setStyle("-fx-text-fill: red");
        }else{
            messageBox.setStyle("-fx-text-fill: black");
        }
    }

    public static DataBase getDataBase() {
        return dataBase;
    }

    public static Stage getWindow() {
        return window;
    }

    public static Scene getScene(){
        return scene;
    }

}
