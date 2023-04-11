package com.darwinsys.util;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneNumberUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected=NullPointerException.class)
	public void testMnemonicsNPE() throws Exception {
		PhoneNumberUtil.mnemonics(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMnemonicsIAE() {
		PhoneNumberUtil.mnemonics("");
	}
	
	@Test
	public void testMnemonics01() {
		String[] res;
		res = PhoneNumberUtil.mnemonics("01");
		assertEquals("right#results-simple", 1, res.length);
	}
	
	@Test
	public void testMnemonics42() {
		String[] res;
		res = PhoneNumberUtil.mnemonics("42");
		// 4 = g h i
		// 2 = a b c
		// expect: ga ha ia gb hb ib gc hc ic
		assertEquals("right#results-42->9", 9, res.length);
		assertEquals("ga", res[0]);
		for (String r : res)
			System.out.println(42 + " -> " + r);
	}
	
	@Test
	public void testMnemonics234() {
		String[] res;
		res = PhoneNumberUtil.mnemonics("234");
		assertEquals("right#results-234->27", 27, res.length);
		assertEquals("adg", res[0]);
	}

	@Test
	public void testLongString() {
		var res = PhoneNumberUtil.mnemonics("416-555-1212");
		assertEquals(2187, res.length);
		assertEquals("g1mjjj1a1a", res[0]);
	}
}
