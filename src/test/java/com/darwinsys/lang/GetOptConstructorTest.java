package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Some test cases for GetOpt.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
class GetOptConstructorTest {

	@Test
	void ok() {
		GetOpt getopt = new GetOpt("tn:");
		assertEquals(2, getopt.options.length);
		GetOptDesc tDesc = getopt.options[0];
		assertEquals('t', tDesc.getArgLetter());
		assertNull(tDesc.getArgName());
		assertFalse(tDesc.takesArgument());
		GetOptDesc nDesc = getopt.options[1];
		assertEquals('n', nDesc.getArgLetter());
		assertNull(nDesc.getArgName());
		assertTrue(nDesc.takesArgument());
	}

	@Test
	void forNull() {
		String bad = null;
		assertThrows(IllegalArgumentException.class, () -> new GetOpt(bad));
	}

	@Test
	void forNoLetter() {
		assertThrows(IllegalArgumentException.class, () -> new GetOpt("::"));
	}

	@Test
	void extraLetters() {
		new GetOpt("f:c:"); // this failed at one point - multiple : args
		new GetOpt("foo"); // multiple occurrences of same letter - ok?
	}

	@Test
	void forLeadingColon() {
		try {
			new GetOpt(":a:b");
			fail("GetOpt(::) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			//
		}
	}

	@Test
	void forExtraCruft() {
		String bad = "abc@";
		try {
			new GetOpt(bad);
			fail("GetOpt(" + bad + ") did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			//
		}
	}
}
