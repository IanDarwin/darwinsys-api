package com.darwinsys.lang;

import static org.junit.Assert.assertEquals;

import java.util.jar.JarFile;

import org.junit.Test;

public class JarFileClassLoaderTest {
	String JAR = "regress/jarloadertest.jar";
	String TESTCLASS = "com.darwinsys.util.ArrayIterator";
	
	@Test public void testLoad() throws Exception {
		JarFileClassLoader cl = 
			new JarFileClassLoader(new JarFile(JAR));
		Class<?> c = cl.findClass(TESTCLASS);
		assertEquals(TESTCLASS, c.getName());
	}
}
