package com.darwinsys.util;

/* Miscellaneous string utilities
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

	public static void main(String[] args) {
		String[] list = { "apples", "oranges", "pumpkins", "bananas" };
		System.out.println(arrayToCommaList(list));
	}
}
