package com.darwinsys.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test the PassPhrase class.
 */
public class PassPhraseTest {

	/*
	 * Test for String getNext()
	 */
	@Test
	public void testGetNext() {
		String s = PassPhrase.getNext();
		System.out.println(s);
		assertNotNull(s);
		assertEquals(PassPhrase.MIN_LENGTH, s.length());
	}

	/*
	 * Test for String getNext(int)
	 */
	@Test
	public void testGetNextint() {
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
	@Test(expected=IllegalArgumentException.class)
	public void testGetNextZero() {
		PassPhrase.getNext(0);
		fail("Did not trap length zero request");
	}
}
