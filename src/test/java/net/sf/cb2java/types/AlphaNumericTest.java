package net.sf.cb2java.types;

import net.sf.cb2java.Settings;
import net.sf.cb2java.Values;
import junit.framework.TestCase;

public class AlphaNumericTest extends TestCase {

	public void testValidateNull() {
		AlphaNumeric cut = createTestObject("X(100)");
		cut.validate(null);
	}
	
	public void testValidateA100() {
		AlphaNumeric cut = createTestObject("A(100)");
		cut.validate("Lorem ipsum dolor sit amet");
		
		Exception ex = null;
		try {
			cut.validate("Lorem ipsum 1 dolor sit amet");
			fail("Alphabetic picture should not accept numeric characters.");
		} catch (IllegalArgumentException e) {
			ex = e;
		}
		assertNotNull(ex);
	}

	public void testValidateX100WithCarriageReturn() {
		Exception ex = null;
		try {
			AlphaNumeric cut = createTestObject("X(100)");
			cut.validate("Lorem ipsum \n dolor sit amet");
			fail("AlphaNumeric does not currently allow carriage return characters.");
		} catch (IllegalArgumentException e) {
			ex = e;
			// Test passes, however I'm not sure it's correct. According to the
			// COBOL reference: "The contents of the item in standard data format can be any allowable characters 
			// from the character set of the computer."
			// http://publib.boulder.ibm.com/infocenter/ratdevz/v7r1m1/topic/com.ibm.ent.cbl.zos.doc/topics/igy3lr31.pdf
		}
		assertNotNull(ex);
	}
	
	public void testIllegalPicture() {
		Exception ex = null;
		try {
			createTestObject("XA9Z");
			fail("AlphaNumeric should not accept pic Z.");
		} catch (IllegalArgumentException e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals("character [Z] not allowed.", ex.getMessage());
	}

	protected AlphaNumeric createTestObject(String picture) {
		AlphaNumeric cut = new AlphaNumeric("DATA-ITEM", 5, 0, picture);
		
		Values values = Settings.DEFAULT.getValues();
		values.setEncoding(Settings.DEFAULT.getEncoding());
		cut.setValue(values.SPACES);
		return cut;
	}

}
