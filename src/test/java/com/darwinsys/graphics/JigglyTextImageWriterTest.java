package com.darwinsys.graphics;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

public class JigglyTextImageWriterTest extends TestCase {

	JigglyTextImageWriter writer;

	protected void setUp() throws Exception {
		Font font = new Font("SansSerif", Font.BOLD, 24);
		writer = new JigglyTextImageWriter(font, 300, 100);
	}

	public void testWrite() throws Throwable {
		File tmp = File.createTempFile("jiggly", "pig.jpg");
		OutputStream os = new FileOutputStream(tmp);
		writer.write("Hello Squig", os);
		os.close();
		assertTrue("created", tmp.exists());
		assertTrue("is file", tmp.isFile());
		assertTrue("file size", tmp.length() > 2000); // empirical
		tmp.delete();
	}

}
