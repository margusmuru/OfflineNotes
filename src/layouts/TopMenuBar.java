package layouts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import mainPackage.Main;
import messageBoxes.*;
import noteStuff.Note;
import noteStuff.NoteBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by margus@workstation on 13.11.2015.
 */
public class TopMenuBar {

    private Menu fileMenu, editMenu, helpMenu, viewMenu, insertMenu;
    private CheckMenuItem textWrap, caseSensitiveSearch, statusBarVisible, tagBrowserVisible;
    List<Object> clipBoard;

    /**
     * Constructor
     */
    public TopMenuBar(){
        this.createFileMenu();
        this.createEditMenu();
        this.createHelpMenu();
        this.createViewMenu();
        this.createInsertMenu();
        this.clipBoard = new ArrayList<>();
    }

    /**
     * Create top menu bar with menus
     * @return JavaFX MenuBar object
     */
    public MenuBar getLayout(){
        MenuBar topMenu = new MenuBar();
        topMenu.getMenus().addAll(fileMenu, editMenu, viewMenu, insertMenu, helpMenu);
        return topMenu;
    }

    //=============File Menu================================================================================

    /**
     * Creates the File menu and its items
     * contains actionevents to show/hide elements
     */
    private void createFileMenu(){
        this.fileMenu = new Menu("_File");
        //new note
        MenuItem newNote = new MenuItem("New Note");
        newNote.setOnAction(this::newNoteAction);
        newNote.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        //new notebook
        MenuItem newNoteBook = new MenuItem("New NoteBook...");
        newNoteBook.setOnAction(this::newNoteBookAction);
        //save note to file
        MenuItem saveNote = new MenuItem("Save note...");
        saveNote.setOnAction(this::saveNoteAction);
        saveNote.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        //save database
        MenuItem save = new MenuItem("Save changes");
        save.setOnAction(event -> Main.getDataBase().saveDataToDisk());
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));
        //exit
        MenuItem exit = new MenuItem("_Exit");
        exit.setOnAction(event -> Main.closeProgram());
        exit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        //exit without saving
        MenuItem exitNoSave = new MenuItem("Exti without saving");
        exitNoSave.setOnAction(event -> Main.closeProgramWithoutSaving());
        exitNoSave.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));

        this.fileMenu.showingProperty().addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
            {
                if(newValue.booleanValue()){
                    //to add a note, a notebook must be selected and cannot be trash nor searhcresults
                    if (Main.getLeftSideBarObj().getSelectedNoteBook() == null ||
                            Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals("#Trash") ||
                            Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals(("#SearchResult"))){
                        newNote.setDisable(true);
                    }else{
                        newNote.setDisable(false);
                    }
                    //a note must be selected to be able to save it
                    if (Main.getNoteBoxAreaObj().getSelectedListViewItem() == null){
                        saveNote.setDisable(true);
                    }else{
                        saveNote.setDisable(false);
                    }
                }
            }
        });

        //add elements to the fileMenu
        this.fileMenu.getItems().addAll(newNote, newNoteBook, new SeparatorMenuItem());
        this.fileMenu.getItems().addAll(saveNote, save, new SeparatorMenuItem() ,exitNoSave, exit);

    }

    /**
     * called when user presses "New Note" in fileMenu
     * @param event
     */
    public void newNoteAction(ActionEvent event) {
        try{
            NoteBook selectedNoteBook = Main.getLeftSideBarObj().getSelectedNoteBook();
            //you cannot add new notes to trash or search list
            if (selectedNoteBook.toString().equals("#Trash")){
                Main.setMessage("You cannot create new notes for trash!", true);
            }else if(selectedNoteBook.toString().equals("#SearchResult")) {
                Main.setMessage("You cannot create new notes in search results!", true);
            }else{
                selectedNoteBook.addNote();
            }
        }catch (NullPointerException e){
            Main.setMessage("A NoteBook must be selected", true);
        }
    }

    /**
     * called when user presses "New NoteBook" in fileMenu or buttonbar
     * @param event
     */
    public void newNoteBookAction(ActionEvent event){
        //call out GetNameMessageBox to get name for the new notebook
        String nameForNoteBook = GetNameMessageBox.display("Set name", "Type a name for the notebook");
        //notebook must have a name. also cannot be cancel-message
        if(nameForNoteBook.trim().length() > 0 && nameForNoteBook != "#cancel" && nameForNoteBook != "#Trash" && nameForNoteBook != "#SearchResult"){
            Main.getDataBase().addNoteBook(nameForNoteBook.trim());
        }else if(nameForNoteBook != "#cancel"){
            Main.setMessage("NoteBook must have a name", true);
        }
    }

    /**
     * save note to file
     * passes command to DataBase
     * @param event
     */
    private void saveNoteAction(ActionEvent event){
        Main.getDataBase().saveNoteToDisk(Main.getNoteBoxAreaObj().getSelectedListViewItem());
    }

    //==============Edit Menu================================================================================

    /**
     * Creates the Edit menu and its buttons
     * contains actionevents to show/hide elements
     */
    private void createEditMenu(){
        this.editMenu = new Menu("_Edit");
        //remove note
        MenuItem deleteItem = new MenuItem("Delete Note");
        deleteItem.setOnAction(this::deleteNoteAction);
        //copy
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(this::copyAction);
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        //paste
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(this::pasteAction);
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        //move note
        MenuItem moveItem = new MenuItem("Move note");
        moveItem.setOnAction(this::moveNoteAction);
        moveItem.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));
        //find
        MenuItem findItem = new MenuItem("Find");
        findItem.setOnAction(this::findAction);
        findItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        //replace
        MenuItem replaceItem = new MenuItem("Replace");
        replaceItem.setOnAction(this::replaceAction);
        replaceItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        //rename notebook
        MenuItem renameBookItem = new MenuItem("Rename Notebook");
        renameBookItem.setOnAction(this::renameNoteBookAction);
        //remove notebook
        MenuItem deleteBookItem = new MenuItem("Delete Notebook");
        deleteBookItem.setOnAction(this::deleteNoteBookAction);

        this.editMenu.showingProperty().addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
            {
                if(newValue.booleanValue()){
                    // you cannot do some stuff, if a note is not selected
                    if (Main.getNoteBoxAreaObj().getSelectedListViewItem() == null){
                        deleteItem.setDisable(true);
                        moveItem.setDisable(true);
                    }else{
                        deleteItem.setDisable(false);
                        moveItem.setDisable(false);
                    }
                    // you cannot do some stuff, if a notebook is not selected
                    if (Main.getLeftSideBarObj().getSelectedNoteBook() == null){
                        renameBookItem.setDisable(true);
                        deleteBookItem.setDisable(true);
                    }else if (Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals("#Trash") ||
                            Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals("#SearchResult")){
                        renameBookItem.setDisable(true);
                    }else{
                        if (!Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals("#Trash") &&
                                !Main.getLeftSideBarObj().getSelectedNoteBook().toString().trim().equals("#SearchResult")){
                            renameBookItem.setDisable(false);
                        }
                        deleteBookItem.setDisable(false);
                    }
                    //copy&paste
                    if (Main.getLeftSideBarObj().isTreeViewFocused() || Main.getNoteBoxAreaObj().isListViewFocused() ||
                            Main.getNoteBoxAreaObj().isAnyTextFieldFocused()){
                        //copy
                        if (Main.getLeftSideBarObj().isTreeViewFocused()){
                            copyItem.setText("Copy <NoteBook>");
                        }else if (Main.getNoteBoxAreaObj().isListViewFocused()){
                            copyItem.setText("Copy <Note(s)>");
                        }else{
                            copyItem.setText("Copy <Text>");
                        }
                        copyItem.setDisable(false);

                        if (clipBoard.size() > 0){
                            //check what type of objekt the clipboard is holding
                            if (clipBoard.get(0) instanceof Note){
                                pasteItem.setText("Paste <Note(s)>");
                            }
                            if (clipBoard.get(0) instanceof NoteBook){
                                pasteItem.setText("Paste <NoteBook>");
                            }
                            if (clipBoard.get(0) instanceof String){
                                pasteItem.setText("Paste <Text>");
                            }
                            pasteItem.setDisable(false);
                        }else{
                            pasteItem.setText("Paste");
                            pasteItem.setDisable(true);
                        }
                    }else{
                        copyItem.setDisable(true);
                        copyItem.setText("Copy");
                        pasteItem.setDisable(true);
                        pasteItem.setText("Paste");
                    }
                    //find and replace
                    findItem.setDisable(Main.getNoteBoxAreaObj().getSelectedListViewItems().size() == 0);
                    replaceItem.setDisable(Main.getNoteBoxAreaObj().getSelectedListViewItems().size() == 0);
                } //if menu clicked
            }//changed method
        });

        editMenu.getItems().addAll(renameBookItem, copyItem, pasteItem, moveItem, new SeparatorMenuItem(),findItem, replaceItem,
                new SeparatorMenuItem(), deleteItem, deleteBookItem);
    }

    /**
     * call when user presses "Remove NoteBook"
     * @param event actionEvent
     */
    public void deleteNoteAction(ActionEvent event){
        //check #trash existence
        NoteBook trash = Main.getDataBase().getNoteBookByName("#Trash");
        if (trash == null){
            //currenlty selected notebook will lose focus after creating trash. lets create an override
            TreeItem<NoteBook> tmp = Main.getLeftSideBarObj().getSelectedTreeItem();
            //add note
            Main.getDataBase().addNoteBookFromFile("#Trash");
            trash = Main.getDataBase().getNoteBookByName("#Trash");
            //re-focus previously selected book
            Main.getLeftSideBarObj().selectTreeItem(tmp);
        }
        //list of notes to delete
        ObservableList<Note> notesToDelete = Main.getNoteBoxAreaObj().getSelectedListViewItems();
        NoteBook curNoteBook = null;

        for (Note noteItem : notesToDelete){
            //check if a note is selected
            if(noteItem != null){
                //get current notebook
                curNoteBook = Main.getDataBase().getNoteBookByName(noteItem.getParentBranch());
                //move note to trash only from standard notebook
                if (!curNoteBook.toString().equals("#Trash")){
                    noteItem.setParentBranch("#Trash");
                    noteItem.setName("#Trash:" + noteItem.getName());
                    trash.addNoteFromFile(noteItem);
                }
                //remove note
                curNoteBook.removeNote(noteItem);

            }
        }
        //refresh listView
        if (curNoteBook != null){
            Main.getNoteBoxAreaObj().replaceListView(curNoteBook.getListView());
        }

    }

    public void copyAction(ActionEvent event){
        //clear clipboard
        this.clipBoard.clear();
        //check what is currenlty selected
        if (Main.getNoteBoxAreaObj().isListViewFocused()){
            //copy selected listview items into clipboard
            for (Note note : Main.getNoteBoxAreaObj().getSelectedListViewItems()){
                this.clipBoard.add(note);
            }
        }
        else if (Main.getLeftSideBarObj().isTreeViewFocused()){
            //copy notebook to clipboard
            this.clipBoard.add(Main.getLeftSideBarObj().getSelectedNoteBook());
        }else if (Main.getNoteBoxAreaObj().isAnyTextFieldFocused()){
            this.clipBoard.add(Main.getNoteBoxAreaObj().getSelectedText());
        }
    }

    public void pasteAction(ActionEvent event){
        //check what is on paste value
        if (this.clipBoard.size() > 0){
            //clipboard contains notes
            if (this.clipBoard.get(0) instanceof Note){
                //get selected notebook
                NoteBook selectedBook = Main.getLeftSideBarObj().getSelectedNoteBook();
                for (Object note : this.clipBoard){
                    Note curNote = (Note)note;
                    Note newNote = curNote.getClone();
                    if (newNote != null){
                        newNote.setName(newNote.getName() + " copy");
                        selectedBook.addNoteFromFile(newNote);
                    }
                }
                //refresh listview
                Main.getNoteBoxAreaObj().replaceListView(selectedBook.getListView());
            }
            //clipboard contains a notebook
            if (this.clipBoard.get(0) instanceof NoteBook){
                NoteBook curBook = Main.getLeftSideBarObj().getSelectedNoteBook();
                //name will be validated when added to database
                NoteBook newBook = new NoteBook(curBook.toString().trim());
                //clone notes
                for(int i = curBook.getNotesArray().size(); i > 0; i--){
                    Note newNote = curBook.getNotesArray().get(i - 1).getClone();
                    if (newNote != null){
                        newBook.addNoteFromFile(newNote);
                    }
                }
                Main.getDataBase().addNoteBook(newBook);
            }
            if (this.clipBoard.get(0) instanceof String){
                Main.getNoteBoxAreaObj().insertTextToCaretPosition((String)this.clipBoard.get(0));
            }
        }
    }

    public void findAction(ActionEvent event){
        Main.getNoteBoxAreaObj().showSearchAndReplaceBar(false);
    }

    public void replaceAction(ActionEvent event){
        Main.getNoteBoxAreaObj().showSearchAndReplaceBar(true);
    }

    /**
     * moves selected notes to a different notebook
     * @param event
     */
    public void moveNoteAction(ActionEvent event){
        NoteBook destinationBook;
        NoteBook currentBook = Main.getLeftSideBarObj().getSelectedNoteBook();

        if (currentBook != null){
            MoveNoteDialogBox dialogBox = new MoveNoteDialogBox();
            destinationBook = dialogBox.display(Main.getDataBase().getNotebooks(), currentBook.toString());

            if (destinationBook != null){
                ObservableList<Note> notesToMove = Main.getNoteBoxAreaObj().getSelectedListViewItems();
                for (Note note : notesToMove){
                    //remove from current book and set new parent
                    currentBook.removeNote(note);
                    note.setParentBranch(destinationBook.toString());
                    //rename note heading if moving from trash
                    if (currentBook.toString().equals("#Trash") && note.getName().startsWith("#Trash:")){
                        note.setName(note.getName().substring(7, note.getName().length()));
                    }
                    destinationBook.addNoteFromFile(note);
                }
                Main.getNoteBoxAreaObj().replaceListView(currentBook.getListView());
            }
        }
    }

    /**
     * starts notebook rename sequence. asks user for a new name, if name is valid, renames notebook.
     * @param event
     */
    private void renameNoteBookAction(ActionEvent event){
        TreeItem treeItem = Main.getLeftSideBarObj().getSelectedTreeItem();
        String nameForNoteBook = GetNameMessageBox.display("Rename", "Type a new name for the notebook");
        if(nameForNoteBook.trim().length() > 0 && nameForNoteBook != "#cancel"
                && nameForNoteBook != "#Trash" && nameForNoteBook != "#SearchResult"){
            Main.getDataBase().renameNoteBook((NoteBook)treeItem.getValue(), nameForNoteBook.trim());
            //replace treeItem
            Main.getLeftSideBarObj().renameTreeItem(treeItem);
        }else if(nameForNoteBook != "#cancel"){
            Main.setMessage("NoteBook must have a name", true);
        }
    }

    /**
     * delete currently selected notebook.
     * @param event actionEvent
     */
    public void deleteNoteBookAction(ActionEvent event){
        TreeItem treeItem = Main.getLeftSideBarObj().getSelectedTreeItem();
        boolean confirm = GetConfirmationBox.display("Confirmation", "Are you sure you want to delete selected Notebook? \n" +
                " It will also delete all notes in that Notebook!");
        if (treeItem != null && confirm){
            Main.getDataBase().removeNoteBook(treeItem.getValue().toString(), treeItem);
        }
    }

    //============Insert Menu==================================================================================

    /**
     * Creates the Insert menu and its buttons
     * contains actionevents to show/hide elements
     */
    private void createInsertMenu(){
        this.insertMenu = new Menu("_Insert");
        //Date
        MenuItem dateItem = new MenuItem("Insert Date");
        dateItem.setOnAction(this::dateAction);
        //Time
        MenuItem timeItem = new MenuItem("Insert Time");
        timeItem.setOnAction(this::timeAction);
        //Date and Time
        MenuItem dateTimeItem = new MenuItem("Insert Date & Time");
        dateTimeItem.setOnAction(this::dateTimeAction);

        this.insertMenu.showingProperty().addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
            {
                if(newValue.booleanValue()){
                    //disable menu items if note heading nor content area is not in focus
                    if (Main.getNoteBoxAreaObj().isAnyTextFieldFocused()){
                        dateItem.setDisable(false);
                        timeItem.setDisable(false);
                        dateTimeItem.setDisable(false);
                    }else{
                        dateItem.setDisable(true);
                        timeItem.setDisable(true);
                        dateTimeItem.setDisable(true);
                    }
                } //if menu clicked
            }//changed method
        });

        this.insertMenu.getItems().addAll(dateItem, timeItem, dateTimeItem);
    }

    private void dateAction(ActionEvent event){
        Main.getNoteBoxAreaObj().insertDateTime("Date");
    }

    private void timeAction(ActionEvent event){
        Main.getNoteBoxAreaObj().insertDateTime("Time");
    }

    private void dateTimeAction(ActionEvent event){
        Main.getNoteBoxAreaObj().insertDateTime("DateTime");
    }

    //============View Menu==================================================================================

    private void createViewMenu(){
        this.viewMenu = new Menu("_View");

        this.textWrap = new CheckMenuItem("Wrap text");
        this.textWrap.setSelected(false);
        this.textWrap.setOnAction(this::textWrapAction);
        this.textWrap.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCodeCombination.ALT_DOWN));

        this.caseSensitiveSearch = new CheckMenuItem("Case-sensitive search");
        this.textWrap.setSelected(false);
        this.caseSensitiveSearch.setOnAction(this::caseSensitiveAction);
        this.caseSensitiveSearch.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.ALT_DOWN));

        this.statusBarVisible = new CheckMenuItem("Status Bar");
        this.statusBarVisible.setSelected(true);
        this.statusBarVisible.setOnAction(this::statusBarAction);
        this.statusBarVisible.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.ALT_DOWN));

        this.tagBrowserVisible = new CheckMenuItem("Tag Browser");
        this.tagBrowserVisible.setSelected(true);
        this.tagBrowserVisible.setOnAction(this::tagBrowserAction);
        this.tagBrowserVisible.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCodeCombination.ALT_DOWN));

        this.viewMenu.getItems().addAll(this.textWrap, this.caseSensitiveSearch,
                this.tagBrowserVisible, this.statusBarVisible);
    }

    private void textWrapAction(ActionEvent event){
        if (this.textWrap.isSelected()){
            Main.getNoteBoxAreaObj().setTextWrap(true);
        }else{
            Main.getNoteBoxAreaObj().setTextWrap(false);
        }
    }

    private void caseSensitiveAction(ActionEvent event){
        if (this.caseSensitiveSearch.isSelected()){
            Main.getNoteBoxAreaObj().setCaseSensitivity(true);
        }else{
            Main.getNoteBoxAreaObj().setCaseSensitivity(false);
        }
    }

    private void statusBarAction(ActionEvent event){
        Main.setMessageBoxVisibility(this.statusBarVisible.isSelected());
    }

    private void tagBrowserAction(ActionEvent event){
        Main.getLeftSideBarObj().setTagBrowserVisibility(this.tagBrowserVisible.isSelected());
    }

    /**
     * called by DataBase.applySettings()
     * @param value boolean
     */
    //========Setters=============
    public void setStatusBarVisible(boolean value){
        this.statusBarVisible.setSelected(value);
    }

    public void setTagBrowserVisible(boolean value){
        this.tagBrowserVisible.setSelected(value);
    }

    public void setTextWrap(boolean value){
        this.textWrap.setSelected(value);
    }

    public void setCaseSensitiveSearch(boolean value) {
        this.caseSensitiveSearch.setSelected(value);
    }

    public boolean getCaseSensitiveSearch() {
        return caseSensitiveSearch.isSelected();
    }

    //=============Help Menu===============================================================================

    /**
     * Creates the Help menu
     */
    private void createHelpMenu(){
        this.helpMenu = new Menu("_Help");

        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(event -> {
            AboutBox.display();
        });

        this.helpMenu.getItems().addAll(aboutItem);
    }

}
