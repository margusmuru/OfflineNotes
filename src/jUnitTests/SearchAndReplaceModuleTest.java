package jUnitTests;

import layouts.SearchAndReplaceModule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by margus@workstation on 03.12.2015.
 */
public class SearchAndReplaceModuleTest {

    @Test
    public void testFormatSourceText() throws Exception {
        SearchAndReplaceModule srm = new SearchAndReplaceModule();
        assertEquals("TeXt", srm.formatSearchText("TeXt", true));
        assertEquals("text", srm.formatSearchText("TeXt", false));
    }

    @Test
    public void testFormatSearchText() throws Exception {
        SearchAndReplaceModule srm = new SearchAndReplaceModule();
        assertEquals("TeXt", srm.formatSourceText("TeXt", true));
        assertEquals("text", srm.formatSourceText("TeXt", false));
    }
}