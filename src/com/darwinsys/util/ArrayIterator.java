package com.darwinsys.util;

import java.util.Iterator;

/** De-mystify the Iterator interface, showing how
 * to write a simple Iterator for an Array of Objects.
 * @author	Ian Darwin, http://www.darwinsys.com/
 * @version	$Id$
 */
public class ArrayIterator implements Iterator {
	/** The data to be iterated over. */
	protected Object[] data;

	protected int index = 0;

	/** Construct an ArrayIterator object.
	 * @param data The array of objects to be iterated over.
	 */
	public ArrayIterator(Object[] data) {
		setData(data);
	}

	/** (Re)set the data array to the given array, and reset the iterator.
	 * @param data The array of objects to be iterated over.
	 */
	public void setData(Object[] data) {
		this.data = data;
		index = 0;
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
