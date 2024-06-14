package com.darwinsys.tel;

import java.util.Locale;
import java.util.logging.Logger;

/**
 * Convert phone number to name and vice versa.
 * Maybe do something better with '1'?
 */
public class DialWords {
	
	private static final char[][] data = {
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

	public final static int[] REV = {
			2, 2, 2,        // a, b, c
			3, 3, 3,        // d, e, f
			4, 4, 4,        // g, h, i
			5, 5, 5,        // j, k, l
			6, 6, 6,        // m, n, o
			7, -1, 7, 7,    // p, q, r, s
			8, 8, 8,        // t, u, v
			9, 9, 9, 9      // w, y, y, z
	};
	
	static Logger logger = Logger.getLogger(DialWords.class.getName());

	public static String nameToNumber(String name) {
		StringBuilder s = new StringBuilder();
		for (char c : name.toLowerCase(Locale.ENGLISH).toCharArray()) {
			if (Character.isLetter(c)) {
				int index = c - 'a';
				if (index < 0 || index > REV.length) {
					continue;
				}
				int i = REV[index];
				if (i > 0) {
					s.append((char)(i+'0'));
				}
			}
		}
		return s.toString();
	}


	public static String[] numberToName(String numberStr) {
		
		if (numberStr == null) {
			throw new NullPointerException();
		}
		if (numberStr.length() == 0) {
			throw new IllegalArgumentException("empty string");
		}
		
		// Get array of chars to simplify code
		char[] phNumAsChars = digitsOnly(numberStr).toCharArray();
		
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

	private static String digitsOnly(String numberStr) {
		var sb = new StringBuilder();
		for (var ch : numberStr.toCharArray()) {
			if (isDigit(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/** The only non-digit chars that are allowed. */
	private static boolean isPlain(char ch) {
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
	private static boolean isDigit(char ch) {
		switch(ch) {
		case '1': case '2': case '3':
		case '4': case '5': case '6':
		case '7': case '8': case '9':
			return true;
		}
		return false;
	}

	/** Backwards compatibility */
	public static String[] mnemonics(String number) {
		return numberToName(number);
	}
}
