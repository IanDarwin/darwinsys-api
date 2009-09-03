
package rejmi.action;

import java.lang.reflect.*;

import org.jboss.seam.annotations.Name;

@Name("objectMerge")
public class ObjectMerge {

	@In Object left;
	@In Object right;

	Object merge;

	public void merge() throws Exception {
		Class c = left.getClass();
		if (left.getClass() != right.getClass()) {
			throw new IllegalArgumentException(left + " class != " + right);
		}
		merge = c.newInstance();
		Field[] fields = c.getFields();
		for (Field f : fields) {
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
		
	}
}
