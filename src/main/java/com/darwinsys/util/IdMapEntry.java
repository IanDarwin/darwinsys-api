package com.darwinsys.util;

/**
 * An IdMapEntry is one int-String pair, for example, an "id" or primary key
 * in a database and a name or description of the item in the named row.
 * IdMapEntry objects are immutable.
 * <p>Rather like a java.util.Map.Entry but without needing to convert.
 */
public record IdMapEntry(int id, String name) {
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != IdMapEntry.class) {
			return false;
		}
		IdMapEntry i2 = (IdMapEntry)obj;
		return id == i2.id && name.equals(i2.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() | id;
	}

	public int getKey() {
		return id;
	}

	public String getValue() {
		return name;
	}
}
