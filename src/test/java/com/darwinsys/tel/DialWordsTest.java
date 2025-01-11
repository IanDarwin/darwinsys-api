package com.darwinsys.tel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DialWordsTest {

	// The Easy Direction
	@Test
	void numberToName() {
		assertEquals("5282", DialWords.nameToNumber("java"));
	}

	// to make sure this non-ascii character doesn't throw an exception
	@Test
	void numberToNameBadInput() {
		assertEquals("282", DialWords.nameToNumber("\u4444ava"));
	}

	@Test
	void numberToNameNPE() {
		assertThrows(NullPointerException.class, () ->
			DialWords.numberToName(null));
	}

	@Test
	void testnumberToNameIAE() {
		assertThrows(IllegalArgumentException.class, () ->
			DialWords.numberToName(""));
	}

	@Test
	void numberToName01() {
		String[] res;
		res = DialWords.numberToName("01");
		assertEquals(1, res.length, "right#results-simple");
	}

	@Test
	void numberToName42() {
		String[] res;
		res = DialWords.numberToName("42");
		// 4 = g h i
		// 2 = a b c
		// expect: ga ha ia gb hb ib gc hc ic
		assertEquals(9, res.length, "right#results-42->9");
		assertEquals("ga", res[0]);
		for (String r : res)
			System.out.println(42 + " -> " + r);
	}

	@Test
	void numberToName234() {
		String[] res;
		res = DialWords.numberToName("234");
		assertEquals(27, res.length, "right#results-234->27");
		assertEquals("adg", res[0]);
	}

	@Test
	void longString() {
		var res = DialWords.numberToName("416-555-1212");
		assertEquals(2187, res.length);
		assertEquals("g1mjjj1a1a", res[0]);
	}
}
