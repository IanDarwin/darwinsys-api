package com.darwinsys.util;

/** Utilities for debugging
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
@Deprecated
public class Debug {
	private static final String DEBUG_STRING = "debug.";

	/** Static method to see if a given category of debugging is enabled.
	 * Enable by setting e.g., -Ddebug.fileio to debug file I/O operations.
	 * For example:<br>
	 * if (Debug.isEnabled("fileio"))<br>
	 * 	System.out.println("Starting to read file " + fileName);
	 * @param category The category
	 * @return True if the category is enabled for debuggin
	 */
	public static boolean isEnabled(String category) {
		return System.getProperty(DEBUG_STRING + category) != null;
	}

	/** Static method to println a given message if the
	 * given category is enabled for debugging, as reported by isEnabled.
	 * @param category The category
	 * @param msg The message
	 */
	public static void println(String category, String msg) {
		if (isEnabled(category))
			System.out.println(msg);
	}
	
	/** Static method to println an arbitrary Object if the given
	 * category is enabled for debugging, as reported by isEnabled.
	 * @param category The category
	 * @param stuff The message
	 */
	public static void println(String category, Object stuff) {
		println(category, stuff.toString());
	}
	
	/** Invoke System.out.printf if and only if the given
	 * category is enabled for debugging, as reported by isEnabled.
	 * @param category The category
	 * @param format The format string
	 * @param objects The parameters for the format string
	 */
	public static void printf(String category, String format, Object...objects) {
		if (isEnabled(category))
			System.out.printf(format, objects);
	}
}
