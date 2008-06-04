package com.darwinsys.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

	 /**
	 * Describe a Property of a class; if its getter/setter are
	 * non-null access it that way, else access it via "raw".
	 */
	static class Prop {
		String name;
		Field rawField;
		Method getter, setter;
		Prop(String name) {
			this.name = name;
		}
	 }

	/*
	 * Overload of equals for use from 'normal' equals methods
	 * to perform an exhaustive comparison (using Reflection)
	 * on all fields within the class.
	 * If both objects are null (not possible in the usual
	 * use case), treat as true. If one is null, safely
	 * return false. Otherwise, use the Reflection API.
	 * @param o1
	 * @param o2
	 * @return True if o1 and o2 should be equal under the
	 * general contract of Object.equals
	 * @see java.lang.Object#equals(Object o2);
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		// Slight variation on equals() contract: if both
		// are null, treat as same instead of NPE
		if (o1 == null && o2 == null) {
			return true;
		}
		// One is not null & the other is, safely return false
		if (o1 == null || o2 == null) {
			return false;
		}
		Class c1 = o1.getClass();
		Class c2 = o2.getClass();

		// Class objects are singleton-like, compare with ==
		if (c1 != c2) {
			System.err.println("Warn: classes are different");
			return false;
		}

		Map<String, Prop> props = getProperties(c1);
		List<String> propNames =
			new ArrayList<String>(props.keySet());
		for (String name : propNames) {
			System.err.println("Trying property " + name);
			if (!propsEquals(props.get(name).rawField, o1, o2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * A JUnit-like assertion method that uses reflection to
	 * ensure that no properties have default values; useful for
	 * testing (possibly large) Constructors to ensure that all
	 * properties are being set (and set correctly, e.g., gotta
	 * love those copy-and-paste errors in legacy constructors
	 * that are set by hand...).
	 * @param o The object (of any type) to be tested.
	 */
	public static void assertNoDefaultProperties(Object o) throws Exception {
		if (o == null) {
			throw new NullPointerException("Object may not be null");
		}
		Class c = o.getClass();
		Map<String, Prop> props = getProperties(c);
		List<String> propNames =
			new ArrayList<String>(props.keySet());
		for (String name : propNames) {
			Prop p= props.get(name);
			Object target = null;
			if (p.rawField != null) {
				target = p.rawField.get(o);
			} else if (p.getter != null) {
				target = p.getter.invoke(o, new Object[0]);
			}
			System.out.printf("Field %s, value %s%n", name, target);
			if (target == null || target.equals(Boolean.FALSE)) {
				throw new AssertionError("property " + p.name + " is default");
			}
		}
		System.out.println("assertNoDefaultProperties didn't find any problems");
	}

	/** Extract a map of all properties */
	private static Map<String, Prop> getProperties(Class c1) {
		Map<String, Prop> propsMap = new HashMap<String, Prop>();
		Field[] fields = c1.getDeclaredFields();
		for (Field f : fields) {
			int mods = f.getModifiers();
			if (Modifier.isPrivate(mods)) {
				continue;
			}
			String name = f.getName();
			Prop p = propsMap.get(name);
			if (p == null) {
				p = new Prop(name);
				propsMap.put(name, p);
			}
		}
		Method[] methods = c1.getDeclaredMethods();
		for (Method m : methods) {
			int mods = m.getModifiers();
			if (Modifier.isPrivate(mods)) {
				continue;
			}
			String name = m.getName();
			if (!name.startsWith("get") || name.startsWith("set")) {
				continue;
			}
			String propName = name.substring(3);
			Prop p = propsMap.get(propName);
			if (p == null) {
				p = new Prop(propName);
				propsMap.put(propName, p);
			}
			if (name.startsWith("get")) {
				p.getter = m;
			} else if (name.startsWith("set")) {
				p.setter = m;
			} else {
				throw new IllegalStateException("invalid name " + name);
			}
		}
		return propsMap;
	}

	private static boolean propsEquals(Member member, Object o1, Object o2) {
		if (member == null) {
			System.err.println("Warning: member is null in propsEquals");
			return false;
		}
		try {
			if (member instanceof Field) {
				Field f = (Field)member;
				System.out.println(String.format("EqualsUtils.propsEquals(%s)", f.getName()));
				Object val1 = f.get(o1);
				Object val2 = f.get(o2);
				System.err.println(val1);
				System.err.println(val2);
				return val1.equals(val2);
			} else if (member instanceof Method) {
				Method m = (Method)member;
				String methodName = m.getName();
				// System.out.println(String.format("EqualsUtils.propsEquals(%s())", methodName));
				if (methodName.startsWith("set")) {
					// only test getters here - see TGS
					return true;
				}
				Object v1 = m.invoke(o1, new Object[0]);
				Object v2 = m.invoke(o2, new Object[0]);
				if (v1 == null && v2 == null) {
					return true;
				}
				if (v1 == null || v2 == null) {
					return false;
				}
				return v1.equals(v2);
			} else
				throw new IllegalArgumentException(
					"Internal error: member " + member + " neither Method nor Field");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		/*NOTREACHED*/
	}
}
