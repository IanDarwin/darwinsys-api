package com.darwinsys.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.Ignore;

public class ClassSourceUtilsTest extends JarFileTestBase {
	
	@Ignore("failsome")
	@Test public void testListClass() {
		assertEquals(1, ClassSourceUtils.classListFromSource("java.lang.Object").size());
	}
	
	@Ignore("failsome")
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void testFailureListClass() {

		ClassSourceUtils.classListFromSource("java.lang.NoSuchClass");

	}
	
	@Ignore("failsome")
	@Test public void testListDir() {

		final List<Class<?>> list = 
			ClassSourceUtils.classListFromSource("build");

		System.out.println("testListDir() found " + list.size() + " class files");
		assertTrue(list.size() > 1);
	}
	
	@Ignore("failsome")
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
