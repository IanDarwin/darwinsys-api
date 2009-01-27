package com.darwinsys.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ClassSourceUtilsTest extends JarFileTestBase {
	
	@Test public void testListClass() {
		assertEquals(1, ClassSourceUtils.classListFromSource("java.lang.Object").size());
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void testFailureListClass() {

		ClassSourceUtils.classListFromSource("java.lang.NoSuchClass");

	}
	
	@Test public void testListDir() {

		final List<Class<?>> list = 
			ClassSourceUtils.classListFromSource("build");

		System.out.println("testListDir() found" + list.size());
		assertTrue(list.size() > 1);
	}
	
	@Test public void testListJar() throws Exception {

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
