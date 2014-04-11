package net.sf.cb2java.types;

import junit.framework.TestCase;

public class AlphaNumericTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testValidateX2000() {
		AlphaNumeric cut = new AlphaNumeric("DATA-ITEM", 5, 0, "X(100)");
		cut.validate("Lorem ipsum dolor sit amet");
	}

	public void testValidateX2000WithCarriageReturn() {
		try {
			AlphaNumeric cut = new AlphaNumeric("DATA-ITEM", 5, 0, "X(100)");
			cut.validate("Lorem ipsum \n dolor sit amet");
			fail("AlphaNumeric does not currently allow carriage return characters.");
		} catch (IllegalArgumentException e) {
			// Test passes, however I'm not sure it's correct. According to the
			// COBOL reference: "The contents of the item in standard data format can be any allowable characters 
			// from the character set of the computer."
			// http://publib.boulder.ibm.com/infocenter/ratdevz/v7r1m1/topic/com.ibm.ent.cbl.zos.doc/topics/igy3lr31.pdf
		}
	}

}
