package com.darwinsys.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DigestUtilsTest {

	@Test
	public void testmd5() {

		final String clear = "Once, upon a midnight dreary...";
		
		System.out.println("Original is " + clear);

		String digest = DigestUtils.md5(clear);
		System.out.println("Digest is " + digest);

		assertEquals("Digestion", "d367bcfa2fdb30f5e8cb762585338e37", digest);
	}
}
