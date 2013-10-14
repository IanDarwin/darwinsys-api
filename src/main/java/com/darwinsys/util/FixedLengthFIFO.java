package com.darwinsys.util;

import java.lang.reflect.Array;
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
 * XXX Could reduce code size by basing on AbstractList.
 */
public class FixedLengthFIFO<T> implements List<T> {
	private final int size;
	/** Used to protect against concurrentmodification in iterator;
	 * must be incremented for any change operation
	 */
	private long generation;
	private final T[] data;
	private int n = 0;
	private static final long serialVersionUID = 5887759670059817977L;

	/**
	 * Construct a FIFO of a fixed (maximum) length.
	 * @param size The maximum number of items to hold in the FIFO
	 */
	@SuppressWarnings("unchecked")
	public FixedLengthFIFO(int size) {
		this.size = size;
		data = (T[])new Object[size];	// You can not get rid of this warning without suppresswarnings
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
			final long gen = generation;
			public boolean hasNext() {
				check();
				return ix < n - 1;
			}

			public T next() {
				check();
				if (!hasNext()) {
					throw new IllegalStateException("Called next when hasNext is false");
				}
				return (T)data[++ix];
			}

			public void remove() {
				check();
				if (ix == -1) {
					throw new IllegalStateException("You called remove before next");
				}
				throw new UnsupportedOperationException("iterator.remove");
			}
			
			private void check() {
				if (gen != generation) {
					throw new ConcurrentModificationException();
				}
			}
		};
	}

	public Object[] toArray() {
		return data.clone();
	}

	@SuppressWarnings("unchecked")
	public <U> U[] toArray(U[] a) {
		if (a.length < size) {
            a = (U[])Array.newInstance(a.getClass().getComponentType(), n);
		}
        System.arraycopy(data, 0, a, 0, n);
		return a;
	}

	/** Add an element to the FIFO. Unlike a normal List, this FIFO
	 * will not grow indefinitely, but adding will not make the FIFO
	 * exceed its fixed size.
	 * @param o
	 * @return True, since you can always add one more object.
	 */
	public boolean add(T o) {
		++generation;
		if (n >= size) {
			remove(data[0]);			
		}
		data[n++] = o;
		return true;
	}

	/**
	 * Remove the given element if it's still in the FIFO.
	 * @param o The element to be removed.
	 */
	public boolean remove(Object o) {
		// generation changed in overload
		int i = indexOf(o);
		if (i == -1) {
			return false;
		} 
		return remove(i) != null;
	}
	
	public T remove(int i) {
		++generation;
		if (i > n) {
			return null;
		}
		// We are going to remove one element;
		n--;
		final T old = (T)data[i];
		// do some easy cases first.
		if (size() == 0) { // n == 0 here means length == 1, since we did n-- above.
			data[0] = null;
			return old;
		}
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
		throw new UnsupportedOperationException("containsAll");
	}

	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("addAll");
	}

	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("addAll");
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("removeAll");
	}

	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException("retainAll");
	}

	public void clear() {
		++generation;
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
		if (index > size)
		throw new IndexOutOfBoundsException(Integer.toString(index));
		for (int ix = n; ix < index; ix++) {
			data[ix] = null;
		}
		data[index] = element;
		return element;
	}

	public void add(int index, T element) {
		throw new UnsupportedOperationException("add(int, element)");
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
		for (int i = n - 1; i >= 0; i--) {
			if (data[i].equals(o)) {
				return i;
			}
		}
		return -1;
	}

	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException("listIterator");
	}

	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException("listIterator(int)");
	}

	@SuppressWarnings("unchecked")
	public List<T> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > size()) {
			throw new IndexOutOfBoundsException("must be in 0.." + (size()-1));
		}
		int newlen = toIndex - fromIndex + 1;
		T[] newdata = (T[])new Object[newlen];
		System.arraycopy(data, fromIndex, newdata, 0, newlen);
		return Arrays.asList(newdata);
	}

	public int hashCode() {
		return data.hashCode();
	}
}
