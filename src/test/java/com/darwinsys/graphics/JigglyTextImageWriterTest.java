package com.darwinsys.graphics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JigglyTextImageWriterTest {

	JigglyTextImageWriter writer;

	@BeforeEach
	void setUp() throws Exception {
		Font font = new Font("SansSerif", Font.BOLD, 24);
		writer = new JigglyTextImageWriter(font, 300, 100);
	}

	@Test
	void write() throws Throwable {
		File tmp = File.createTempFile("jiggly", "pig.jpg");
		OutputStream os = new FileOutputStream(tmp);
		writer.write("Hello Squig", os);
		os.close();
		assertTrue(tmp.exists(), "created");
		assertTrue(tmp.isFile(), "is file");
		assertTrue(tmp.length() > 2000, "file size"); // empirical
		tmp.delete();
	}

}
