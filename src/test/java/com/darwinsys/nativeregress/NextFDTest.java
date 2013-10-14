package com.darwinsys.nativeregress;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import com.darwinsys.io.NextFD;

public class NextFDTest extends TestCase {

	/**
	 * A file that exists on most *NIX systems; it is the file we open for
	 * reading iff no argument is passed on the command line.
	 */
	public static final String COMMON_FILE = "/etc/passwd";

	/**
	 * Test method for 'com.darwinsys.io.NextFD.getNextFD()'
	 */
	public void testGetNextFD() throws IOException {

		int start = NextFD.getNextFD();
		System.out.println("nextfd returned " + start);

		String fileName = COMMON_FILE;

		InputStream is = new FileInputStream(fileName);
		System.out.printf("File %s is open.%n", fileName);

		int high = NextFD.getNextFD();
		System.out.println("nextfd returned " + high);
		if (start != -1) {
			assertEquals("opening file adds 1", high, start + 1);
		}

		is.close();
		
		int end = NextFD.getNextFD();
		System.out.println("after close, nextfd returned " + end);
		assertEquals("back to where we started?", end, start);
	}
}
