package com.darwinsys.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetOptDescTest {

	@Test
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

	@Test(expected=IllegalArgumentException.class)
	public void testGetOptDescFailures() {
		new GetOptDesc('\0', null, false);
	}
}
