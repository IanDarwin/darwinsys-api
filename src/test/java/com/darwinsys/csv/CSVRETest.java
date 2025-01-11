package com.darwinsys.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for CSV RE
 */
public class CSVRETest extends CSVParserTestBase {

	public CSVRETest() {
		csv = new CSVRE();
	}

	@Test
	void canonical() {
		List<String> list = csv.parse("\"a\",\"b\",\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	void nullField() {
		List<String> list = csv.parse("\"a\",,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	void notAllQuoted() {
		List<String> list = csv.parse("\"a\",b,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	void allUnQuoted() {
		List<String> list = csv.parse("a,b,c");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	void mixedField() {
		List<String> list = csv.parse("\"LU\",86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		assertEquals(2, list.size());
	}

	@Test
	void brokenQuotes() {
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
	// Not working yet
	@Test
	@Disabled
	void escapeQuoted() {
		String string = "\"a,b,c\",d,\"and \\\"e\",f";
		System.out.println(string);
		List<String> list = csv.parse(string);
		assertEquals(4, list.size());
		assertEquals("a,b,c", list.get(0));
		assertEquals("d", list.get(1));
		// assertEquals("and \\\"e", list.get(2));
		assertEquals("f", list.get(3));
	}
}
