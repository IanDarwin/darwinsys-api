package com.darwinsys.util;

/**
 * A map entry (key-value pair), similar to java.util.Map.Entry<K,V> but easier
 * to create and more durable (not tied to a parent Iterator, which also implies
 * that setValue() methods are not written back anywhere!).
 * <br/>
 * Warning: If you think you need this class, why not just use a Map implementation?
 * @author ian
 * @version $Id$
 */
public class MapEntry<K, V> {
	/** This MapEntry's Key */
	private final K key;
	/** This MapEntry's Value */
	private V value;

	public MapEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	@Override public boolean equals(Object other) {
		if (!(other instanceof MapEntry)) {
			return false;
		}
		MapEntry e2 = (MapEntry) other;
		return (key == null ? e2.getKey() == null : key.equals(e2.getKey()))
				&& (value == null ? e2.getValue() == null : value.equals(e2
						.getValue()));
	}

	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}
}
