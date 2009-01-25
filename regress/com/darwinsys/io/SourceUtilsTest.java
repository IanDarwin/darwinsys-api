package com.darwinsys.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class SourceUtilsTest {
	
	@Test public void testClass() {
		assertEquals(SourceType.CLASS, 
			SourceUtils.classify("java.lang.Object"));
	}
	@Test public void testDir() {
		assertEquals(SourceType.DIRECTORY, 
			SourceUtils.classify("/"));
	}
	
	@Test public void testJar() throws Exception {
		
		File file = File.createTempFile("foo", ".jar");
		try {
			assertEquals(SourceType.JAR,
					SourceUtils.classify(
							file.getAbsolutePath()));
		} finally {
			file.delete();
		}
	}
}
