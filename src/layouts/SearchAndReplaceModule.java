package layouts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mainPackage.Main;

/**
 * Created by margus@workstation on 01.12.2015.
 */
public class SearchAndReplaceModule {

    private TextArea textArea;
    private TextField searchField, replaceField;
    private VBox mainLayout;
    private int lastSearchPos = 0;
    private String lastSearchTerm = "";

    /**
     * Constructor
     * @param textArea source TextArea
     * @param hostLayout parent Layout for the layout returned by this class
     * @param replace whether the replace bar must be created
     */
    public SearchAndReplaceModule(TextArea textArea,VBox hostLayout, boolean replace){
        this.mainLayout = new VBox();
        this.mainLayout.setPadding(new Insets(2,2,2,2));
        this.textArea = textArea;
        this.createSearchLayout(hostLayout);
        if (replace){
            this.createReplaceLayout();
        }
    }

    /**
     * empty constructor
     */
    public SearchAndReplaceModule(){

    }

    public VBox getLayout(){
        return this.mainLayout;
    }

    /**
     * Creates the Search Bar with buttons, fields and actions
     * @param hostLayout parent VBox
     */
    private void createSearchLayout(VBox hostLayout){
        HBox topBarLayout = new HBox();
        HBox searchLayout = new HBox(2);

        this.searchField = new TextField();
        this.searchField.setPromptText("Search term");
        this.searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //if there is a value, search it, else, clear the searchResults
                if (!newValue.equals("")){
                    searchNext();
                }
                lastSearchPos = 0;
                lastSearchTerm = "";
            }
        });

        Button searchButton = new Button("Search Next  ");
        searchButton.setOnAction(event -> {
            //what to do when button is clicked
            this.lastSearchPos++;
            searchNext();
            Main.getNoteBoxAreaObj().focusNoteTextField();
        });

        Button closeButton = new Button("X");
        closeButton.setOnAction(event -> {
            if (hostLayout.getChildren().contains(this.mainLayout)){
                hostLayout.getChildren().remove(this.mainLayout);
            }
        });

        searchLayout.getChildren().addAll(this.searchField, searchButton);
        HBox.setHgrow(searchLayout, Priority.ALWAYS);
        topBarLayout.getChildren().addAll(searchLayout, closeButton);


        this.mainLayout.getChildren().addAll(topBarLayout);
    }

    /**
     * Creates the replace bar with Buttons, Fields and actions
     */
    private void createReplaceLayout(){
        HBox replaceLayout = new HBox(2);
        replaceLayout.setPadding(new Insets(2,0,0,0));
        //replacefield
        this.replaceField = new TextField();
        this.replaceField.setPromptText("Replace with...");

        Button replaceButton = new Button("Replace Next");
        replaceButton.setOnAction(event -> {
            replaceNext();
            searchNext();
        });
        Button replaceAllButton = new Button("Replace All");
        replaceAllButton.setOnAction(event -> {
            replaceAll();
        });
        replaceLayout.getChildren().addAll(this.replaceField, replaceButton);
        this.mainLayout.getChildren().add(replaceLayout);
    }

    /**
     * Searches TextArea for the term in searchField, if found highlights the next one
     */
    private void searchNext(){
        String textSource = formatSourceText(textArea.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());
        String searchTerm = formatSearchText(searchField.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());

        int startIndex = textSource.indexOf(searchTerm, this.lastSearchPos);
        int endIndex = startIndex + searchTerm.length();
        if (startIndex >= 0){
            this.lastSearchPos = startIndex;
            this.lastSearchTerm = searchTerm;
            this.textArea.selectRange(startIndex, endIndex);
        }else if(textSource.contains(searchTerm)){
            this.lastSearchPos = 0;
            searchNext();
        }else{
            this.textArea.selectRange(0,0);
        }

    }

    /**
     * Searches and replaces next found matching word with a new word in replaceField
     * Also highLights next matching word
     */
    private void replaceNext(){
        String textSource = formatSourceText(textArea.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());
        String searchTerm = formatSearchText(searchField.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());

        int startIndex = textSource.indexOf(searchTerm, lastSearchPos);
        int endIndex = startIndex + searchField.getText().length();
        if (startIndex >= 0){
            lastSearchPos = startIndex;
            lastSearchTerm = searchTerm;
            textArea.setText(textArea.getText().substring(0, startIndex) + replaceField.getText()
                    + textArea.getText().substring(endIndex, textArea.getText().length()));
        }else if(textArea.getText().contains(searchField.getText())){
            lastSearchPos = 0;
            searchNext();
        }else{
            textArea.selectRange(0,0);
        }
    }

    /**
     * replaces all intances of matching text with replaceTerm
     */
    private void replaceAll(){
        String textSource = formatSourceText(textArea.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());
        String searchTerm = formatSearchText(searchField.getText(), Main.getTopMenuBarObj().getCaseSensitiveSearch());

        String replaceTerm = this.replaceField.getText();
        int matchCount = 0;
        while(textSource.contains(searchTerm)){
            int startIndex = textSource.indexOf(searchTerm, 0);
            int endIndex = startIndex + searchTerm.length();
            if (startIndex >= 0){
                this.textArea.setText(textArea.getText().substring(0, startIndex) + replaceTerm
                        + textArea.getText().substring(endIndex, textArea.getText().length()));
                matchCount++;
            }
        }
        Main.setMessage("Replaced " + matchCount + " intances of \"" + searchField.getText() +
                "\" with \"" + replaceTerm + "\"" , false);
    }

    /**
     * Focuses the searchBar
     */
    public void focusSearchBar(){
        this.searchField.requestFocus();
    }

    //TODO rename to private
    public String formatSourceText(String text, boolean caseSensitive){
        String textSource;
        if (caseSensitive){
            textSource = text;
        }else{
            textSource = text.toLowerCase();
        }
        return textSource;
    }

    //TODO rename to private
    public String formatSearchText(String text, boolean caseSensitive){
        String searchTerm;
        if (caseSensitive){
            searchTerm = text;
        }else{
            searchTerm = text.toLowerCase();
        }
        return searchTerm;
    }
}
