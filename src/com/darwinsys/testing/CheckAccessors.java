package com.darwinsys.testing;

import static org.junit.Assert.assertEquals;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** A JUnit helper to test the setter/getter pairs in
 * the given class(es).
 */
public class TestAccessors {

	public static void process(final String className) throws Exception {
		final Class<?> c = Class.forName(className);
		process(c);
	}
	
	public static void process(final Class<?> c)  throws Exception {
		// Many class-like things cannot be instantiated:
		if (c.isInterface() ||
			c.isEnum() ||
			c.isAnnotation()) {
			return;
		}
		// Nor can abstract or non-public classes.
		int mod = c.getModifiers();
		if (Modifier.isAbstract(mod) ||
			!Modifier.isPublic(mod)) {
			return;
		}
		Constructor<?> con;
		try {
			con = c.getConstructor(new Class<?>[0]);
		} catch (NoSuchMethodException ignore) {
			return;
		}
		mod = con.getModifiers();
		if (!Modifier.isPublic(mod)) {
			return;
		}
		final Object instance = c.newInstance();
		final BeanInfo beanInfo = Introspector.getBeanInfo(c);
		final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor p : props) {
			final String propName = p.getName();
			Method writeMethod = p.getWriteMethod();
			if (writeMethod == null || 
				!(Modifier.isPublic(writeMethod.getModifiers()))) {
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
			mod = readMethod.getModifiers();
			if (!(Modifier.isPublic(readMethod.getModifiers()))) {
					// non-public get method not worth logging
					continue;
				}
			Object back = readMethod.invoke(instance, new Object[0]);
			assertEquals(c.getName() + "." + propName, value, back);
		}
	}
}
