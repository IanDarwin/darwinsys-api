package com.darwinsys.io;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.jar.*;
import java.util.zip.*;

import org.junit.Test;
import org.junit.Ignore;

public class SourceUtilsTest {
	
	@Ignore("failsome")
	@Test public void testClassifyClass() {
		assertEquals(SourceType.CLASS, 
			SourceUtils.classify("java.lang.Object"));
	}
	
	@Ignore("failsome")
	@Test public void testClassifyDir() {
		assertEquals(SourceType.DIRECTORY, 
			SourceUtils.classify("."));
	}

	@Ignore("failsome")
	@Test public void testClasifyJar() throws Exception {
		
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
