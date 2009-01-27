package com.darwinsys.lang;

import static org.junit.Assert.assertEquals;

import java.util.jar.JarFile;

import org.junit.Test;

import com.darwinsys.io.JarFileTestBase;

public class JarFileClassLoaderTest extends JarFileTestBase {
		
	@Test public void testLoad() throws Exception {
		JarFileClassLoader cl = 
			new JarFileClassLoader(new JarFile(jarFileName));
		Class<?> c = cl.findClass(TESTCLASS);
		assertEquals(TESTCLASS, c.getName());
	}
}
