import java.util.*;

/** De-mystify the Iterator interface (new in 1.2), showing how
 * to write an Iterator for an Array of Objects.
 * @author	Ian Darwin, ian@darwinsys.com
 * @version	$Id$
 */
public class ArrayIterator implements Iterator {
	/** The data to be sorted. */
	protected Object[] data = { "one", "two", "three" };

	protected int index = 0;

	/** Constructor */
	public ArrayIterator(Object[] d) {
		data = d;
	}

	/** Default Constructor */
	public ArrayIterator() {
	}

	/** 
	 * Tell if there are any more elements.
	 * @return true if not at the end, i.e., if next() will succeed.
	 * @return false if next() will throw an exception.
	 */
	public boolean hasNext() {
		return (index < data.length);
	}

	/** Returns the next element from the data */
	public Object next() {
		if (hasNext()) {
			return data[index++];
		}
		throw new IndexOutOfBoundsException("only " + data.length + " elements");
	}

	/** Remove the object that next() just returned.
	 * An Iterator is not required to support this interface,
	 * and we certainly don't!
	 */
	public void remove() {
		throw new UnsupportedOperationException(
			"This demo does not implement the remove method");
	}

}
