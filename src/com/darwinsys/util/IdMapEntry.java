package com.darwinsys.util;

/**
 * An IdMapEntry is one int-String pair, for example, an "id" or primary key
 * in a database and a name or description of the item in the named row.
 * IdMapEntry objects are immutable.
 * <p>Rather like a java.util.Map.Entry but without needing to convert.
 * @version $Id$
 */
public class IdMapEntry {
	int id;
	String name;

	public IdMapEntry(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getKey() {
		return id;
	}

	public String getValue() {
		return name;
	}
}
