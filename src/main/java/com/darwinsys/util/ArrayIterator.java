// BEGIN main
package com.darwinsys.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Demonstrate the Iterator and Iterable interfaces, showing how
 * to write a simple Iterator for an Array of Objects.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class ArrayIterator<T> implements Iterable<T>, Iterator<T> {
	/** The data to be iterated over. */
	protected T[] data;

	protected int index = 0;

	/** Construct an ArrayIterator object.
	 * @param d The array of objects to be iterated over.
	 */
	public ArrayIterator(final T[] d) {
		setData(d);
	}

	/** (Re)set the data array to the given array, and reset the iterator.
	 * @param d The array of objects to be iterated over.
	 */
	public void setData(final T[] d) {
		this.data = d;
		index = 0;
	}

	// -------------------
	// Methods of Iterable
	// -------------------

	@Override
	public Iterator<T> iterator() {
		index = 0;
		return this;	// since main class implements both interfaces
	}

	// -------------------
	// Methods of Iterator
	// -------------------


	/** 
	 * Tell if there are any more elements.
	 * @return true if not at the end, i.e., if next() will succeed.
	 * @return false if next() will throw an exception.
	 */
	@Override
	public boolean hasNext() {
		return (index < data.length);
	}

	/** Returns the next element from the data */
	@Override
	public T next() {
		if (hasNext()) {
			return data[index++];
		}
		throw new NoSuchElementException("only " + data.length + " elements");
	}

	/** Remove the object that next() just returned.
	 * An Iterator is not required to support this interface,
	 * and we don't.
	 * @throws UnsupportedOperationException unconditionally
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException(
			"This demo Iterator does not implement the remove method");
	}
}
// END main
