package com.darwinsys.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test the PassPhrase class.
 */
class PassPhraseTest {

	/*
	 * Test for String getNext()
	 */
	@Test
	void getNext() {
		String s = PassPhrase.getNext();
		System.out.println(s);
		assertNotNull(s);
		assertEquals(PassPhrase.MIN_LENGTH, s.length());
	}

	/*
	 * Test for String getNext(int)
	 */
	@Test
	void getNextint() {
		final int[] lengths = { 1, 5, 20, 200 };
		for (int i = 0; i < lengths.length; i++) {
			int j = lengths[i];
			String s = PassPhrase.getNext(j);
			System.out.println(s);
			assertNotNull(s);
			assertEquals(j, s.length());
		}
	}

	/* Test for failure */
	@Test
	void getNextZero() {
		assertThrows(IllegalArgumentException.class, () -> PassPhrase.getNext(0));
	}
}
