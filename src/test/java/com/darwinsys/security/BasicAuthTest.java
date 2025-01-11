package com.darwinsys.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicAuthTest {

	@BeforeEach
	void foo() {
		// empty
	}

	@Test
	void basicAuth() {

		String user = "rintintin", pass = "superdog";
		assertEquals("Basic cmludGludGluOnN1cGVyZG9n", 
			BasicAuth.makeHeaderValue(user,pass));
	}
}
