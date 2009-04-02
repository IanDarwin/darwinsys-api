package com.darwinsys.testing;

import static org.junit.Assert.assertEquals;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** A JUnit helper to test the setter/getter pairs in
 * the given class(es).
 */
public class TestAccessors {

	private final static boolean debug = true;
	
	public static void debug(String s) {
		if (debug) {
			System.out.println(s);
		}
	}
	
	public static void process(final String className) throws Exception {
		final Class<?> c = Class.forName(className);
		process(c);
	}
	
	private static boolean isPublic(Member m) {
		return Modifier.isPublic(m.getModifiers());
	}
	
	public static void process(final Class<?> c)  throws Exception {
		// Many class-like things cannot be instantiated:
		if (c.isInterface() ||
			c.isEnum() ||
			c.isAnnotation()) {
			debug(c + " not an instantiable class");
			return;
		}
		// Nor can abstract classes.
		if (Modifier.isAbstract(c.getModifiers())) {
			debug(c + " is abstract");
			return;
		}
		Constructor<?> con;
		try {
			con = c.getConstructor((Class[])null);
			if (!isPublic(con)) {
				debug(c + ": constructor not public");
				return;
			}
		} catch (NoSuchMethodException ignore) {
			debug(c + ": getConstructor: NoSuchMethodException");
		}
		final Object instance = c.newInstance();
		final BeanInfo beanInfo = Introspector.getBeanInfo(c);
		final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor p : props) {
			final String propName = p.getName();
			Method writeMethod = p.getWriteMethod();
			if (writeMethod == null || 
				!isPublic(writeMethod)) {
				// no set method not worth logging, i.e., Object.getClass()
				continue;
			}
			final Class<?> type = p.getPropertyType();
			Object value = RandomDataGenerator.getRandomValue(type);
			if (value == null) {
				continue;	// can't test this setter/getter
			}
			writeMethod.invoke(instance, new Object[]{value});
			
			final Method readMethod = p.getReadMethod();
			if (readMethod == null)
				continue;
			if (!isPublic(readMethod)) {
					// non-public get method not worth logging
					continue;
				}
			Object back = readMethod.invoke(instance, new Object[0]);
			assertEquals(c.getName() + "." + propName, value, back);
		}
	}
}
