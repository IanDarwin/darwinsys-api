package com.darwinsys.util;

/** Class to hold a name and a value from a Properties; the
 * ArrayList contains one of these per Properties entry.
 * Needs to be a non-inner class only to allow the Panel
 * to construct instances of it.
 */
public class ArrayListTableDatum {
	String name;
	String value;
	/** Constructor used below */
	public ArrayListTableDatum(String n, String v) {
		name = n; value = v;
	}
	/** public no-arg constructor, req'd for Add operation */
	public ArrayListTableDatum() {
		// Nothing
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
