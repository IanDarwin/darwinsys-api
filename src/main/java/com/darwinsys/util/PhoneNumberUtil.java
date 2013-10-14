package com.darwinsys.util;

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
		int permutations = 1;
		for (char ch : chars) {
			int n = ch - '0';
			if (isDigit(ch)) {
				permutations *= Math.max(1, data[n].length);
			} else if (isPlain(ch)) {
				// nothing
			} else {
				throw new IllegalArgumentException(
					String.format("Invalid character %c in number string", ch));
			}
		}

		// Now we know the number of permutations
		String[] result = new String[permutations];

		// Do the actual work.
		int sIndex = 0;
		for (char ch : chars) {
			if (isDigit(ch)) {
				int n = ch - '0';
				for (char x : data[n]) {
					// XXX I got interrupted here...
				}	
			} else {
				// XXX 
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
