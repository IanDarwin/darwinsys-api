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

//		for (int i = 0; i < x.length; i++) {
//			System.out.print(x[i] + ' ');
//		}
		assertEquals("llll", x[0]); // XXX wrong, actually
		String[] y = DialWords.numberToName("6432");
		assertEquals(81, y.length);
		assertEquals("pppp", y[0]); // XXX ditto
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
