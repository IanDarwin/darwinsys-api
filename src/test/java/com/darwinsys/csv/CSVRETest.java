package com.darwinsys.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit tests for CSV RE
 */
public class CSVRETest extends CSVParserTestBase {

	public CSVRETest() {
		csv = new CSVRE();
	}

	@Test
	public void testCanonical() {
		List list = csv.parse("\"a\",\"b\",\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	@Test
	public void testNullField() {
		List list = csv.parse("\"a\",,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	public void testNotAllQuoted() {
		List list = csv.parse("\"a\",b,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	@Test
	public void testAllUnQuoted() {
		List list = csv.parse("a,b,c");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	@Test
	public void testMixedField() {
		List list = csv.parse("\"LU\",86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		assertEquals(2, list.size());
	}

	@Test
	public void testBrokenQuotes() {
		try {
			csv.parse("one,two,\"three,four");
			fail("Did not catch expected exception on bad input");
		} catch (IllegalArgumentException expected) {
			// do nothing
		}
	}

	/**
	 * Test suggested by an email from "Benoit" &lt;benoitx
	 * at yahoo.com%gt.
	 * The current RE version does not work with this test.
	 */
	@Test @Ignore // Not working yet
	public void testEscapeQuoted() {
		String string = "\"a,b,c\",d,\"and \\\"e\",f";
		System.out.println(string);
		List list = csv.parse(string);
		assertEquals(4, list.size());
		assertEquals("a,b,c", list.get(0));
		assertEquals("d", list.get(1));
		// assertEquals("and \\\"e", list.get(2));
		assertEquals("f", list.get(3));
	}
}
