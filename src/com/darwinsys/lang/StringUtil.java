package com.darwinsys.lang;

/** Miscellaneous string utilities:
 * arrayToCommaList - print an array with commas, "and";
 * subst - simple substitute (like 1-4's String.subst but not RE-based)
 * .
 */
public class StringUtil {

	/** Format an array of Object as a list with commas,
	 * like "apples, oranges, and bananas");
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

	/** Simple string substitution (not RE-based). */
	public static String subst(String oldStr, String newStr, String input) {
		int where = 0;
		StringBuffer sb = new StringBuffer(input);
		while ((where = StringUtil.indexOf(sb, oldStr, where)) != -1) {
			sb.replace(where, where + oldStr.length(), newStr);
		}
		return sb.toString();
	}

	/** Backwards-compability: StringBuffer.indexOf(String, int) added in
	 * JDK 1-4, but we need it here and one of my servers is on JDK 1-3.
	 */
	public static int indexOf(StringBuffer sb, String str, int fromIndex) {

		// Reject the impossible at once
		if (sb == null || str == null) {
			throw new IllegalArgumentException(
				"input strings may not be null");
		}
		if (fromIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(
				fromIndex + " is negative");
		}
		if (fromIndex + str.length() > sb.length())
			return -1;

		// OK, hunt and peck.
		int i;
		for ( ; fromIndex < sb.length(); fromIndex++) { 
			boolean foundThisRound = true;
		 	for (i = 0; foundThisRound && i < str.length(); i++) {
				if (sb.charAt(fromIndex + i) != str.charAt(i))
					foundThisRound = false;
			}
			if (foundThisRound)
				return fromIndex;
		}
		return -1;
	}
}
