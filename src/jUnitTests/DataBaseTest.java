package jUnitTests;

import databaseManagers.DataBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataBaseTest {

    @Test
    public void testCheckForNumbersAtTheEnd() throws Exception {

        DataBase dataBase = new DataBase();
        assertEquals("Nimi", dataBase.checkForNumbersAtTheEnd("Nimi"));
        assertEquals("Nimi2", dataBase.checkForNumbersAtTheEnd("Nimi1"));
        assertEquals("Nimi 3", dataBase.checkForNumbersAtTheEnd("Nimi 2"));
        //fixed:
        assertEquals("Nimi02", dataBase.checkForNumbersAtTheEnd("Nimi01"));
        assertEquals("Nimi005", dataBase.checkForNumbersAtTheEnd("Nimi004"));
        //fixed:
        assertEquals("Nimi010", dataBase.checkForNumbersAtTheEnd("Nimi009"));

    }
}