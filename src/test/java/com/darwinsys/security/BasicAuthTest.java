package com.darwinsys.security;

import org.junit.*;
import static org.junit.Assert.*;

public class BasicAuthTest {

	@Before
	public void foo() {
		// empty
	}

	@Test
	public void testBasicAuth() {

		String user = "rintintin", pass = "superdog";
		assertEquals("Basic cmludGludGluOnN1cGVyZG9n", 
			BasicAuth.makeHeaderValue(user,pass));
	}
}
