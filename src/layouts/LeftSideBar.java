package layouts;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mainPackage.Main;
import noteStuff.NoteBook;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Comparator;

/**
 * Created by margus@workstation on 13.11.2015.
 */
public class LeftSideBar {

    private TreeView<NoteBook> tree;
    private TreeItem<NoteBook> root;
    private VBox layout;
    private TagBrowserLayout tagBrowserLayout;
    private SplitPane sp;
    private VBox tagBox;

    /**
     * Constructor
     * Creates the left sidebar elements
     */
    public LeftSideBar(){
        this.layout = new VBox();

        this.root = new TreeItem<>(new NoteBook("root"));
        root.setExpanded(true);

        //new tree
        this.tree = new TreeView<>(this.root);
        this.tree.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE && tree.isFocused()){
                    Main.getTopMenuBarObj().deleteNoteBookAction(null);
                }
            }
        });
        this.tree.setShowRoot(false);
        this.tree.setPrefWidth(200);
        this.tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //listener to replace listview content whne a notebook is selected
        this.tree.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<TreeItem<NoteBook>>() {
            public void changed(ObservableValue<? extends TreeItem<NoteBook>> observableValue,
                                TreeItem<NoteBook> oldItem, TreeItem<NoteBook> newItem) {
                // newItem may be null
                try{
                    //new item:
                    if (newItem.getValue().getNotesArray().size() > 0){
                        Main.getNoteBoxAreaObj().replaceListView(newItem.getValue().getListView());
                    }else{
                        Main.getNoteBoxAreaObj().replaceListView(null);
                    }

                }catch (NullPointerException e){
                    //no new item:
                    Main.getNoteBoxAreaObj().replaceListView(null);
                }
            }
        });

        //elements above treeView
        VBox topElements = new VBox();
        Label descriptionLabel = new Label("NoteBooks:");
        descriptionLabel.setFont(Font.font("Times", 15));

        //buttons sub-layout
        HBox topButtonsLayout = new HBox();
        //add notebook button
        Button addButton = new Button("+");
        addButton.setTooltip(new Tooltip("Add a new notebook"));
        addButton.setStyle(
                "-fx-focus-color: transparent; -fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 15;");
        addButton.setOnAction(event -> {
            Main.getTopMenuBarObj().newNoteBookAction(event);
        });
        // remove notebook button
        Button removeButton = new Button("-");
        removeButton.setTooltip(new Tooltip("Remove selected notebook"));
        removeButton.setStyle(
                "-fx-focus-color: transparent; -fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 15;");
        removeButton.setOnAction(event -> {
            Main.getTopMenuBarObj().deleteNoteBookAction(event);
        });
        topButtonsLayout.getChildren().addAll(addButton, removeButton);
        //separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        topElements.getChildren().addAll(topButtonsLayout, descriptionLabel, separator);

        this.tagBrowserLayout = new TagBrowserLayout();
        this.tagBox = this.tagBrowserLayout.getLayout();

        this.sp = new SplitPane();
        this.sp.setOrientation(Orientation.VERTICAL);
        this.sp.getItems().addAll(tree, tagBox);
        this.sp.setDividerPositions(0.8f);

        //keeps SpiltPane height at max possible
        VBox.setVgrow(sp, Priority.ALWAYS);
        layout.getChildren().addAll(topElements, sp);

    }

    //==========Selection management =========================================================

    /**
     * Select next TreeItem, next to currently selected item.
     */
    public void selectNextTreeItem(){
        TreeItem nextItem;
        // try to select it
        try{
            nextItem = this.tree.getSelectionModel().getSelectedItem().nextSibling();
            if (nextItem != null){
                int nextIndex = this.tree.getRow(nextItem);
                this.tree.getSelectionModel().select(nextIndex);
            }
        }catch (NullPointerException e){
            //there are no elements. clear listview
            Main.getNoteBoxAreaObj().replaceListView(null);
        }
    }

    /**
     * Select specified TreeItem
     * @param item TreeItem
     */
    public void selectTreeItem(TreeItem<NoteBook> item){
        int itemIndex = this.tree.getRow(item);
        this.tree.getSelectionModel().select(itemIndex);
    }

    /**
     * select tree item specified by the index
     * @param index
     */
    public void selectTreeItem(int index){
        if (index < 1){
            this.tree.getSelectionModel().selectFirst();
        }else{
            this.tree.getSelectionModel().select(index);
        }
    }

    public void clearNoteBookSelection(){
        this.tree.getSelectionModel().clearSelection();
    }

    //=========Element manipulation ===========================================================

    /**
     * add a new treeItem to the treeview
     * @param item TreeItem<NoteBook>
     */
    public void addTreeItem(TreeItem<NoteBook> item){
        try{
            this.root.getChildren().add(item);
            sortNoteBooksByName();
            selectTreeItem(item);
        }catch (Exception e){}
    }

    /**
     * replace currently selected treeItem with a new one. used for renaming notebooks
     * @param item TreeItem
     */
    public void renameTreeItem(TreeItem<NoteBook> item){
        try{
            this.root.getChildren().remove(getSelectedTreeItem());
            this.root.getChildren().add(item);
            this.root.getChildren().sort(Comparator.comparing(t->t.getValue().toString()));
            this.tree.getSelectionModel().select(item);
        }catch (Exception e){}
    }

    /**
     * remove specified treeItem from TreeView
     * @param item
     */
    public void removeTreeItem(TreeItem<NoteBook> item){
        try{
            this.root.getChildren().remove(item);
        }catch (Exception e){}
    }

    public void sortNoteBooksByName(){
        this.root.getChildren().sort(Comparator.comparing(t->t.getValue().toString()));
    }

    //==============Getters&Setters============================================================

    /**
     * get leftsidebar StackPane
     * @return StackPane
     */
    public VBox getLayout(){
        return this.layout;
    }

    public TagBrowserLayout getTagBrowserLayout() {
        return tagBrowserLayout;
    }

    public boolean getTagBrowserVisibility(){
        if (this.sp.getItems().contains(this.tagBox)){
            return true;
        }else{
            return false;
        }
    }

    public void setTagBrowserVisibility(boolean value){
        if (!this.sp.getItems().contains(this.tagBox) && value){
            this.sp.getItems().add(this.tagBox);
        }else if(this.sp.getItems().contains(this.tagBox) && !value){
            this.sp.getItems().remove(this.tagBox);
        }
    }

    /**
     * get currently selected item in treeView
     * @return NoteBook
     */
    public NoteBook getSelectedNoteBook(){
        try{
            return this.tree.getSelectionModel().getSelectedItem().getValue();
        }catch (NullPointerException e){
            return null;
        }

    }

    /**
     * return currently selected treeview item
     * @return TreeItem
     */
    public TreeItem<NoteBook> getSelectedTreeItem(){
        return this.tree.getSelectionModel().getSelectedItem();
    }

    public int getSelectedTreeItemID(){
        return this.tree.getSelectionModel().getSelectedIndex();
    }

    public boolean isTreeViewFocused(){
        return this.tree.isFocused();
    }

}
