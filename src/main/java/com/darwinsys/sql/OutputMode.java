package com.darwinsys.sql;

/** The set of all valid modes. Short, lowercase names were used
 * for simple use in \mX where X is one of the names.
 */
public enum OutputMode {
	/** Mode for Text */
	t("Text"),
	/** Mode for HTML output */
	h("HTML"),
	/** Mode for SQL output */
	s("SQL"),
	/** Mode for XML output */
	x("XML"),
	/** Mode for JTable */
	j("Table");

	String name;
	OutputMode(String n) {
		name = n;
	}
	public String toString() {
		return name;
	}
}