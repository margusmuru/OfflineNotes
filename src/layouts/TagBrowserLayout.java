package layouts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mainPackage.Main;
import noteStuff.Note;
import noteStuff.NoteBook;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by margus@workstation on 22.11.2015.
 */
public class TagBrowserLayout {

    private static VBox layout;
    private static TreeView<String> tree;
    private static TreeItem<String> root;
    private static List<String> uniqueTags;

    public TagBrowserLayout(){
        createLayout();
    }

    public static VBox getLayout() {
        return layout;
    }

    private static void createLayout(){
        layout = new VBox();
        layout.setMinHeight(25);
        layout.setMaxWidth(200);

        root = new TreeItem<>("Tags");
        tree = new TreeView<>(root);
        root.setExpanded(true);
        tree.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<TreeItem<String>>() {
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                                        TreeItem<String> oldItem, TreeItem<String> newItem) {
                        // newItem may be null
                        try{
                            String[] splitString = newItem.getValue().split(" ");
                            Main.getLeftSideBarObj().clearNoteBookSelection();
                            Main.getNoteBoxAreaObj().replaceListView(createListView(splitString[1].toString()));
                        }catch (NullPointerException e){
                            //no new item:
                            Main.getNoteBoxAreaObj().replaceListView(null);
                        }catch (ArrayIndexOutOfBoundsException e){}
                    }
                });

        tree.setShowRoot(true);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //keeps treeView at max height
        VBox.setVgrow(tree, Priority.ALWAYS);
        layout.getChildren().add(tree);
    }

    /**
     * creates or updates tag TreeView
     */
    public static void createTagTree(){
        root.getChildren().clear();
        List<String> tagsList = collectTags();
        for (String el : tagsList){
            root.getChildren().add(new TreeItem<>("(" + Integer.toString(getTagOccurrenceCount(el)) + ") " + el));
        }
    }

    /**
     * gather all unique tags into one arraylist
     * @return ArrayList
     */
    private static List<String> collectTags(){
        uniqueTags = new ArrayList<>();
        for (NoteBook book : Main.getDataBase().getNotebooks()){
            for (Note note : book.getListView().getItems()){
                //there may be no tags at all
                try{
                    for (String tag : note.getTags()){
                        if (!uniqueTags.contains(tag.toString().trim()) && !tag.toString().trim().equals("")){
                            uniqueTags.add(tag.toString().trim());
                        }
                    }
                }catch (NullPointerException e){}
            }
        }
        return uniqueTags;
    }

    /**
     * creates a listview of notes based on given tag
     * @param tag String
     * @return ListView
     */
    private static ListView<Note> createListView(String tag){
        ListView<Note> listView = new ListView<>();
        for (NoteBook book : Main.getDataBase().getNotebooks()){
            for (Note note : book.getListView().getItems()){
                //if note contains the tag, add it to listView
                if (note.getTagsAsString().contains(tag)){
                    listView.getItems().add(note);
                }
            }
        }
        return listView;
    }

    /**
     * calculates, how many notes have specified tag
     * @param tag String
     * @return int
     */
    private static int getTagOccurrenceCount(String tag){
        int count = 0;
        for (NoteBook book : Main.getDataBase().getNotebooks()){
            for (Note note : book.getListView().getItems()){
                //if note contains the tag, add it to listView
                if (note.getTagsAsString().contains(tag)){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * returns latest tag-list
     * @return ArrayList of Strings
     */
    public static List<String> getUniqueTags() {
        return uniqueTags;
    }
}
