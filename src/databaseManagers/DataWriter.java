package databaseManagers;


import mainPackage.Main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by margus@workstation on 19.11.2015.
 */
public class DataWriter {

    private Object data;
    private String noteData;
    private File file;
    private String filename;
    private String savePath;

    /**
     * Constructor for writing an object
     * @param notes Arraylist object containing notes
     * @param filename filename
     * @param savePath path where to save
     */
    public DataWriter(Object notes, String filename, String savePath){
        this.data = notes;
        this.filename = filename;
        this.savePath = savePath;
    }

    /**
     * Constructor for writing a text file
     * @param noteData String of text file content
     * @param file File to write
     */
    public DataWriter(String noteData, File file){
        this.noteData = noteData;
        this.file = file;
    }

    /**
     * Writes given object do disk
     */
    public void writeDisk(){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream;

        // Write to disk with FileOutputStream
        try{
            fileOutputStream = new FileOutputStream(this.savePath + this.filename);
        }catch (FileNotFoundException e){
            Main.setMessage("File not found " + e.getMessage(), true);
        }
        // Write object with ObjectOutputStream
        try{
            objectOutputStream = new ObjectOutputStream (fileOutputStream);
            objectOutputStream.writeObject(this.data);

            Main.setMessage("Data write successful!", false);
        }catch (IOException e){
            Main.setMessage("IO error in writing data: " + e.getMessage(), true);
        }

    }

    /**
     * Writes note to a text file
     */
    public void writeNoteToFile(){
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(noteData);
            fileWriter.close();
            Main.setMessage("Note saved to file successfully", false);
        } catch (IOException e) {
            Main.setMessage("Error writing file: " + e.getMessage(), true);
        }
    }
}
