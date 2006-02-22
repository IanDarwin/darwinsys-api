package com.darwinsys.util;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A Fixed-size FIFO.
 * TODO: Go Generic (need to allocate array of T).
 * Development of this program was funded by the Toronto Centre for
 * Phenogenomics (www.phenogenomics.ca).
 */
public class FixedLengthFIFO implements List {
	private final int size;
	private final String[] data;
	private int n = 0;
	private static final long serialVersionUID = 5887759670059817977L;

	/**
	 * @param initialCapacity
	 */
	public FixedLengthFIFO(int size) {
		this.size = size;
		data = new String[size];
		n = 0;
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size <= 0;
	}

	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	public Iterator iterator() {
		
		return new Iterator() {
			int ix = -1;
			final int howmany = n;
			public boolean hasNext() {
				check();
				return ix < n - 1;
			}

			public Object next() {
				check();
				if (!hasNext()) {
					throw new IllegalStateException("Called next when hasNext is false");
				}
				return data[++ix];
			}

			public void remove() {
				check();
				if (ix == -1) {
					throw new IllegalStateException("You called remove before next");
				}
				throw new IllegalArgumentException("method not implemented");
			}
			
			private void check() {
				if (howmany != n) {
					throw new ConcurrentModificationException();
				}
			}
		};
	}

	public Object[] toArray() {
		return data;
	}

	public Object[] toArray(Object[] a) {
		return data;
	}

	public boolean add(Object o) {
		if (n >= size) {
			remove(data[0]);			
		}
		data[n++] = (String)o;
		return true;
	}

	public boolean remove(Object o) {
		int i = indexOf(o);
		if (i == -1) {
			return false;
		} 
		return remove(i) != null;
	}
	
	public Object remove(int i) {	
		if (i > n) {
			return null;
		}
		// We are going to remove one element;
		n--;
		// do the two easy cases first.
		if (i == 0) {
			System.arraycopy(data, 1, data, 0, n - 1);
			return true;
		}
		if (i == n-1) {
			data[i] = null;
			return true;
		}
		System.arraycopy(data, i+1, data, i, n - i - 1);
		return true;
	}

	public boolean containsAll(Collection c) {
		throw new IllegalArgumentException("method not implemented");
	}

	public boolean addAll(Collection c) {
		throw new IllegalArgumentException("method not implemented");
	}

	public boolean addAll(int index, Collection c) {
		throw new IllegalArgumentException("method not implemented");
	}

	public boolean removeAll(Collection c) {
		throw new IllegalArgumentException("method not implemented");
	}

	public boolean retainAll(Collection c) {
		throw new IllegalArgumentException("method not implemented");
	}

	public void clear() {
		for (int i = 0; i < n; i++) {
			data[i] = null;
		}
	}

	public Object get(int index) {
		if (n > size) {
			throw new ArrayIndexOutOfBoundsException(Integer.toString(index));
		}
		return data[index];
	}

	public Object set(int index, Object element) {
		throw new IllegalArgumentException("method not implemented");
	}

	public void add(int index, Object element) {
		throw new IllegalArgumentException("method not implemented");
	}

	public int indexOf(Object o) {
		for (int i = 0; i < n; i++) {
			if (data[i].equals(o)) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		for (int i = n - 1; i >= 0; i++) {
			if (data[i].equals(o)) {
				return i;
			}
		}
		return -1;
	}

	public ListIterator listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public List subList(int fromIndex, int toIndex) {
		throw new IllegalArgumentException("method not implemented");
	}


	

}
