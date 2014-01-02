package com.darwinsys.lang;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Some test cases for GetOpt.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class GetOptConstructorTest  {

	@Test
	public void testOK() {
		GetOpt getopt = new GetOpt("tn:");
		assertEquals(2, getopt.options.length);
		GetOptDesc tDesc = getopt.options[0];
		assertTrue('t' == tDesc.getArgLetter());
		assertEquals(null, tDesc.getArgName());
		assertFalse(tDesc.takesArgument());
		GetOptDesc nDesc = getopt.options[1];
		assertTrue('n' == nDesc.getArgLetter());
		assertEquals(null, nDesc.getArgName());
		assertTrue(nDesc.takesArgument());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testForNull() {
		String bad = null;
		new GetOpt(bad);
		fail("GetOpt(null) did not throw expected exception");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testForNoLetter() {
		new GetOpt("::");
		fail("GetOpt(::) did not throw expected exception");
	}
	
	@Test
	public void testExtraLetters() {
		new GetOpt("f:c:"); // this failed at one point - multiple : args
		new GetOpt("foo"); // multiple occurrences of same letter - ok?
	}

	@Test
	public void testForLeadingColon() {
		try {
			new GetOpt(":a:b");
			fail("GetOpt(::) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			//
		}
	}
	
	@Test
	public void testForExtraCruft() {
		String bad = "abc@";
		try {
			new GetOpt(bad);
			fail("GetOpt(" + bad + ") did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			//
		}
	}
}
