import java.util.*;

/** Demonstrate the Iterator interface (new in 1.2).
 * @author	Ian Darwin, ian@darwinsys.com
 * @version	$Id$
 */
public class IterDemo implements Iterator {
	/** Some data. Not very interesting but
	 * this is meant to be a simple demo.
	 */
	protected String[] data = { "one", "two", "three" };

	protected int index = 0;

	/** Returns true if not at the end, i.e., if next() will return an element.
	 * Returns false if next() will throw an exception.
	 */
	public boolean hasNext() {
		return (index < data.length);
	}

	/** Returns the next element from the data */
	public Object next() {
		if (index >= data.length)
			throw new IndexOutOfBoundsException("only " + data.length + " elements");
		return data[index++];
	}

	/** Remove the object that next() just returned.
	 * The Iterator is not required to support this interface.
	 */
	public void remove() {
		throw new UnsupportedOperationException(
			"This demo does not implement the remove method");
	}

	/** Simple tryout */
	public static void main(String unused[]) {
		IterDemo it = new IterDemo();
		while (it.hasNext())
			System.out.println(it.next());
	}
}
