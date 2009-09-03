
package rejmi.action;

import java.lang.reflect.*;

import org.jboss.seam.annotations.*;

import darwinian.contacts.Contact;

@Name("contactMerge")
public class ContactMerge {

	@In Contact left;
	@In Contact right;

	Contact merge;

	public void merge() throws Exception {
		if (left.getClass() != right.getClass()) {
			throw new IllegalArgumentException(left + " class != " + right);
		}
		merge = new Contact();
		Class c = left.getClass();
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
