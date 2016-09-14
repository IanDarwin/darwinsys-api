package com.darwinsys.diff;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** 
 * Compare two objects and report in detail on the diffs;
 * probably useful in audit logs, among other places.
 * <pre>
 * final List&lt;DiffField&gt; diffs = DiffObj.diffObj(oldObj, newObj);
 * for (DiffObj.DiffField d : diffs) {
 *     auditlog.printf("User %s changed %s from %s to %s%n",
 *         userName, d.name, d.oldValue, d.newValue);
 * </pre>
 * @author Ian Darwin
 */
public class DiffObj {

	public static class DiffField {
		public DiffField(String name, Object oldFieldValue, Object newFieldValue) {
			this.name = name;
			this.oldVal = oldFieldValue;
			this.newVal = newFieldValue;
		}
		String name;
		Object oldVal;
		Object newVal;
	}

	public static List<DiffField> diffObj(Object oldValue, Object newValue) {
		List<DiffField> allDiffs = new ArrayList<>();
		Class cOld = oldValue.getClass(), cNew = newValue.getClass();
		if (cOld != cNew) {
			throw new IllegalArgumentException("Objects to be compared must be of identical class");
		}
		Class c;
		for (c = cOld; c!= null; c = c.getSuperclass()) {
			Field[] allF = c.getDeclaredFields();
			
			for (Field f : allF) {
				if (f.getName().startsWith("this$")) {
					continue;
				}
				f.setAccessible(true);
				try {
					final Object oldFieldValue = f.get(oldValue);
					final Object newFieldValue = f.get(newValue);
					if (!oldFieldValue.equals(newFieldValue)) {
						allDiffs.add(new DiffField(f.getName(), oldFieldValue, newFieldValue));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					System.err.println("Ignoring error: " + e + " in field " + f);
				}
			}
		}
		return allDiffs;
	}
}
