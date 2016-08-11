package com.darwinsys.lang;

/** 
 * Miscellaneous string utilities:
 */
public class StringUtil {
	
	public static final String ENCODING_UTF_8 = "UTF-8";

	/** Like String instance method of same name, but null-safe.
	 * @param s The input string
	 * @return True if input either is null, or is empty as per String isEmpty().
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	/** Format an array of Object as a list with commas,
	 * like "apples, oranges, and bananas");
	 * XXX Should have a boolean for the final comma :-)
	 * @param array The objects to be stringified
	 * @return a pretty list
	 */
	public static String arrayToCommaList(Object[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<array.length; i++) {
			if (i > 0 && i < array.length - 1) {
				sb.append(',');
			}
			if (i > 0) {
				sb.append(' ');
			}
			if (i==(array.length-1)) {
				sb.append("and ");
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/** Reverse a String character by character, 'not now' returns 'won ton'
	 * @param str The string to reverse
	 * @return The reversed string
	 */
	public static String reverse(String str) {
		return new StringBuffer(str).reverse().toString();
	}
	
	/** 
	 * Replace all occurrences of one string with another (not RE-based).
	 * @param oldStr The string to be replaced
	 * @param newStr The string to replace it with
	 * @param input The string in which do do the replacement
	 * @return The string with replacements done
	 */
	public static String subst(String oldStr, String newStr, String input) {
		int where = 0;
		StringBuffer sb = new StringBuffer(input);
		while ((where = StringUtil.indexOf(sb, oldStr, where)) != -1) {
			sb.replace(where, where + oldStr.length(), newStr);
		}
		return sb.toString();
	}

	// For backwards compatability only, do not use in new code.
	public static int indexOf(StringBuffer sb, String str, int fromIndex) {
		return sb.indexOf(str, fromIndex);
	}
}
