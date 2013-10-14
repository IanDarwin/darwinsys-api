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
	
	public void testMnemonics() {
		String[] res;
		res = PhoneNumberUtil.mnemonics("01");
		assertEquals("right#results-simple", 1, res.length);
		res = PhoneNumberUtil.mnemonics("42");
		assertEquals("right#results-simple", 9, res.length);
		res = PhoneNumberUtil.mnemonics("234");
		assertEquals("right#results-simple", 27, res.length);
	}
}
