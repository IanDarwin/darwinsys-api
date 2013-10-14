package com.darwinsys.reflection;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionUtilsTest {

	ReflectionUtils target;
	
	@Before
	public void setUp() throws Exception {
		target = new ReflectionUtils();
	}
	
	@Test
	public void testGetSetters() throws Exception {
		final List<Method> setters = 
			target.getSetters("java.util.Date");
		assertNotNull(
			"getMethods returned a list", setters);
		assertEquals(
			"getMethods returned correct list", 7, setters.size());
		
		// The order is indeterminate (unless you want to
		// rely on an implementation detail) so just make
		// sure that setTime() is present...
		boolean found = false;
		for (Method m : setters) {
			System.out.println(m);
			if ("setTime".equals(m.getName()))
				found = true;
		}
		assertTrue("found setTime()", found);
	}
}
