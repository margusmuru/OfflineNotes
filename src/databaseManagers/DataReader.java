package databaseManagers;

import mainPackage.Main;
import mainPackage.Settings;
import noteStuff.Note;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by margus@workstation on 19.11.2015.
 */
public class DataReader {

    private BufferedReader dataReader;
    private DataBase dataBase;
    private String fileName;
    private String savePath;

    /**
     * Constructor
     */
    public DataReader(DataBase dataBase, String fileName, String savePath){
        this.dataBase = dataBase;
        this.fileName = fileName;
        this.savePath = savePath;
    }

    /**
     * reads object from disk
     * @return Object
     */
    private Object readObjectFromDisk(){
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object obj = null;
        // Read from disk using FileInputStream
        try{
            fileInputStream = new FileInputStream(this.savePath + this.fileName);
        }catch (FileNotFoundException e){
            Main.setMessage(e.getMessage(), true);
        }
        // Read object from fileINputStream using ObjectInputStream
        if (fileInputStream != null){
            try{
                objectInputStream = new ObjectInputStream(fileInputStream);
            }catch (IOException e){
                Main.setMessage(e.getMessage(), true);
            }
        }
        // Read an object
        try{
            obj = objectInputStream.readObject();

        }catch (ClassNotFoundException e){
            Main.setMessage(e.getMessage(), true);
        }catch (IOException e){
            Main.setMessage(e.getMessage(), true);
        }catch (NullPointerException e){
            Main.setMessage(e.getMessage(), true);
        }
        return obj;
    }

    /**
     * reads note data from disk
     * @return Arraylist containing Note objects
     */
    public  ArrayList<Note> readData(){
        ArrayList<Note> noteBooks = new ArrayList<>();
        Object obj = readObjectFromDisk();
        if (obj instanceof ArrayList) {
            noteBooks = (ArrayList) obj;
        }
        return noteBooks;
    }

    /**
     * reads settings data from disk
     * @return Settings object
     */
    public  Settings readSettings(){
        Settings settings = null;
        Object obj = readObjectFromDisk();
        //TODO: what is the correct type?
        if (obj instanceof Object) {
            settings = (Settings) obj;
        }
        return settings;
    }
}
