package com.darwinsys.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the Constructors and methods
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class ReflectionUtils {

	public List<Method> getSetters(String cName) throws ClassNotFoundException {
		Class c = Class.forName(cName);
		Method[] meths = c.getMethods();
		List<Method> list = new ArrayList<Method>();
		for (Method m : meths) {
			if (m.getName().startsWith("set")) {
				list.add(m);
			}
		}
		return list;
	}
}
