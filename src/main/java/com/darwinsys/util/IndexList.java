package com.darwinsys.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * A general-purpose List, in which objects keep their identity (index),
 * that is, insert() operations do not renumber remaining objects.
 * Hence, more like a real array than an ArrayList is(!).
 * </p><p>
 * Not necessarily for production use; written as an example of
 * implementing the List interface.
 */
public class IndexList<T> implements List<T> {
	/** high water mark */
	private int hwm = 0;

	/** Implementation: data */
	private T[] data;

	/** The initial size of an instance's internal store */
	public static final int DEFAULT_START_SIZE = 42;

	public IndexList() {
		this(DEFAULT_START_SIZE);
	}
	
	@SuppressWarnings("unchecked")
	public IndexList(int startSize) {
		data = (T[])new Object[startSize];
		hwm = 0;
	}

	private void ensureCapacity(int i) {
		if (i >= data.length) {
			@SuppressWarnings("unchecked")
			T[] newData = (T[])Array.newInstance(data[0].getClass(), i + 10);
			System.arraycopy(data, 0, newData, 0, hwm);
			data = newData;
		}
	}

	@Override
    public int size() {
		return hwm;
	}
	@Override
    public boolean isEmpty() {
		return hwm > 0;
	}
	@Override
    public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	/** Add the given object to the end of the list */
	@Override
    public boolean add(T o) {
		ensureCapacity(hwm);
		data[hwm++] = o;
		return true;
	}
	/** remove() simply sets the given value to null.
	 */
	@Override
    public boolean remove(Object o) {
		int i;
		if ((i = indexOf(o)) == -1)
			return false;
		remove(i);
		return true;
	}

	/** remove() simply sets the given value to null.
	 */
	@Override
    public T remove(int i) {
		T old = data[i];
		data[i] = null;
		return old;
	}

	/** removeAll removes all the elements in a Collection from this List
	 * <br>NOT IMPLEMENTED.
	 */
	@Override
    public boolean removeAll(Collection c) {
		throw new IllegalStateException(
			"removeAll method not implemented in IndexList");
	}

    @SuppressWarnings("unchecked")
	@Override
    public Iterator<T> iterator() {
		Object[] newData = new Object[hwm];
		System.arraycopy(data, 0, newData, 0, hwm);
		return new ArrayIterator(newData);
	}

	/** Return the collection as an Array of Object */
	@Override
    public Object[] toArray() {
		return (Object[])data.clone();
	}

	/** Return the collection as an Array of newData's type */
    @SuppressWarnings("unchecked")
	@Override
    public Object[] toArray(Object[] newData) {
		if (newData.length != hwm) {
			throw new IllegalArgumentException("newData length != current");
		}
		System.arraycopy(data, 0, newData, 0, hwm);
		return newData;
	}

	@Override
    public boolean containsAll(Collection c) {
		Iterator it = c.iterator();
		while (it.hasNext()) {
			if (indexOf(it.next()) == -1) {
				return false;
			}
		}
		return true;
	}

    @SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection c) {
		Iterator<T> it = c.iterator();
		while (it.hasNext()) {
			add(it.next());
		}
		return true;
	}

	@Override
    public boolean addAll(int i, Collection c) {
		throw new IllegalStateException(
			"addAll method not implemented in IndexList");
	}

	@Override
    public boolean retainAll(Collection c) {
		throw new IllegalStateException(
			"removeAll method not implemented in IndexList");
	}

    @SuppressWarnings("unchecked")
	@Override
    public void clear() {
		data = (T[])new Object[DEFAULT_START_SIZE];
		hwm = 0;
	}

	@Override
    public int hashCode() {
		return data.hashCode();
	}

	@Override
    public T get(int i) {
		return data[i];
	}

	@Override
    public T set(int i, T o) {
		ensureCapacity(i);
		T old = data[i];
		data[i] = o;
		return old;
	}

	/** Unlike the general contract of List, this will replace, not insert
	 * before, the object at the given index.
	 */
	@Override
    public void add(int i, T o) {
		ensureCapacity(i);
		data[i] = o;
	}

	/** Find the location where this object is referenced, or null */
	@Override
    public int indexOf(Object o) {
		for (int i=0; i<hwm; i++) {
			if (o == data[i]) {
				return i;
			}
		}
		return -1;
	}
	@Override
    public int lastIndexOf(Object o) {
		for (int i=hwm-1; i>=0; i--) {
			if (o == data[i]) {
				return i;
			}
		}
		return -1;
	}
	@Override
    public ListIterator<T> listIterator() {
		throw new IllegalStateException(
			"listIterator method not implemented in IndexList");
	}
	@Override
    public ListIterator<T> listIterator(int i) {
		throw new IllegalStateException(
			"listIterator method not implemented in IndexList");
	}
	@Override
    public List<T> subList(int from, int to) {
		throw new IllegalStateException(
			"subList method not implemented in IndexList");
	}
}
