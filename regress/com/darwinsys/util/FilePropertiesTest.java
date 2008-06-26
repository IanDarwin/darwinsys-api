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
	Properties p;
	File erewhon;

	/** Set up for junit test.
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		erewhon = File.createTempFile("filepropertiestest", "junk");
		erewhon.deleteOnExit();
		// no such file should exist; should not throw IOException
		p = new FileProperties("no such file");
	}

    public void testSet() throws Exception {
    	assertEquals("Properties p should be empty:", p.size(), 0);
		p.setProperty("foo", "bar");
		p.store(new FileOutputStream(erewhon), "# test");

		p = new FileProperties(erewhon.getAbsolutePath());
		assertEquals("bar", p.getProperty("foo"));
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
