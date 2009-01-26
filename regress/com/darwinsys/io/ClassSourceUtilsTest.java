package com.darwinsys.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ClassSourceUtilsTest {
	
	@Test public void testListClass() {
		assertEquals(1, ClassSourceUtils.classListFromSource("java.lang.Object").size());
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void testFailureListClass() {
		ClassSourceUtils.classListFromSource("java.lang.NoSuchClass");
	}
	
	@Test public void testListDir() {
		final List<Class<?>> list = ClassSourceUtils.classListFromSource("build");
		System.out.println(list.size());
		assertTrue(list.size() > 1);
	}
}
