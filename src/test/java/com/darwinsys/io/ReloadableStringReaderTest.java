package com.darwinsys.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReloadableStringReaderTest {
	final static String STRING = "Hello World of Blank Canvas";

	ReloadableStringReader target = new ReloadableStringReader(STRING);

	@Test
	public void testAvailable() {
		assertEquals("avail?", STRING.length(), target.available());
	}

	@Test
	public void testReadaFew() {
		assertEquals("readaFew@0", 'H', target.read());
		assertEquals("readaFew@1", 'e', target.read());
		assertEquals("readaFew@2", 'l', target.read());
	}

	@Test
	public void testReadEOF() {
		for (int i = 0; i < STRING.length(); i++) {
			target.read();
		}
		assertEquals("readEOF", -1, target.read());
	}

	@Test
	public void testReadPartial() {
		char chars[] = new char[10];
		int n = target.read(chars, 0, 10);
		assertEquals("n=read", 10, n);
		for (int i = 0; i < n; i++)
			assertEquals("readPartial@"+i, chars[i], (char)STRING.charAt(i));
	}

	@Test
	public void testReadFully() {
		char chars[] = new char[100];
		int n = target.read(chars, 0, 100);
		assertEquals("n=readFully", STRING.length(), n);
		for (int i = 0; i < n; i++)
			assertEquals("readFully@"+i, chars[i], (char)STRING.charAt(i));
	}

	@Test
	public void testMarkReset() {
		target.mark(0);
		int b1 = target.read();
		target.reset();
		int b2 = target.read();
		assertEquals("markreset", b1, b2);
		assertEquals("markresetavail", STRING.length()-1, target.available());
	}

	@Test
	/** Read a few chars, change the string, ensure no missing or extra chars in stream */
	public void testBaitAndSwitch() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= 5; i++)
			sb.append((char)target.read());
		target.setString("Word");
		for (int i = 0; i <= 3; i++)
			sb.append((char)target.read());
		assertEquals("baitAndSwitch", "Hello Word", sb.toString());
	}
}
