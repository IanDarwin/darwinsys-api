package com.darwinsys.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Make instances of newer Iterator look like older Enumeration,
 * for some kind of backwards compatibility.
 */
public class IteratorEnumerationBridge<T> implements Enumeration {

	private final Iterator<T> it;

	public IteratorEnumerationBridge(Iterator<T> it) {
		this.it = it;
	}

	/** Tell whether this Enumeration has more elements 
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return it.hasNext();
	}

	/** Get the next element from this Enumeration; can
	 * only be called if hasMoreElements() is true. 
	 * @see java.util.Enumeration#nextElement()
	 */
	public T nextElement() {
		return it.next();
	}

}