package com.darwinsys.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ObjectMerge {

	public static Object merge(Object left, Object right) throws Exception {
		Object merge = null;
		Class<?> c = left.getClass();
		if (left.getClass() != right.getClass()) {
			throw new IllegalArgumentException(left + " class != " + right);
		}
		merge = c.newInstance();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			int mod = f.getModifiers();
			if (Modifier.isFinal(mod) ||
				Modifier.isStatic(mod))
				continue; // can not merge final or static
			f.setAccessible(true);
			Object l = f.get(left);
			Object r = f.get(right);
			if (l == null && r == null) {
				continue;	// leave it null
			}
			if (l == null) {
				f.set(merge, r);	// go w/ right's val
			} else if (r == null) {
				f.set(merge, l);	// go w/ left's
			} else if (l.equals(r)) {
				f.set(merge, l);
			} else {
				// they differ, leave it null
			}
		}
		return merge;
	}
}
