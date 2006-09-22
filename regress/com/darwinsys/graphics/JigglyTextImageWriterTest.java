package com.darwinsys.graphics;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

public class JigglyTextImageWriterTest extends TestCase {

	JigglyTextImageWriter writer;

	protected void setUp() throws Exception {
		super.setUp();
		Font font = new Font("SansSerif", Font.BOLD, 24);
		writer = new JigglyTextImageWriter(font, 300, 200);
	}

	public void testWrite() throws Throwable {
		File tmp = new File("/tmp/jig.jpg");
		OutputStream os = new FileOutputStream(tmp);
		writer.write("Hello Squig", os);
		assertTrue(tmp.exists());
		assertTrue(tmp.isFile());
		assertTrue(tmp.length() > 0);
	}

}
