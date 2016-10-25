package noteStuff;

import javafx.scene.control.ListView;
import layouts.TagBrowserLayout;
import mainPackage.Main;

import java.util.ArrayList;

/**
 * Created by margus@workstation on 13.11.2015.
 */
public class NoteBook{

    private String name;
    private ArrayList<Note> notes;
    private ListView<Note> listView;

    /**
     * Constructor
     * If name is passed as an empty string, the group will be named as "UnTitled"
     * @param name name for the group
     */
    public NoteBook(String name){
        if(name == ""){
            this.name = "UnTitled";
        }else{
            this.name = name;
        }
        notes = new ArrayList<>();
        this.listView = new ListView<>();
    }

    //========Note manipulation================================================================

    /**
     * Add a new note to this NoteBook
     */
    public void addNote(){
        Note note = new Note(this.name, Main.getDataBase().getNextNoteID());

        this.notes.add(note);
        this.listView.getItems().add(0, note);
        // add to current NoteBox listView
        Main.getNoteBoxAreaObj().replaceListView(this.listView);
        Main.setMessage("New note added", false);
    }

    /**
     * adds specified note. Used by database after reading files from disk
     * @param note Note
     */
    public void addNoteFromFile(Note note){
        this.notes.add(note);
        this.listView.getItems().add(note);
    }

    /**
     * remove specified Note object from notebook
     * also replaces listView
     * @param note Note
     */
    public void removeNote(Note note){
        this.notes.remove(note);
        this.listView.getItems().remove(note);
        try{
            TagBrowserLayout.createTagTree();
        }catch(Exception e){}
    }

    /**
     * used, if that notebook is a searchbook, and has to be cleared for new results
     */
    public void clearNoteBook(){
        this.notes.clear();
        this.listView.getItems().clear();
    }

    //==============Getters&Setters============================================================

    /**
     * returns name of the notebook
     * @return String
     */
    public String toString(){
        return this.name;
    }

    /**
     * rename notebooks. changes parent value in all notes in this book
     * @param newName String
     */
    public void setName(String newName){
        this.name = newName;
        //rename parentBranch value in all notes in this book
        for (Note note : this.notes){
            note.setParentBranch(newName);
        }
    }

    /**
     * return current LIstView
     * @return ListView
     */
    public ListView<Note> getListView(){
        return this.listView;
    }

    /**
     * returns an arraylist containing notes
     * @return ArrayList
     */
    public ArrayList<Note> getNotesArray(){
        return this.notes;
    }

}
