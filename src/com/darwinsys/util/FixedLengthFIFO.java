package com.darwinsys.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A Fixed-size FIFO.
 * Development of this program was funded by the Toronto Centre for
 * Phenogenomics (www.phenogenomics.ca).
 * XXX Could reduce size by basing on AbstractList.
 */
public class FixedLengthFIFO<T> implements List<T> {
	private final int size;
	private final T[] data;
	private int n = 0;
	private static final long serialVersionUID = 5887759670059817977L;

	/**
	 * @param initialCapacity
	 */
	public FixedLengthFIFO(int size) {
		this.size = size;
		data = (T[])new Object[size];	// XXX not quite right
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

	public Iterator<T> iterator() {
		
		return new Iterator<T>() {
			int ix = -1;
			final int howmany = n;
			public boolean hasNext() {
				check();
				return ix < n - 1;
			}

			public T next() {
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

	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), n);
		}
        System.arraycopy(data, 0, a, 0, n);
		return a;
	}

	public boolean add(T o) {
		if (n >= size) {
			remove(data[0]);			
		}
		data[n++] = o;
		return true;
	}

	public boolean remove(Object o) {
		int i = indexOf(o);
		if (i == -1) {
			return false;
		} 
		return remove(i) != null;
	}
	
	public T remove(int i) {	
		if (i > n) {
			return null;
		}
		// We are going to remove one element;
		n--;
		final T old = data[i];
		// do the two easy cases first.
		if (i == 0) {
			System.arraycopy(data, 1, data, 0, n - 1);
			return old;
		}
		if (i == n-1) {
			data[i] = null;
			return old;
		}
		System.arraycopy(data, i+1, data, i, n - i - 1);
		return old;
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

	public T get(int index) {
		if (n > size) {
			throw new ArrayIndexOutOfBoundsException(Integer.toString(index));
		}
		return data[index];
	}

	public T set(int index, T element) {
		throw new IllegalArgumentException("method not implemented");
	}

	public void add(int index, T element) {
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

	public ListIterator<T> listIterator() {
		throw new IllegalArgumentException("method not implemented");
	}

	public ListIterator<T> listIterator(int index) {
		throw new IllegalArgumentException("method not implemented");
	}

	public List<T> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > size()) {
			throw new IllegalArgumentException("index out of range");
		}
		int newlen = toIndex - fromIndex + 1;
		T[] newdata = (T[])new Object[newlen];
		System.arraycopy(newdata, 0, data, fromIndex, newlen);
		return Arrays.asList(newdata);
	}

	public int hashCode() {
		return data.hashCode();
	}
	
	private ArrayList x;
}
