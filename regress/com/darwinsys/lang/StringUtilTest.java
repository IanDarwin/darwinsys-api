package com.darwinsys.lang;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {

	public void testToCommaList() {
		String[] list = { "apples", "oranges", "pumpkins", "bananas" };
		assertEquals(
			"apples, oranges, pumpkins and bananas",
			StringUtil.arrayToCommaList(list));
	}

	public void testIndexOf() {
		StringBuffer sb = new StringBuffer("This Fish in the Ish Wish Dish");

		// Test Successes
		assertEquals(2, StringUtil.indexOf(sb, "is", 0));
		assertEquals(2, StringUtil.indexOf(sb, "is", 2));
		assertEquals(6, StringUtil.indexOf(sb, "is", 3));
		assertEquals(6, StringUtil.indexOf(sb, "is", 4));
		assertEquals(6, StringUtil.indexOf(sb, "is", 5));
		assertEquals(6, StringUtil.indexOf(sb, "is", 6));
		assertEquals(22,StringUtil.indexOf(sb, "is", 7));

		// Test Failures
		assertEquals(-1, StringUtil.indexOf(sb, "XX", 0));
		assertEquals(-1, StringUtil.indexOf(sb, "XX", 100));
	}

	public void testSubst() {
		String oldStr = "Old Mc${fred} had a farm, had ${fred}";
		String expect = "Old McFRED had a farm, had FRED";
		String newStr = StringUtil.subst("${fred}", "FRED", oldStr);
		assertEquals(expect, newStr);
	}
}
