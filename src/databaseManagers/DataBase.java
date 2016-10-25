package databaseManagers;

import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import layouts.TagBrowserLayout;
import mainPackage.Main;
import mainPackage.Settings;
import messageBoxes.GetNoteSaveOptionsBox;
import noteStuff.Note;
import noteStuff.NoteBook;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by margus@workstation on 14.11.2015.
 */
public class DataBase {

    private ArrayList<NoteBook> notebooks;
    private String fileForData;
    private String fileForSettings;
    private String savePath;
    private Settings settings;

    /**
     * Constructor.
     * Creates ArrayList containig NoteBooks
     */
    public DataBase(){
        this.notebooks = new ArrayList<>();
        this.fileForData = "_notebooks.data";
        this.fileForSettings = "_settings.data";
        //this.savePath = "D:\\Desktop\\";
        this.savePath = "";
    }

    //=======Disk read&write + settings======================================================

    /**
     * prepares note list and settings file for writing
     * calls writing to file
     */
    public void saveDataToDisk(){
        //create an arraylist for notes
        ArrayList<Note> notesList = new ArrayList<>();
        //add notes from all notebooks
        for (NoteBook book : this.notebooks){
            //search results will not be saved
            if (!book.toString().equals("#SearchResult")){
                ArrayList<Note> nList = book.getNotesArray();
                if (nList.size() == 0){
                    notesList.add(new Note("#GhostNote#", book.toString(), "#GhostNote#"));
                }else{
                    for (Note note : nList){
                        notesList.add(note);
                    }
                }
            }
        }
        //write notes to file
        DataWriter dataWriter = new DataWriter(notesList, this.fileForData, this.savePath);
        dataWriter.writeDisk();
        //gather gui selections before writing to disk
        gatherSettings();
        // write settings to file
        DataWriter settingsWriter = new DataWriter(this.settings, this.fileForSettings, this.savePath);
        settingsWriter.writeDisk();
    }

    /**
     * saves given note to txt file
     * asks user which additional data should be included
     * @param note
     */
    public void saveNoteToDisk(Note note){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(note.getName());

        //Show save file dialog
        File file = fileChooser.showSaveDialog(Main.getWindow());

        if(file != null){
            //let user choose which parameters to write along wiht note text
            GetNoteSaveOptionsBox optionsBox = new GetNoteSaveOptionsBox();
            List<Boolean> options = optionsBox.display();
            if (options.size() == 0){
                Main.setMessage("Save to file canceled", true);
            }else{
                //set up data to write
                StringBuilder builder = new StringBuilder();
                if (options.get(0)){
                    builder.append("Heading: " + note.getName() + "\n");
                }
                if (options.get(1)){
                    builder.append("Notebook: " + note.getParentBranch() + "\n");
                }
                if (options.get(2)){
                    builder.append("Note ID: " +  Integer.toString(note.getNoteID()) + "\n");
                }
                if (options.get(3)){
                    builder.append("Time created: " + note.getTimeCreated() + "\n");
                }
                if (options.get(4)){
                    builder.append("Time modified: " +  note.getTimeModified() + "\n");
                }
                if (options.get(5)){
                    builder.append("Note tags: " + Arrays.toString(note.getTags()) + "\n");
                }
                builder.append("Note content:\n" + note.getText());
                //write to file
                DataWriter dataWriter = new DataWriter(builder.toString(), file);
                dataWriter.writeNoteToFile();
            }
        }
    }

    /**
     * collects active settings and writes them to settings object.
     */
    private void gatherSettings(){
        //last opened notebook
        try{
            getSettings().setLastOpenNoteBookID(Main.getLeftSideBarObj().getSelectedTreeItemID());
        }catch (NullPointerException e){
            getSettings().setLastOpenNoteBookID(-1);
        }
        //last opened note
        try{
            getSettings().setLastOpenNoteID(Main.getNoteBoxAreaObj().getSelectedListViewItemIndex());
        }catch (NullPointerException e){
            getSettings().setLastOpenNoteID(-1);
        }
        //noteText text wrap
        getSettings().setWordWrap(Main.getNoteBoxAreaObj().getTextWrap());
        //searchfield case sensitivity
        getSettings().setCaseSensitivity(Main.getNoteBoxAreaObj().isCaseSensitivity());
        //statusbar visibility
        getSettings().setStatusBarVisibility(Main.getMessageBoxVisibility());
        //tag browser visibility
        getSettings().setTagBrowserVisible(Main.getLeftSideBarObj().getTagBrowserVisibility());
        //window parameters
        getSettings().setStageH(Main.getScene().getHeight());
        getSettings().setStageW(Main.getScene().getWidth());
        getSettings().setStageX(Main.getWindow().getX());
        getSettings().setStageY(Main.getWindow().getY());
    }

    /**
     * calls reading from disk.
     * unpacks notes data to correct noteBooks
     */
    public void readDataFromDisk(){
        DataReader dataReader = new DataReader(this, this.fileForData, this.savePath);
        int actualNoteCount = 0;
        //get notes from file
        ArrayList<Note> fileData = dataReader.readData();
        //recreate note structure
        for (Note note : fileData){
            //check if parent notebook exists
            try{
                NoteBook parent = getNoteBookByName(note.getParentBranch().trim());
                if (parent == null){
                    //create the notebook
                    parent = addNoteBookFromFile(note.getParentBranch().trim());
                }
                //notebook exists (now)
                if (!note.getName().equals("#GhostNote#") && !note.getText().equals("#GhostNote#")){
                    parent.addNoteFromFile(note);
                    actualNoteCount++;
                }
            }catch (NullPointerException e){}
        }
        Main.setMessage("Read " + actualNoteCount + " notes from file.", false);
    }

    /**
     * calls reading settings from disk
     */
    public void readSettingsFromDisk(){
        DataReader dataReader = new DataReader(this, this.fileForSettings, this.savePath);
        Settings newSettings = dataReader.readSettings();
        //if there was no settings file, get default settings
        if (newSettings == null){
            newSettings = new Settings();
            //if there are any notes, find the largest noteID
            if (this.notebooks.size() > 0){
                int largestID = Integer.MIN_VALUE;
                for (NoteBook book : this.notebooks){
                    for (Note note : book.getNotesArray()){
                        if (note.getNoteID() > largestID){
                            largestID = note.getNoteID();
                        }
                    }
                }
                newSettings.setNextNoteID(largestID);
            }
        }
        //save new settings to database
        this.settings = newSettings;
        //apply settings
        applySettings();
    }

    /**
     * applies read settings
     */
    public void applySettings(){
        //word wrap
        Main.getTopMenuBarObj().setTextWrap(getSettings().isWordWrap());
        Main.getNoteBoxAreaObj().setTextWrap(getSettings().isWordWrap());
        //search case sensitivity
        Main.getTopMenuBarObj().setCaseSensitiveSearch(getSettings().isCaseSensitivity());
        Main.getNoteBoxAreaObj().setCaseSensitivity(getSettings().isCaseSensitivity());
        //statusbar visibility
        Main.getTopMenuBarObj().setStatusBarVisible(getSettings().isStatusBarVisibility());
        Main.setMessageBoxVisibility(getSettings().isStatusBarVisibility());
        //tag browser visibility
        Main.getTopMenuBarObj().setTagBrowserVisible(getSettings().isTagBrowserVisible());
        Main.getLeftSideBarObj().setTagBrowserVisibility(getSettings().isTagBrowserVisible());
        //last opened notebook
        Main.getLeftSideBarObj().selectTreeItem(getSettings().getLastOpenNoteBookID());
        try{
            Main.getNoteBoxAreaObj().replaceListView(Main.getLeftSideBarObj().getSelectedNoteBook().getListView());
        }catch (NullPointerException e){}
        //last openend note
        Main.getNoteBoxAreaObj().selectListViewItem(getSettings().getLastOpenNoteID());
    }


    //========Note(Books) Manipulation========================================================

    /**
     * creates a new notebook into notebooks arraylist and adds a new treeItem to root treeItem
     * @param name
     */
    public void addNoteBook(String name){
        //modify notebook name if needed:
        String newName = notebookNameValidation(name);

        NoteBook newNoteBook = new NoteBook(newName);
        this.notebooks.add(newNoteBook);
        // add the notebook to TreeView
        TreeItem<NoteBook> treeItem = new TreeItem<>(newNoteBook);
        Main.getLeftSideBarObj().addTreeItem(treeItem);

        Main.setMessage("New notebook \"" + newName + "\" added", false);
    }

    /**
     * add a new notebook to database
     * used when pasting a notebook
     * @param noteBook NoteBook
     */
    public void addNoteBook(NoteBook noteBook){
        //modify name
        NoteBook newBook = noteBook;
        newBook.setName(notebookNameValidation(noteBook.toString().trim() + " copy"));
        this.notebooks.add(newBook);
        // add the notebook to TreeView
        TreeItem<NoteBook> treeItem = new TreeItem<>(newBook);
        Main.getLeftSideBarObj().addTreeItem(treeItem);

        Main.setMessage("New notebook \"" + newBook.toString() + "\" added", false);
    }

    public void renameNoteBook(NoteBook noteBook, String newName){
        if (this.notebooks.contains(noteBook)){
            noteBook.setName(notebookNameValidation(newName));
        }
    }

    /**
     * used, when notebook is cretaed from save file
     * @param name String
     * @return NoteBook
     */
    public NoteBook addNoteBookFromFile(String name){
        NoteBook newNoteBook = new NoteBook(name);
        this.notebooks.add(newNoteBook);
        TreeItem<NoteBook> treeItem = new TreeItem<>(newNoteBook);
        Main.getLeftSideBarObj().addTreeItem(treeItem);
        return newNoteBook;
    }

    /**
     * Since two notebooks cant have the same name, it checks for a valid name.
     * if a name exists, tries adding numbers to the end. 1..2..etc
     * @param name name typed by user
     * @return valid name. original or modified, if needed
     */
    private String notebookNameValidation(String name){
        String validName = name;
        boolean found;
        int counter = 1;
        do{
            found = false;
            for (NoteBook book : this.notebooks){
                if (book.toString().equals(validName)){
                    //check if last characters of existing name are numbers
                    //if are, encrease the number by one
                    String numberCheckedName = checkForNumbersAtTheEnd(validName);
                    if (numberCheckedName.equals(validName)){
                        validName = validName + " " + Integer.toString(counter);
                        counter++;
                    }else{
                        validName = numberCheckedName;
                    }
                    found = true;
                }
            }
        }while(found);
        return validName;
    }

    //TODO rename to private
    /**
     * Used for new notebook name validation
     * check if there are any numbers at the end of the string.
     * if there are, +1 is added to the number, else, returns original string
     * @param name String
     * @return String
     */
    public String checkForNumbersAtTheEnd(String name){
        String newString = "";
        //create char array from given String
        char[] charArray = name.toCharArray();
        String reversedNumber = "";
        //check for numbers. from last to first
        for(int i = charArray.length - 1; i >= 0; i--){
            try{
                int num = Integer.parseInt(Character.toString(charArray[i]));
                reversedNumber = new String(reversedNumber + Integer.toString(num));
            }catch (Exception e){
                break;
            }
        }
        //check result
        if (reversedNumber.length() > 0){
            String number = new StringBuilder(reversedNumber).reverse().toString();
            int numberAsInt = Integer.parseInt(number);
            //create new name, with number encreased by one.
            newString = name.substring(0, name.length() - Integer.toString(numberAsInt + 1).length()) + (numberAsInt + 1);


        }else{
            //return original string, since there was no number at the end
            newString = name;
        }
        //check if any number is found
        return newString;
    }

    /**
     * removes a notebook from arraylist and tree
     * @param name
     */
    public void removeNoteBook(String name, TreeItem<NoteBook> treeItem){
        boolean success = false;
        for(int i = 0; i < notebooks.size(); i++){
            if (notebooks.get(i).toString().equals(name)){
                notebooks.remove(i);
                success = true;
                break;
            }
        }
        if (success){
            Main.getNoteBoxAreaObj().replaceListView(null);
            Main.getLeftSideBarObj().selectNextTreeItem();
            Main.getLeftSideBarObj().removeTreeItem(treeItem);
            Main.setMessage("Notebook \"" + name + "\" removed", false);
            //update tag tree
            try{
                TagBrowserLayout.createTagTree();
            }catch(Exception e){}
        }else
            Main.setMessage("Error: Could not find notebook to remove", true);
    }

    //==============Getters&Setters============================================================

    /**
     * returns settings object
     * @return Settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * find a notebook by its name
     * @param name name of the notebook
     * @return NoteBook
     */
    public NoteBook getNoteBookByName(String name){
        for (NoteBook noteBook : this.notebooks){
            if (noteBook.toString().equals(name)){
                return noteBook;
            }
        }
        return null;
    }

    /**
     * get next available note ID for a new note
     * @return
     */
    public int getNextNoteID(){
        return getSettings().getNextNoteID();
    }

    public ArrayList<NoteBook> getNotebooks(){
        return this.notebooks;
    }

}
