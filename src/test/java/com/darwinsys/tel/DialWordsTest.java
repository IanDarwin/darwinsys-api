package com.darwinsys.tel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DialWordsTest {

	// The Easy Direction
	@Test
	public void testNumberToName() {
		assertEquals("5282", DialWords.nameToNumber("java"));
	}

	@Test // to make sure this non-ascii character doesn't throw an exception
	public void testNumberToNameBadInput() {
		assertEquals("282", DialWords.nameToNumber("\u4444ava"));
	}

	@Test(expected=NullPointerException.class)
	public void testNumberToNameNPE() throws Exception {
		DialWords.numberToName(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testnumberToNameIAE() {
		DialWords.numberToName("");
	}
	
	@Test
	public void testNumberToName01() {
		String[] res;
		res = DialWords.numberToName("01");
		assertEquals("right#results-simple", 1, res.length);
	}
	
	@Test
	public void testNumberToName42() {
		String[] res;
		res = DialWords.numberToName("42");
		// 4 = g h i
		// 2 = a b c
		// expect: ga ha ia gb hb ib gc hc ic
		assertEquals("right#results-42->9", 9, res.length);
		assertEquals("ga", res[0]);
		for (String r : res)
			System.out.println(42 + " -> " + r);
	}
	
	@Test
	public void testNumberToName234() {
		String[] res;
		res = DialWords.numberToName("234");
		assertEquals("right#results-234->27", 27, res.length);
		assertEquals("adg", res[0]);
	}

	@Test
	public void testLongString() {
		var res = DialWords.numberToName("416-555-1212");
		assertEquals(2187, res.length);
		assertEquals("g1mjjj1a1a", res[0]);
	}
}
