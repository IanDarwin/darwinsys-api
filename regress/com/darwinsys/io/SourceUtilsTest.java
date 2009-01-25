package com.darwinsys.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class SourceUtilsTest {
	
	@Test public void testClassifyClass() {
		assertEquals(SourceType.CLASS, 
			SourceUtils.classify("java.lang.Object"));
	}
	
	@Test public void testClassifyDir() {
		assertEquals(SourceType.DIRECTORY, 
			SourceUtils.classify("/"));
	}
	
	@Test public void testClasifyJar() throws Exception {
		
		File file = File.createTempFile("foo", ".jar");
		try {
			assertEquals(SourceType.JAR,
					SourceUtils.classify(
							file.getAbsolutePath()));
		} finally {
			file.delete();
		}
	}
	
	@Test public void testListClass() {
		assertEquals(1, SourceUtils.classListFromSource("java.lang.Object").size());
	}
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void testFailureListClass() {
		SourceUtils.classListFromSource("java.lang.NoSuchClass");
	}
}
