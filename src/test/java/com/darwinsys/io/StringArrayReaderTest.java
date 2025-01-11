package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

class StringArrayReaderTest {

	String[] data = {
			"Line 1",
			"Line 2",
			"Line 3"
	};

	@Test
	void simple() throws IOException {
		Reader r = new StringArrayReader(data);
		try (BufferedReader is = new BufferedReader(r);) {
			String line;
			assertNotNull(line = is.readLine());
			assertEquals(data[0], line);
			assertNotNull(line = is.readLine());
			assertEquals(data[1], line);
			assertNotNull(line = is.readLine());
			assertEquals(data[2], line);
			assertNull(line = is.readLine());
		}
	}

}
