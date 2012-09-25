package net.sf.cb2java.copybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.Record;

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
    
    /**
     * Parse copybook data.
     *
     * @throws FileNotFoundException
     */
    public void testWeCanParseCopybookData() throws FileNotFoundException, IOException {
        Copybook copybook = CopybookParser.parse("B", new FileInputStream(new File("./target/test-classes/b.copybook")));
        assertEquals(31, copybook.getLength());
        List<Record> results = copybook.parseData(new FileInputStream(new File("./target/test-classes/b.input.txt")));
        assertEquals(1, results.size());
        Record record = results.get(0);
        Data root = record.getChild("ROOT");
        assertEquals("ABCDEF", root.getChildren().get(0).toString());
        assertEquals("B", ((Data)root.getChildren().get(1)).getName().toString());
        assertEquals("BCDE", root.getChildren().get(1).toString());
        // TODO assertEquals(12345, root.getChildren().get(2).toString());
        // TODO assertEquals(1234, ((Data)root.getChildren().get(3)).getValue());
    }
}
