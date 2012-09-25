package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import junit.framework.TestCase;

/**
 *
 * @author devstopfix
 */
public class CopybookParserTest extends TestCase {
    
    public CopybookParserTest(String testName) {
        super(testName);
    }

    /**
     * Take a copybook and parse it.
     *
     * @throws FileNotFoundException
     */
    public void testWeCanParseCopybook() throws FileNotFoundException {
        Copybook copybook = CopybookParser.parse("A", new FileInputStream(new File("./target/test-classes/a.copybook")));
        assertEquals(55, copybook.getLength());
    }
}
