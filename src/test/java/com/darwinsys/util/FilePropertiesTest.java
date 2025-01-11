package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilePropertiesTest {
	Properties p;
	File erewhon;

	/** Set up for junit test.
	 * @see 
	 */
	@BeforeEach
	void setUp() throws Exception {
		erewhon = File.createTempFile("filepropertiestest", "junk");
		erewhon.deleteOnExit();
		// no such file should exist; should not throw IOException
		p = new FileProperties("no such file");
	}

	@Test
	void set() throws Exception {
		assertEquals(0, p.size(), "Properties p should be empty:");
		p.setProperty("foo", "bar");
		p.store(new FileOutputStream(erewhon), "# test");

		p = new FileProperties(erewhon.getAbsolutePath());
		assertEquals("bar", p.getProperty("foo"));
	}

	@Test
	void inputStream() throws IOException {
    	String propsFile =
    		"foo=bar\n" +
    		"x y\n";
    	InputStream is = new ByteArrayInputStream(propsFile.getBytes());
    	p = new FileProperties(is);
    	assertEquals("bar", p.getProperty("foo"));
    	assertEquals("y", p.getProperty("x"));
    }
}
