package com.darwinsys.util;

import java.io.*;
import java.util.*;

/**
 * PropertiesMap -- a Map that loads from a Properties file, but unlike
 * Properties, preserves the ordering of the original file.
 * <p>
 * Written mainly as a demonstration of building a simple Map implementation
 * from scratch, but useful when order matters and yet you want the 
 * convenience of Map acccess.
 * @author  Ian F. Darwin
 * @version $Id$
 */
public class PropertiesMap implements Map {

	private List names = new ArrayList();
	private List values = new ArrayList();

	public void load(String fileName) throws IOException {
		if (fileName == null) {
			throw new IOException("filename is null!");
		}
		InputStream is = new FileInputStream(fileName);
		BufferedReader rdr = new BufferedReader(
			new InputStreamReader(is));
		String line;
		while ((line = rdr.readLine()) != null) {
			int ix = line.indexOf('=');
			String name = line.substring(0, ix);
			String value = line.substring(ix+1);
			names.add(name);
			values.add(value);
		}
		rdr.close();
	}

	/** Return the number of entries in the Map */
    public int size() {
		return names.size();
	}

	/** Return true if the Map is empty */
    public boolean isEmpty() {
		return names.isEmpty();
	}

	/** Return true if the given object is contained as a Key */
    public boolean containsKey(Object obj) {
		return names.contains(obj);
	}

	/** Return true if the given object is contained as a Value */
    public boolean containsValue(Object obj) {
		return values.contains(obj);
	}

	/** Get a given object */
    public Object get(Object obj) {
		return values.get(names.indexOf(obj));
	}

	/** Add a given object into this Map. */
    public Object put(Object n, Object v) {
		names.add(n);
		values.add(v);
		return n;
	}

	/** Remove a given object */
    public Object remove(Object obj) {
		int i = values.indexOf(obj);
		if (i < 0) 
			throw new IllegalArgumentException("remove(" + obj + ") not found");
		names.remove(i);
		values.remove(i);
		return obj;
	}

	/** Merge all the values from another map into this map. */
    public void putAll(java.util.Map map) {
		Iterator k = map.keySet().iterator();
		while (k.hasNext()) {
			Object key = k.next();
			Object val = map.get(key);
			put(key, val);
		}
	}

	/** Discard all object references held in the collection, i.e.,
	 * reset to its initial state.
	 */
    public void clear() {
		names.clear();
		values.clear();
	}

	/** Return the set of keys */
    public java.util.Set keySet() {
		return new HashSet(names);
	}

	/** Return a Collection containing the values */
    public java.util.Collection values() {
		return values;
	}

	/** EntrySet (not implemented, returns null) */
    public java.util.Set entrySet() {
		return null;
	}
}
