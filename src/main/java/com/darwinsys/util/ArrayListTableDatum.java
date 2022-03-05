package com.darwinsys.util;

/** Class to hold a name and a value from a Properties; the
 * ArrayList contains one of these per Properties entry.
 * Needs to be a non-inner class only to allow the Panel
 * to construct instances of it.
 */
public class ArrayListTableDatum {
	private String name;
	private String value;
	/** Constructor used below
	 * @param n the name
	 * @param v the value
	 */
	public ArrayListTableDatum(String n, String v) {
		name = n; value = v;
	}
	/** public no-arg constructor, req'd for Add operation */
	public ArrayListTableDatum() {
		// Nothing
	}
	/** Get the name */
	public String getName() {
		return name;
	}
	/** Get the value */
	public String getValue() {
		return value;
	}
	/** Set the name
	 * @param name The Name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** Set the value
	 * @param value The value.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
