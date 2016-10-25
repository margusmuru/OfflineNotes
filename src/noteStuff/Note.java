package noteStuff;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by margus@workstation on 13.11.2015.
 */
public class Note  implements java.io.Serializable, Cloneable{
    private String name;
    private String parentBranch;
    private String text;
    private int noteID;
    private String timeCreated;
    private String timeModified;
    private String[] tags;

    /**
     * Constructor 1
     * Used for creating a ghost-note
     * @param name name of the note
     * @param parentBranch parent notebook name
     * @param text note content
     */
    public Note(String name, String parentBranch, String text){
        this.name = name;
        this.text = text;
        this.parentBranch = parentBranch;
    }

    /**
     * Constructor 2
     * @param parentBranch name of the parent notebook
     * @param noteID ID of the note
     */
    public Note(String parentBranch, int noteID){
        this.name = "UnTitled";
        this.parentBranch = parentBranch;
        this.noteID = noteID;
        this.text = "";
        this.timeCreated = getCurrentTime();
        this.timeModified = "";
        this.tags = null;
    }

    /**
     * used to duplicate the note object
     * @return Object
     * @throws CloneNotSupportedException
     */
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    /**
     * returns current system time as String
     * @return String
     */
    private String getCurrentTime(){
        Date rightNow = new Date();
        Locale currentLocale = new Locale("en");
        DateFormat timeFormatter;
        timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
        return timeFormatter.format(rightNow);
    }

    //==============Getters&Setters============================================================

    public String toString(){
        return name;
    }

    public Note getClone(){
        try{
            return (Note)clone();
        }catch (Exception e){
            return null;
        }
    }

    public String getParentBranch() {
        return parentBranch;
    }

    public void setParentBranch(String parentBranch) {
        this.parentBranch = parentBranch;
        this.timeModified = getCurrentTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.timeModified = getCurrentTime();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.timeModified = getCurrentTime();
    }

    public int getNoteID() {
        return noteID;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getTimeModified() {
        return timeModified;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTagsAsString(){
        String tagsString = "";
        try{
            for (String el : this.tags){
                tagsString = new String(tagsString + " " + el);
            }
        }catch (NullPointerException e){}
        return tagsString.trim();
    }

    public void setTags(String[] tags) {
        this.tags = tags;
        this.timeModified = getCurrentTime();
    }
}
