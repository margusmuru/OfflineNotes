package layouts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mainPackage.Main;
import messageBoxes.NoteInfoBox;
import noteStuff.Note;
import noteStuff.NoteBook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by margus@workstation on 14.11.2015.
 */
public class NoteBoxArea {

    private ListView<Note> listView;
    private TextArea noteText;
    private TextField noteHeading, tagField, searchField;
    private VBox masterLayout, noteLayout;
    //search
    private boolean caseSensitivity;
    //tagField helpers
    private List<String> suggestionsList;
    private int curSuggestion;
    private SearchAndReplaceModule searchAndReplaceModule;

    /**
     * Constructor
     * Creates NoteBox layout
     */
    public NoteBoxArea(){
        this.listView = new ListView<>();
        this.listView.setMinWidth(200);
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // add listener
        this.listView.getSelectionModel().selectedItemProperty().addListener(e -> {
            setNoteArea();
        });
        this.listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE && listView.isFocused()){
                    Main.getTopMenuBarObj().deleteNoteAction(null);
                }
            }
        });


        //textFields and textAreas
        this.noteHeading = new TextField();
        this.noteHeading.setDisable(true);
        this.noteHeading.setPromptText("Note Heading");
        //handler to rename note if the heading loses focus
        this.noteHeading.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue && !newValue){
                    renameActiveNoteHeading();
                }
            }
        });
        // handler to rename note if the enter key is pressed while the heading is in focus
        this.noteHeading.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                    renameActiveNoteHeading();
                    focusNoteTextField();
                }
            }
        });

        this.noteText = new TextArea();
        this.noteText.setDisable(true);
        this.noteText.setPromptText("Note text...");
        this.noteText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                renameActiveNoteText(newValue);
            }
        });
        //master layout
        this.masterLayout = new VBox();
        this.masterLayout.setStyle("-fx-background-color: gray");


        //Note layout
        this.noteLayout = new VBox();
        this.noteLayout.setMaxWidth(Integer.MAX_VALUE);
        this.noteLayout.setMinWidth(300);

        //tag ribbon
        HBox tagLayout = new HBox();

        this.tagField = createCustomTagField();

        Button infoButton = new Button("info");
        infoButton.setTooltip(new Tooltip("Show information about current note"));
        infoButton.setStyle("-fx-background-color: darkgray");
        infoButton.setOnAction(event1 -> {
            if (getSelectedListViewItem() != null){
                NoteInfoBox.display(getSelectedListViewItem(), Main.getWindow(), infoButton);
            }
        });
        HBox.setHgrow(this.tagField, Priority.ALWAYS);
        tagLayout.getChildren().addAll(this.tagField, infoButton);


        //main separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        //right side layout
        VBox.setVgrow(this.noteText, Priority.ALWAYS);
        this.noteLayout.getChildren().addAll(tagLayout, this.noteHeading, separator, this.noteText);


        //layout under button ribbon
        HBox mainLayout = new HBox();
        mainLayout.setMaxWidth(Integer.MAX_VALUE);
        mainLayout.setPadding(new Insets(0, 5, 0, 5));
        mainLayout.setStyle("-fx-background-color: gray");
        HBox.setHgrow(noteLayout, Priority.ALWAYS);
        mainLayout.getChildren().addAll(this.listView, noteLayout);



        //Button ribbon
        HBox buttonRibbon = new HBox(10);
        buttonRibbon.setAlignment(Pos.CENTER_LEFT);
        buttonRibbon.setPadding(new Insets(5,5,5,5));
        buttonRibbon.setStyle("-fx-background-color: gray");
        //newNote button
        Button newNote = new Button("New Note");
        newNote.setTooltip(new Tooltip("Create a new note in this notebook"));
        newNote.setBorder(Border.EMPTY);
        newNote.setStyle("-fx-background-color: gainsboro");
        newNote.setOnAction(event -> {
            Main.getTopMenuBarObj().newNoteAction(event);
        });
        //search bar
        this.caseSensitivity = false;
        this.searchField = new TextField();
        this.searchField.setTooltip(new Tooltip("Search all notes and content"));
        this.searchField.setPromptText("Search all notes");
        this.searchField.setStyle("-fx-background-color: gray; -fx-border-color: darkgray");
        this.searchField.setMinWidth(250);
        this.searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //if there is a value, search it, else, clear the searchResults
                if (!newValue.equals("")){
                    searchFromNotes(newValue);
                }else{
                    clearSearchResults();
                }
            }
        });
        buttonRibbon.getChildren().addAll(newNote, this.searchField);

        Separator separator1 = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        VBox.setVgrow(mainLayout, Priority.ALWAYS);
        this.masterLayout.getChildren().addAll(buttonRibbon, separator1, mainLayout);
    }


    /**
     * replaces listview content with new. can be null
     * @param listView new listview items
     */
    public void replaceListView(ListView<Note> listView){
        this.listView.getItems().clear();
        if(listView != null){
            this.listView.getItems().addAll(listView.getItems());
        }
        //select first item if list is not empty
        if(this.listView.getItems().isEmpty()){
            this.noteHeading.setText("");
            this.noteText.setText("");
        }else{
            this.listView.getSelectionModel().select(0);
            this.setNoteArea();
        }
    }

    /**
     * select listview item
     * @param index index
     */
    public void selectListViewItem(int index){
        try{
            this.listView.getSelectionModel().clearAndSelect(index);
        }catch (NullPointerException e){}

    }

    /**
     * called when a note is selected in the listView
     * sets noteArea values
     */
    private void setNoteArea(){
        //get currently selected note
        Note curSelected = this.listView.getSelectionModel().getSelectedItem();
        // there may be no selected items
        if (curSelected != null && this.listView.getSelectionModel().getSelectedItems().size() == 1){
            //set heading
            String heading = curSelected.getName();
            if (!heading.equals("UnTitled")){
                this.noteHeading.setText(heading);
            }else{
                this.noteHeading.setText("");
            }
            // set text
            this.noteText.setText(curSelected.getText());
            this.tagField.setText(curSelected.getTagsAsString());
            if (curSelected.getParentBranch().toString().trim().equals("#Trash") ||
                    curSelected.getParentBranch().toString().trim().equals("#SearchResult")){
                setNoteFieldsDisabled(true);
            }else{
                setNoteFieldsDisabled(false);
            }

            focusNoteHeadingField();
        }else{
            setNoteFieldsDisabled(true);
        }
    }

    /**
     * enable or disable note text fields
     * @param value
     */
    private void setNoteFieldsDisabled(boolean value){
        this.noteText.setDisable(value);
        this.noteHeading.setDisable(value);
        this.tagField.setDisable(value);
    }

    /**
     * rename currently selected note´s heading
     */
    private void renameActiveNoteHeading(){
        String newValue = this.noteHeading.getText();
        Note currentNote = this.getSelectedListViewItem();
        try{
            if(!newValue.equals(currentNote.getName()) && !newValue.trim().equals("")){
                try{
                    currentNote.setName(newValue);
                    int index = this.listView.getSelectionModel().getSelectedIndex();
                    this.listView.getItems().remove(index);
                    this.listView.getItems().add(index,currentNote);
                    selectListViewItem(index);
                    Main.setMessage("The note has been renamed to \"" + newValue + "\"", false);
                }catch (NullPointerException e){}
            }
        }catch (NullPointerException e){}
    }

    /**
     * rename currently selected notes content
     * @param newValue
     */
    private void renameActiveNoteText(String newValue){
        Note currentNote = this.getSelectedListViewItem();
        try{
            currentNote.setText(newValue);
        }catch (NullPointerException e){}
    }

    /**
     * request focus for noteText area
     */
    public void focusNoteTextField(){
        this.noteText.requestFocus();
    }

    /**
     * request foxus for noteHeading field
     */
    private void focusNoteHeadingField(){
        this.noteHeading.requestFocus();
    }

    //==============Tags manipulation=======================================================

    /**
     * calls tagString separation into List,
     * saves them as array to the note and refreshes tags TreeView
     */
    private void applyNoteTags(){
        List<String> completeTagsList = separateTagStringToArray(this.tagField.getText().toLowerCase());
        String[] tags = new String[completeTagsList.size()];
        tags = completeTagsList.toArray(tags);
        getSelectedListViewItem().setTags(tags);
        //update tags field
        this.tagField.setText(getSelectedListViewItem().getTagsAsString());
        //update tag tree
        Main.getLeftSideBarObj().getTagBrowserLayout().createTagTree();
    }

    /**
     * separates tag words form spaces and commas,
     * @param tagsFieldValue String containig tags, separated by spaces or commas
     * @return List of Strings
     */
    private List<String> separateTagStringToArray(String tagsFieldValue){
        List<String> completeTagsList = new ArrayList<>();
        // separate by spaces
        String[] separatedTags1 = tagsFieldValue.split(" ");
        // user may separate by commas. do another split
        for (String element1 : separatedTags1){
            String[] newSplit = element1.split(",");
            for(String element2 : newSplit){
                if (!element2.trim().equals("") && !completeTagsList.contains(element2)){
                    completeTagsList.add(element2.toString().trim());
                }
            }
        }
        if (completeTagsList.size() == 0 && tagsFieldValue.trim() != ""){
            completeTagsList.add(tagsFieldValue);
        }

        return completeTagsList;
    }

    /**
     * suggest tag while typing tags
     * @param input String
     */
    private void suggestTag(String input){
        this.suggestionsList = new ArrayList<>();
        List<String> tagsList = separateTagStringToArray(input);
        //make sure there is atleast on element in List
        if (tagsList.size() > 0){
            //get last "word"
            String lastElement = tagsList.get(tagsList.size() - 1);
            //check if not ""
            if (lastElement != null || lastElement.trim() != ""){
                try{
                    for (String existingTag : Main.getLeftSideBarObj().getTagBrowserLayout().getUniqueTags()){
                        if (existingTag.startsWith(lastElement)){
                            //modify textField´s string and select suggestion
                            int endIndex = input.length() - lastElement.length();
                            String output = input.substring(0, endIndex) + existingTag;
                            this.suggestionsList.add(output);
                        }
                    }
                }catch (NullPointerException e){}
            }

        }
        this.curSuggestion = 0;
    }

    /**
     * creates a custom textfield with tag typing suggestions
     * @return TextField
     */
    private TextField createCustomTagField(){
        TextField tagField = new TextField();
        tagField.setDisable(true);
        tagField.setPromptText("Enter tags...");
        tagField.setTooltip(new Tooltip("Add tags to easily find notes later"));
        tagField.setStyle("-fx-background-color: gray");
        tagField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue && !newValue){
                    applyNoteTags();
                }
            }
        });
        //keypress event
        tagField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {
                String newValue = "";

                if (event.getCode() == KeyCode.ENTER){
                    //apply tags to current note and request focus for the heading field
                    applyNoteTags();
                    focusNoteHeadingField();
                }
                //when tab is pressed, complete the word and add space
                if (event.getCode() == KeyCode.TAB){
                    tagField.setText(new String(tagField.getText() + " "));
                    tagField.positionCaret(tagField.getText().length());
                }
                if (event.getCode() == KeyCode.UP){
                    if (curSuggestion - 1  >= 0 && suggestionsList.size() >= 1){
                        curSuggestion--;
                        changeField(newValue);

                    }

                }
                if (event.getCode() == KeyCode.DOWN){
                    if (curSuggestion + 1 < suggestionsList.size()){
                        curSuggestion++;
                        changeField(newValue);

                    }
                }
                //except some keys, when key is pressed, suggest a tag
                if (event.getCode() != KeyCode.SPACE && event.getCode() != KeyCode.COMMA &&
                        event.getCode() != KeyCode.BACK_SPACE && event.getCode() != KeyCode.TAB &&
                        event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN){

                    newValue = tagField.getText();
                    suggestTag(newValue);
                    changeField(newValue);
                }
            }
            private void changeField(String newValue){
                try{
                    tagField.setText(suggestionsList.get(curSuggestion));
                    tagField.positionCaret(newValue.length());
                    tagField.selectRange(newValue.length(), suggestionsList.get(curSuggestion).length());

                }catch (Exception e){}
            }
        });
        //consume some key event for tagField, so pressing tab will complete word instead of losing focus of the field
        final EventHandler<KeyEvent> keyEventHandler =
                keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.TAB || keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
                        keyEvent.consume();
                    }
                };
        tagField.setOnKeyPressed(keyEventHandler);
        return tagField;
    }

    //=============Search=====================================================================

    /**
     * creates a #SearchResults notebook ( if needed ) and fills it with notes based on searchField value;
     * @param searchValue searched text
     */
    private void searchFromNotes(String searchValue){

        //check if notebook "#SearchResults" exists. if not, create it
        NoteBook searchBook = Main.getDataBase().getNoteBookByName("#SearchResult");
        if (searchBook == null){
            Main.getDataBase().addNoteBookFromFile("#SearchResult");
            searchBook = Main.getDataBase().getNoteBookByName("#SearchResult");
        }else{
            //delete all notes in SearchResults
            searchBook.clearNoteBook();
        }
        //search notes for searched term
        for (NoteBook book : Main.getDataBase().getNotebooks()){
            //we must not search the searchbook and trash
            if (book.toString().equals("#SearchResult") ){
                continue;
            }
            //check each note
            for (Note note : book.getNotesArray()){
                //case sensitivity parameter changes few things around here
                if (!this.caseSensitivity){
                    if (note.getName().toLowerCase().contains(searchValue.toLowerCase())
                            || note.getTagsAsString().toLowerCase().contains(searchValue.toLowerCase())
                            || note.getText().toLowerCase().contains(searchValue.toLowerCase())){
                        searchBook.addNoteFromFile(note);
                    }
                }else{
                    if (note.getName().contains(searchValue) || note.getTagsAsString().contains(searchValue)
                            || note.getText().contains(searchValue)){
                        searchBook.addNoteFromFile(note);
                    }
                }
            }
        }
        Main.getNoteBoxAreaObj().replaceListView(searchBook.getListView());
        this.searchField.requestFocus();
    }

    /**
     * clear search results when search field becomes empty
     */
    private void clearSearchResults(){
        NoteBook searchBook = Main.getDataBase().getNoteBookByName("#SearchResult");
        if (searchBook != null){
            searchBook.clearNoteBook();
            Main.getNoteBoxAreaObj().replaceListView(searchBook.getListView());
        }
    }

    public void showSearchAndReplaceBar(boolean withReplace){
        if (this.searchAndReplaceModule!= null && this.noteLayout.getChildren().contains(this.searchAndReplaceModule.getLayout())){
            this.noteLayout.getChildren().remove(this.searchAndReplaceModule.getLayout());
        }
        this.searchAndReplaceModule = new SearchAndReplaceModule(this.noteText, this.noteLayout, withReplace);
        this.noteLayout.getChildren().addAll(searchAndReplaceModule.getLayout());
        searchAndReplaceModule.focusSearchBar();
    }

    //==============Getters&Setters============================================================

    /**
     * return NoteBox layout
     * @return
     */
    public VBox getLayout(){
        return this.masterLayout;
    }

    /**
     * set text wrap parameter for noteText area
     * @param value
     */
    public void setTextWrap(boolean value){
        this.noteText.setStyle("-fx-wrap-text: " + value);
    }

    /**
     * get text wrap property
     * @return boolean
     */
    public boolean getTextWrap(){
        return this.noteText.isWrapText();
    }

    /**
     * get currently selected note as Note
     * @return
     */
    public Note getSelectedListViewItem(){
        return this.listView.getSelectionModel().getSelectedItem();
    }

    /**
     * get index of the currently selected listview item
     * @return int
     */
    public int getSelectedListViewItemIndex(){
        return this.listView.getSelectionModel().getSelectedIndex();
    }

    /**
     * set searchbar case sensitivity
     * @param value
     */
    public void setCaseSensitivity(boolean value){
        this.caseSensitivity = value;
    }

    /**
     * get searchfield caseSensitivity value
     * @return
     */
    public boolean isCaseSensitivity() {
        return caseSensitivity;
    }

    /**
     * returns all currently selected listview items as observable list
     * @return
     */
    public ObservableList<Note> getSelectedListViewItems(){
        return this.listView.getSelectionModel().getSelectedItems();
    }

    /**
     * returns whether the listview is in focus
     * @return boolean
     */
    public boolean isListViewFocused(){
        return this.listView.isFocused();
    }

    /**
     * IF noteHeading, noteText or TagField is focused, returns true
     * @return Boolean
     */
    public boolean isAnyTextFieldFocused(){
        if (this.noteHeading.isFocused() || this.noteText.isFocused() || this.tagField.isFocused()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns selected text from note heading, content or tagfield
     * @return String
     */
    public String getSelectedText(){
        if (this.noteHeading.isFocused()){
            return this.noteHeading.getSelectedText();
        }else if(this.noteText.isFocused()){
            return this.noteText.getSelectedText();
        }else if (this.tagField.isFocused()){
            return this.tagField.getSelectedText();
        }else{
            return "";
        }
    }

    /**
     * inserts given text to caret position
     * caret can be at noteheading, notecontent or tagfield
     * @param valueToInsert
     */
    public void insertTextToCaretPosition(String valueToInsert){
        //heading
        if (this.noteHeading.isFocused()){
            int caretPos = this.noteHeading.caretPositionProperty().get();
            this.noteHeading.setText(this.noteHeading.getText().substring(0, caretPos) + valueToInsert +
                    this.noteHeading.getText().substring(caretPos, this.noteHeading.getText().length()));
            this.noteHeading.positionCaret(caretPos + valueToInsert.length());
        }//note content
        else if (this.noteText.isFocused()){
            int caretPos = this.noteText.caretPositionProperty().get();
            this.noteText.setText(this.noteText.getText().substring(0, caretPos) + valueToInsert +
                    this.noteText.getText().substring(caretPos, this.noteText.getText().length()));
            this.noteText.positionCaret(caretPos + valueToInsert.length());
        }//tag field
        else if (this.tagField.isFocused()){
            int caretPos = this.tagField.caretPositionProperty().get();
            this.tagField.setText(this.tagField.getText().substring(0, caretPos) + valueToInsert +
                    this.tagField.getText().substring(caretPos, this.tagField.getText().length()));
            this.tagField.positionCaret(caretPos + valueToInsert.length());
        }
    }

    /**
     * Inserts current date or time to caret position
     * @param value
     */
    public void insertDateTime(String value){
        String valueToInsert = "";
        Date date = new Date();
        if (value.equals("Time")){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            valueToInsert = dateFormat.format(date);
        }else if (value.equals("Date")){
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            valueToInsert = dateFormat.format(date);
        }else{
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            valueToInsert = dateFormat.format(date);
        }
        //choose where to insert it
        insertTextToCaretPosition(valueToInsert);
    }

}
