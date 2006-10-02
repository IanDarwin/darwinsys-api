package com.darwinsys.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualsUtils {

	/** Use Reflection to perform an exhaustive comparison
	 * on all fields within the class.
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null || o2 == null) {
			return false;
		}
		Class c1 = o1.getClass();
		Class c2 = o2.getClass();

		// Class objects are singleton-like, compare with ==
		if (c1 != c2)
			return false;

		// Will be a map of all properties
		Map<String, Member> props = new HashMap<String, Member>();
		Field[] fields = c1.getDeclaredFields();
		for (Field f : fields) {
			int mods = f.getModifiers();
			if (!Modifier.isPrivate(mods)) {
				props.put(f.getName(), f);
			}
		}
		Method[] methods = c1.getDeclaredMethods();
		for (Method m : methods) {
			String name = m.getName();
			if (name.startsWith("get")) {
				String methodPropName = name.substring(3);
				props.put(methodPropName, m);
				System.out.println("EqualsUtils.equals(): put " + methodPropName);

			}
		}
		List<String> propNames =
			new ArrayList<String>(props.keySet());
		for (String name : propNames) {
			if (!propsEquals(props.get(name), o1, o2)) {
				return false;
			}
		}
		return true;
	}

	private static boolean propsEquals(Member member, Object o1, Object o2) {
		try {
		if (member instanceof Field) {
			Field f = (Field)member;
			System.out.println(
					String.format("EqualsUtils.propsEquals(%s)", f.getName()));
			Object val1 = f.get(o1);
			Object val2 = f.get(o2);
			return val1.equals(val2);
		} else if (member instanceof Method) {
			Method m = (Method)member;
			String methodName = m.getName();
			System.out.println(
				String.format("EqualsUtils.propsEquals(%s())", methodName));
			if (methodName.startsWith("set")) {
				// only test getters here - see TGS
				return true;
			}
			Object v1 = m.invoke(o1, new Object[0]);
			Object v2 = m.invoke(o2, new Object[0]);
			return v1.equals(v2);
		} else
			throw new IllegalArgumentException("Internal error: member neither Method nor Field");
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		/*NOTREACHED*/
	}
}
