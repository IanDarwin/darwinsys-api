package com.darwinsys.util;

import java.util.Iterator;
import java.util.Enumeration;

/** A GOF Adapter to make instances of old Enumeration interface
 * behave like new Iterator interface, so we only have to deal
 * with one well-defined implementation of the Iterator pattern.
 */
public class EnumerationIterator implements Iterator {

	/** The Enumeration being delegated to */
	private final Enumeration oldEnum;

	/** Construct an EnumerationIterator from an old-style Enumeration.
	 * @param  old The Enumeration to be adapted.
	 */
	public EnumerationIterator(final Enumeration old) {
		oldEnum = old;
	}

	/** Fulfuls the general contract of Iterator.hasNext(), that is,
	 * return true as long as there is at least one more item in
	 * the Iterator.
	 */
	public boolean hasNext() {
		return oldEnum.hasMoreElements();
	}
	/** Fulfuls the general contract of Iterator.next(), that is,
	 * returns the next element in the Iterator.
	 */
	public Object next() {
		return oldEnum.nextElement();
	}

	/** Remove is not implemented (optional method).
	 * @throws java.lang.UnsupportedOperationException in all cases.
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}
}
