package com.darwinsys.util;

import java.util.logging.Logger;

/** XXX MERGE WITH c.d.tel/DialWords, which works for simpler cases */
@Deprecated
public class PhoneNumberUtil {
	
	private static char[][] data = {
		{ '0' },		// 0
		{ '1' },		// 1
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
		char[] phNumAsChars = numberStr.toCharArray();
		
		// First pass - how many elements do we need to return?
		int num = 1;
		for (char ch : phNumAsChars) {
			if (isDigit(ch)) {
				num *= data[ch - '0'].length;
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
		char[][] caResults = new char[permutations][phNumAsChars.length];

		// Do the actual work: for each char in original input,
		// put its replacement (or itself) into all the "strings"
		// (char arrays) that will become the final result
		// Work in row major order so each row results in one string.
		
		for (int inputCharNum = 0; inputCharNum < phNumAsChars.length; inputCharNum++) {
			char inputChar = phNumAsChars[inputCharNum];
			char[] datarow = data[inputChar - '0'];
			for (int i = 0; i < permutations; i++) {
				final int n = i%datarow.length;
				char letterForNum = isDigit(inputChar) ? datarow[n] : inputChar;
				caResults[i][inputCharNum] = letterForNum;
			}
		}
		
		// Convert from 2D array of char to array of Strings
		String[] sResults = new String[permutations];
		int ix = 0;
		for (char[] row : caResults) {
			sResults[ix++] = new String(row);
		}
		return sResults;
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
