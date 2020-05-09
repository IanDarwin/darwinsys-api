package com.darwinsys.util;

import java.util.logging.Logger;

/** XXX MERGE WITH c.d.tel/DialWords, which works for simpler cases */
@Deprecated
public class PhoneNumberUtil {
	
	private static char[][] data = {
		{ },		// 0
		{ },		// 1
		{ 'a', 'b', 'c' }, // 2
		{ 'd', 'e', 'f' }, // 3
		{ 'g', 'h', 'i' }, // 4
		{ 'j', 'k', 'l' }, // 5
		{ 'm', 'n', 'o' }, // 6
		{ 'p', 'q', 'r', 's' }, // 7
		{ 't', 'u', 'v' }, // 8
		{ 'w', 'x', 'y', 'z' }, // 9
	};
	
	static Logger logger = Logger.getLogger(PhoneNumberUtil.class.getName());
	
	public static String[] mnemonics(String numberStr) {
		
		if (numberStr == null) {
			throw new NullPointerException();
		}
		if (numberStr.length() == 0) {
			throw new IllegalArgumentException("empty string");
		}
		
		// Get array of chars to simplify code
		char[] chars = numberStr.toCharArray();
		
		// First pass - how many elements do we need to return?
		int num = 1;
		for (char ch : chars) {
			int n = ch - '0';
			if (isDigit(ch)) {
				num *= Math.max(1, data[n].length);
			} else if (isPlain(ch)) {
				// nothing
			} else {
				throw new IllegalArgumentException(
					String.format("Invalid character %c in number string", ch));
			}
		}
		final int permutations = num;
		logger.info(() -> {
			return String.format(
				"For input string %s, computed %d permutations",
				numberStr, permutations);
		});
		
		// Now we know the number of permutations
		String[] result = new String[permutations];

		// Do the actual work.
		for (char ch : chars) {
			if (isDigit(ch)) {
				int n = ch - '0';
				for (char x : data[n]) {
					// XXX I got interrupted here...
				}	
			} else {
				// Do nothing
			}
		}
		
		return result;
	}
	
	/** The only non-digit chars that are allowed. */
	static boolean isPlain(char ch) {
		switch(ch) {
		case '0': case '1': // don't have letters
		case '+': case '-': // int'l prefix, dash
		case '(': case ')': // silly North Americans
		case '*': case '#': // just in case (should not occur, but...)
			return true;
		}
		return false;
	}
	
	/**
	 * Reimplement Character.isDigit to be unaffected by the user's locale.
	 * @param ch The char that might be a digit
	 * @return True iff ch is a telephone digit.
	 */
	static boolean isDigit(char ch) {
		switch(ch) {
		case '1': case '2': case '3':
		case '4': case '5': case '6':
		case '7': case '8': case '9':
			return true;
		}
		return false;
	}

}
