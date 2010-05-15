package com.darwinsys.tel;

import java.util.Locale;

public class DialWords {
	public final static char[][] KB = {
		{'0', '0', '0'},
		{'1', '1', '1'},
		{'a', 'b', 'c'}, // 2
		{'d', 'e', 'f'}, // 3
		{'g', 'h', 'i'}, // 4
		{'j', 'k', 'l'}, // 5
		{'m', 'n', 'o'}, // 6
		{'p', 'r', 's'}, // 7
		{'t', 'u', 'v'}, // 8
		{'w', 'x', 'y', 'z'}, // 9: Z optional
	};

	public final static int[] REV = {
		2, 2, 2,	// a, b, c
		3, 3, 3,	// d, e, f
		4, 4, 4,	// g, h, i
		5, 5, 5,	// j, k, l
		6, 6, 6,	// m, n, o
		7, -1, 7, 7,	// p, q, r, s
		8, 8, 8,	// t, u, v
		9, 9, 9, 9	// w, y, y, z
	};

	/** Convert a name to its numeric equivalent */
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

	/** The harder case, simplified - must be 4-digit number */
	public static String[] numberToName(String number) {
		if (number.length() != 4) {
			throw new IllegalArgumentException(
				"This version can only handle 4-digit numbers");
		}
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) < '0' ||
				number.charAt(i) > '9') {
				throw new IllegalArgumentException(number);
			}
		}
		// Pass 1 - compute how many results, alloc array
		char[] chars = number.toCharArray();
		int nResults = 3 * 3 * 3 * 3;
		char[][] results = new char[nResults][4];
		
		// Pass 2 - do the work
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++) {
						for (int r = 0; r < 3; r++) {
						results[index][0] = KB[chars[i]-'0'][r];
						results[index][1] = KB[chars[j]-'0'][r];
						results[index][2] = KB[chars[k]-'0'][r];
						results[index][3] = KB[chars[l]-'0'][r];
						}
						index++;
					}
				}
			}
		}
		String[] sResults = new String[nResults];
		for (int n = 0; n < nResults; n++) {
			sResults[n] = new String(results[n]);
		}
		return sResults;
	}

}
