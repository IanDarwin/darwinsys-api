package com.darwinsys.lang;

import junit.framework.TestCase;

/**
 * Some test cases for GetOpt.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class GetOptConstructorTest extends TestCase {

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
	
	public void testForNull() {
		try {
			String bad = null;
			new GetOpt(bad);
			fail("GetOpt(null) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
	}

	public void testForNoLetter() {
		try {
			new GetOpt("::");
			fail("GetOpt(::) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
		new GetOpt("f:c:"); // this failed at one point - multiple : args
		new GetOpt("foo"); // multiple occurrences of same letter - ok?
	}

	public void testForExtraCruft() {
		String bad = "abc@";
		try {
			new GetOpt(bad);
			fail("GetOpt(" + bad + ") did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
	}
}
