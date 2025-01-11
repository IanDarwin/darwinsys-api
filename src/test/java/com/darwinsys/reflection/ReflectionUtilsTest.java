package com.darwinsys.reflection;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

	ReflectionUtils target;

	@BeforeEach
	void setUp() throws Exception {
		target = new ReflectionUtils();
	}

	@Test
	void getSetters() throws Exception {
		final List<Method> setters = 
			target.getSetters("java.util.Date");
		assertNotNull(
			setters, "getMethods returned a list");
		assertEquals(
			7, setters.size(), "getMethods returned correct list");
		
		// The order is indeterminate (unless you want to
		// rely on an implementation detail) so just make
		// sure that setTime() is present...
		boolean found = false;
		for (Method m : setters) {
			System.out.println(m);
			if ("setTime".equals(m.getName()))
				found = true;
		}
		assertTrue(found, "found setTime()");
	}
}
