package com.darwinsys.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.darwinsys.util.FileProperties;

public class FilePropertiesTest extends TestCase {
	private static final String TEST_FILE_NAME = "erewhon";
	Properties p;
	File erewhon = new File(TEST_FILE_NAME);

	/** Set up for junit test.
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		erewhon.delete();
		// no such file should exist; should not throw IOException
		p = new FileProperties(TEST_FILE_NAME);

	}

    public void testSet() throws Exception {
    	assertEquals("Properties p should be empty:", p.size(), 0);
		p.setProperty("foo", "bar");
		p.store(new FileOutputStream(TEST_FILE_NAME), "# test");

		p = new FileProperties(TEST_FILE_NAME);
		System.out.println("This properties should be have foo=bar:");
		p.list(System.out);
	}

    public void testInputStream() throws IOException {
    	String propsFile =
    		"foo=bar\n" +
    		"x y\n";
    	InputStream is = new ByteArrayInputStream(propsFile.getBytes());
    	p = new FileProperties(is);
    	assertEquals("bar", p.getProperty("foo"));
    	assertEquals("y", p.getProperty("x"));
    }
}
