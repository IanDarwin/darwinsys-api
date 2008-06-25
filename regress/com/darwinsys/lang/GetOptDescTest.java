package com.darwinsys.lang;

import junit.framework.TestCase;

public class GetOptDescTest extends TestCase {

	public void testGetOptDesc() {
		GetOptDesc d;
		d = new GetOptDesc('a', "foo", true);
		assertEquals('a', d.getArgLetter());
		assertEquals("foo", d.getArgName());
		assertTrue(d.takesArgument());
		
		d = new GetOptDesc('j', null, false);
		assertEquals('j', d.getArgLetter());
		assertNull(d.getArgName());
		assertFalse(d.takesArgument());
	}

	public void testGetOptDescFailures() {
		try {
			new GetOptDesc('\0', null, false);
		} catch (Exception e) {
			// System.out.println("Caught expected exception");
		}
	}
}
