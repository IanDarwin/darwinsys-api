package com.darwinsys.tel;

import static org.junit.Assert.*;
import org.junit.Test;

public class DialWordsTest {
	
	// The Easy Direction
	@Test
	public void testNumberToName() {
		assertEquals("5282", DialWords.nameToNumber("java"));
	}
	
	@Test // to make sure this doesn't throw an exception
	public void testNumberToNameBadInput() {
		assertEquals("282", DialWords.nameToNumber("\u4444ava"));
	}
	
	// The Harder Direction
	@Test
	public void testNameToNumber() {
		String[] x = DialWords.numberToName("5678");
		assertEquals(81, x.length);
		assertEquals("llll", x[0]); // XXX wrong, actually
	}
	public void testNameToNumber2() {
		String[] y = DialWords.numberToName("6432");
		assertEquals(81, y.length);
		assertEquals("oooo", y[0]); // XXX ditto
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNameToNumberBadLength() {
		DialWords.numberToName("123");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNameToNumberBadChars() {
		DialWords.numberToName("abcd");
	}
}
