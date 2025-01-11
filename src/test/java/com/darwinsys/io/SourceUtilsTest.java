package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.jar.*;
import java.util.zip.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SourceUtilsTest {

	@Disabled("failsome")
	@Test
	void classifyClass() {
		assertEquals(SourceType.CLASS, 
			SourceUtils.classify("java.lang.Object"));
	}

	@Disabled("failsome")
	@Test
	void classifyDir() {
		assertEquals(SourceType.DIRECTORY, 
			SourceUtils.classify("."));
	}

	@Disabled("failsome")
	@Test
	void clasifyJar() throws Exception {
		
		File file = File.createTempFile("foo", ".jar");
		try {
			Manifest mf = new Manifest();
			Attributes attrs = new Attributes();
			attrs.putValue("Creator", getClass().getName());
			mf.getEntries().put("Creater", attrs);

			JarOutputStream zf = new JarOutputStream(new FileOutputStream(file), mf);
			zf.putNextEntry(new ZipEntry("foo.bar"));
			zf.write("Hello".getBytes());
			zf.closeEntry();
			zf.close();
			assertEquals(SourceType.JAR,
					SourceUtils.classify(
							file.getAbsolutePath()));
		} finally {
			// file.delete();
		}
	}
}
