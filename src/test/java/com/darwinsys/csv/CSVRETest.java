package com.darwinsys.csv;

import java.util.List;

/**
 * JUnit tests for CSV RE
 */
public class CSVRETest extends CSVParserTestBase {

	public CSVRETest() {
		csv = new CSVRE();
	}

	public void testCanonical() {
		List list = csv.parse("\"a\",\"b\",\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testNullField() {
		List list = csv.parse("\"a\",,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("", list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testNotAllQuoted() {
		List list = csv.parse("\"a\",b,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testAllUnQuoted() {
		List list = csv.parse("a,b,c");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testMixedField() {
		List list = csv.parse("\"LU\",86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		assertEquals(2, list.size());
	}

	/**
	 * Test suggested by an email from "Benoit" &lt;benoitx
	 * at yahoo.com%gt.
	 * The current RE version does not work with this test.
	 */
	public void XXXtestEscapeQuoted() {
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
