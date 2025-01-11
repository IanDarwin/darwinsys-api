package com.darwinsys.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DESUtilsTest {

	DESCrypter target;

	@BeforeEach
	void setUp() throws Exception {
		final String myPass = "Ain't no binary blobs here!";
		target = new DESCrypter(myPass);
	}

	@Disabled("string length not % 4 from encrypt")
	@Test
	void des() {

		final String clear = "Once, upon a midnight dreary...";
		
		System.out.println("Original is " + clear);

		String encrypted = target.encrypt(clear);
		System.out.println("Encrypts as " + encrypted);

		String decrypted = target.decrypt(encrypted);

		assertEquals(clear, decrypted, "Decrypted OK!");
	}
}
