package com.darwinsys.util;

import java.util.Enumeration;
import java.util.Iterator;

/** A GOF Adapter to make instances of old Enumeration interface
 * behave like new Iterator interface, so we only have to deal
 * with one well-defined implementation of the Iterator pattern.
 */
public class EnumerationIterator<T> implements Iterator {

	/** The Enumeration being delegated to */
	private final Enumeration<T> oldEnum;

	/** Construct an EnumerationIterator from an old-style Enumeration.
	 * @param  old The Enumeration to be adapted.
	 */
	public EnumerationIterator(final Enumeration<T> old) {
		oldEnum = old;
	}

	/** Fulfills the general contract of Iterator.hasNext(), that is,
	 * return true as long as there is at least one more item in
	 * the Iterator.
	 */
	public boolean hasNext() {
		return oldEnum.hasMoreElements();
	}
	/** Fulfuls the general contract of Iterator.next(), that is,
	 * returns the next element in the Iterator.
	 */
	public T next() {
		return oldEnum.nextElement();
	}

	/** Remove is not implemented (optional method).
	 * @throws java.lang.UnsupportedOperationException in all cases.
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}
}
