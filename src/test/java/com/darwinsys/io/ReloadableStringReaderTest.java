package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReloadableStringReaderTest {
	final static String STRING = "Hello World of Blank Canvas";

	ReloadableStringReader target = new ReloadableStringReader(STRING);

	@Test
	void available() {
		assertEquals(STRING.length(), target.available(), "avail?");
	}

	@Test
	void readaFew() {
		assertEquals('H', target.read(), "readaFew@0");
		assertEquals('e', target.read(), "readaFew@1");
		assertEquals('l', target.read(), "readaFew@2");
	}

	@Test
	void readEOF() {
		for (int i = 0; i < STRING.length(); i++) {
			target.read();
		}
		assertEquals(-1, target.read(), "readEOF");
	}

	@Test
	void readPartial() {
		char chars[] = new char[10];
		int n = target.read(chars, 0, 10);
		assertEquals(10, n, "n=read");
		for (int i = 0; i < n; i++)
			assertEquals(chars[i], (char)STRING.charAt(i), "readPartial@"+i);
	}

	@Test
	void readFully() {
		char chars[] = new char[100];
		int n = target.read(chars, 0, 100);
		assertEquals(STRING.length(), n, "n=readFully");
		for (int i = 0; i < n; i++)
			assertEquals(chars[i], (char)STRING.charAt(i), "readFully@"+i);
	}

	@Test
	void markReset() {
		target.mark(0);
		int b1 = target.read();
		target.reset();
		int b2 = target.read();
		assertEquals(b1, b2, "markreset");
		assertEquals(STRING.length()-1, target.available(), "markresetavail");
	}

	/** Read a few chars, change the string, ensure no missing or extra chars in stream */
	@Test
	void baitAndSwitch() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= 5; i++)
			sb.append((char)target.read());
		target.setString("Word");
		for (int i = 0; i <= 3; i++)
			sb.append((char)target.read());
		assertEquals("Hello Word", sb.toString(), "baitAndSwitch");
	}
}
