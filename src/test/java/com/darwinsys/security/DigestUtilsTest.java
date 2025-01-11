package com.darwinsys.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DigestUtilsTest {

	@Test
	void testmd5() {

		final String clear = "Once, upon a midnight dreary...";
		
		System.out.println("Original is " + clear);

		String digest = DigestUtils.md5(clear);
		System.out.println("Digest is " + digest);

		assertEquals("d367bcfa2fdb30f5e8cb762585338e37", digest, "Digestion");
	}
}
