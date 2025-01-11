package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ClassSourceUtilsTest extends JarFileTestBase {

	@Disabled("failsome")
	@Test
	void listClass() {
		assertEquals(1, ClassSourceUtils.classListFromSource("java.lang.Object").size());
	}

	@Disabled("failsome")
	@Test
	void failureListClass() {
		assertThrows(java.lang.IllegalArgumentException.class, () ->

			ClassSourceUtils.classListFromSource("java.lang.NoSuchClass"));

	}

	@Disabled("failsome")
	@Test
	void listDir() {

		final List<Class<?>> list = 
			ClassSourceUtils.classListFromSource("build");

		System.out.println("testListDir() found " + list.size() + " class files");
		assertTrue(list.size() > 1);
	}

	@Disabled("failsome")
	@Test
	void listJar() throws Exception {

		final List<Class<?>> list =
			// test jar file - See parent class @BeforeClass method
			ClassSourceUtils.classListFromSource(jarFileName);
		
		assertEquals(1, list.size());
		final Class<?> clazz0 = list.get(0);
		// See parent class @BeforeClass method
		assertEquals(TESTCLASS, clazz0.getName());
		final Object newInstance = clazz0.newInstance();
		assertEquals(TESTCLASS, newInstance.getClass().getName());
	}
}
