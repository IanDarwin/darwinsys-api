package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GetOptDescTest {

	@Test
	void getOptDesc() {
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

	@Test
	void getOptDescFailures() {
		assertThrows(IllegalArgumentException.class, () -> {
			new GetOptDesc('\0', null, false);
		});
	}
}
