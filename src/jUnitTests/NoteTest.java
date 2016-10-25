package jUnitTests;

import noteStuff.Note;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by margus@workstation on 03.12.2015.
 */
public class NoteTest {

    @Test
    public void testGetTagsAsString() throws Exception {
        Note note = new Note("test", "test", "test");

        note.setTags(new String[]{"tag1", "tag2"});
        assertEquals("tag1 tag2", note.getTagsAsString());

        note.setTags(new String[]{"tag1"});
        assertEquals("tag1", note.getTagsAsString());

        note.setTags(new String[]{"tag1", "tag2", "tag3"});
        assertEquals("tag1 tag2 tag3", note.getTagsAsString());
    }
}