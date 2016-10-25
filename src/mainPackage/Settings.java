package mainPackage;

/**
 * Created by margus@workstation on 20.11.2015.
 */
public class Settings implements java.io.Serializable{
    private boolean wordWrap, caseSensitivity, statusBarVisibility, tagBrowserVisible;
    private int lastOpenNoteBookID, lastOpenNoteID, nextNoteID;
    private double stageX, stageY, stageW, stageH;

    /**
     * Constructor
     * Sets default settings
     */
    public Settings(){
        this.wordWrap = false;
        this.caseSensitivity = false;
        this.statusBarVisibility = true;
        this.tagBrowserVisible = true;
        this.lastOpenNoteBookID = 0;
        this.lastOpenNoteID = 0;
        this.nextNoteID = 0;
        this.stageH = 500;
        this.stageW = 800;
        this.stageX = 0;
        this.stageY = 0;
    }

    //==============Getters&Setters============================================================

    public String toString(){
        return "settings object";
    }

    public boolean isWordWrap() {
        return this.wordWrap;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public boolean isCaseSensitivity() {
        return caseSensitivity;
    }

    public void setCaseSensitivity(boolean caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }

    public int getLastOpenNoteBookID() {
        return this.lastOpenNoteBookID;
    }

    public void setLastOpenNoteBookID(int lastOpenNoteBook) {
        this.lastOpenNoteBookID = lastOpenNoteBook;
    }

    public int getLastOpenNoteID() {
        return this.lastOpenNoteID;
    }

    public void setLastOpenNoteID(int lastOpenNoteID) {
        this.lastOpenNoteID = lastOpenNoteID;
    }

    public int getNextNoteID() {
        this.nextNoteID++;
        return this.nextNoteID;
    }

    public void setNextNoteID(int nextNoteID) {
        this.nextNoteID = nextNoteID;
    }

    public double getStageX() {
        return this.stageX;
    }

    public void setStageX(double stageX) {
        this.stageX = stageX;
    }

    public double getStageY() {
        return this.stageY;
    }

    public void setStageY(double stageY) {
        this.stageY = stageY;
    }

    public double getStageW() {
        return this.stageW;
    }

    public void setStageW(double stageW) {
        this.stageW = stageW;
    }

    public double getStageH() {
        return this.stageH;
    }

    public void setStageH(double stageH) {
        this.stageH = stageH;
    }

    public boolean isStatusBarVisibility() {
        return statusBarVisibility;
    }

    public void setStatusBarVisibility(boolean statusBarVisibility) {
        this.statusBarVisibility = statusBarVisibility;
    }

    public boolean isTagBrowserVisible() {
        return tagBrowserVisible;
    }

    public void setTagBrowserVisible(boolean tagBrowserVisible) {
        this.tagBrowserVisible = tagBrowserVisible;
    }
}
