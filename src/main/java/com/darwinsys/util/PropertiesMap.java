package com.darwinsys.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PropertiesMap -- a Map that loads from a Properties file, but unlike
 * Properties, preserves the ordering of the original file.
 * <p>
 * Written mainly as a demonstration of building a simple Map implementation
 * from scratch, but useful when order matters and yet you want the 
 * convenience of Map acccess.
 * @author  Ian F. Darwin
 */
public class PropertiesMap implements Map<String,String> {

	private List<String> names = new ArrayList<String>();
	private List<String> values = new ArrayList<String>();

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
	public String get(Object 	obj) {
		int where = names.indexOf(obj);
		if (where == -1) {
			return null;
		}
		return values.get(where);
	}

	/** Add a given object into this Map. */
    public String put(String n, String v) {
		names.add(n);
		values.add(v);
		return n;
	}

	/** Remove a given object */
    public String remove(Object obj) {
    		String o = obj.toString();
		int i = values.indexOf(o);
		if (i < 0) 
			throw new IllegalArgumentException("remove(" + obj + ") not found");
		names.remove(i);
		values.remove(i);
		return o;
	}

	/** Merge all the values from another map into this map. */
    @SuppressWarnings("unchecked")
	public void putAll(Map map) {   
		Iterator<Map.Entry> k = map.entrySet().iterator();
		while (k.hasNext()) {
			Map.Entry<String,String> e = k.next();
			String key = e.getKey();
			String val = e.getValue();
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
    public java.util.Set<String> keySet() {
		return new HashSet<String>(names);
	}

	/** Return a Collection containing the values */
    public java.util.Collection<String> values() {
		return values;
	}

	/** EntrySet (not implemented, returns null) */
    public Set<Entry<String, String>> entrySet() {
		return null;
	}
}
