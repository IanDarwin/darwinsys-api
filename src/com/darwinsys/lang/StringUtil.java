package com.darwinsys.lang;

/** Miscellaneous string utilities
 * XX I18N
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

	public static String subst(String oldStr, String newStr, String input) {
		int where = 0;
		StringBuffer sb = new StringBuffer(input);
		while ((where = sb.indexOf(oldStr, where)) != -1) {
			sb.replace(where, where + oldStr.length(), newStr);
		}
		return sb.toString();
	}
}
