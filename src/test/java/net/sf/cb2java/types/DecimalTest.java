package net.sf.cb2java.types;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;
import net.sf.cb2java.data.Data;

public class DecimalTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testParse() throws UnsupportedEncodingException {
		assertEquals(new BigInteger("9050"), parse("9(4)", "9050"));
		assertEquals(new BigInteger("-9050"), parse("S9(4)", "905}"));
		assertEquals(new BigInteger("-2345"), parse("S9(4)", "234N")); // Real example
		assertEquals(new BigDecimal("2345.67"), parse("9(4)V99", "234567"));
		assertEquals(new BigDecimal("-2345.67"), parse("S9(4)V99", "23456P"));
		assertEquals(new BigDecimal("1.23"), parse("S9(6)V99", "0000012C")); // Real example
	}

	protected Object parse(String pic, String string) throws UnsupportedEncodingException {
		Decimal decimal = new Decimal("DUMMY", pic);
		Data data = decimal.parse(string.getBytes("cp1252"));
		Object value = data.getValue();
		return value;
	}
}
