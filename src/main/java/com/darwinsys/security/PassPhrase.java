package com.darwinsys.security;

/** Cheap, lightweight, low-security password generator.
 * See also: java.security.*;
 */
public class PassPhrase {

	/** Minimum length for a decent password */
	public static final int MIN_LENGTH = 10;

	/** The random number generator. */
	protected static final java.util.Random r = new java.util.Random();

	/* Set of characters that is valid. Must be printable, memorable.
	 * I, small-L and O are good to leave out, as are numeric zero and one.
	 */
	final static char[] lowercaseChar = {
		// Comment out next two lines to make upper-case-only, then
		// use String toUpper() on the user's input before validating.
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
		'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	}, uppercaseChar = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
		'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	},
		digitsChar = {
		'2', '3', '4', '5', '6', '7', '8', '9',
	}, specialChar = {
		// A small set of likely-passable, hard-to-confuse characters
		// i.e., don't use more than one of [=~-]
		'@', '#', '=', '%', '+', '*', ',', '/', ';', '[', ']',
	};


	/* Generate a Password object with a random password. */
	public static String getNext() {
		return getNext(MIN_LENGTH);
	}

	/* Generate a Password object with a random password. */
	public static String getNext(int length) {
		return getNext(length, true, true, true);
	}

	/** Generate a random password, compatibility mode.
	 * @param length How long the passwd is to be
	 * @param lowercase true if you want lowercase letters included
	 * @param uppercase true if you want uppercase letters included
	 * @param digits True if you want numeric digits included
	 * @return A generated password
	 */
	public static String getNext(int length,
			boolean lowercase, boolean uppercase, boolean digits) {
		return getNext(length, lowercase, uppercase, digits, true);
	}

	/* Generate a random password. */
	public static String getNext(int length,
			boolean lowercase, boolean uppercase, boolean digits, boolean special) {
		if (length < 1) {
			throw new IllegalArgumentException("Ridiculous password length " + length);
		}
		int charsLength =
				(lowercase ? lowercaseChar.length : 0) +
				(uppercase ? uppercaseChar.length : 0) +
				(digits ? digitsChar.length : 0) +
				(special ? specialChar.length : 0);
		char[] goodChar = new char[charsLength];
		int n = 0;
		if (lowercase) {
			System.arraycopy(lowercaseChar, 0, goodChar, 0, lowercaseChar.length);
			n+=lowercaseChar.length;
		}
		if (uppercase) {
			System.arraycopy(uppercaseChar, 0, goodChar, n, uppercaseChar.length);
			n+=uppercaseChar.length;
		}
		if (digits) {
			System.arraycopy(digitsChar, 0, goodChar, n, digitsChar.length);
			n+=digitsChar.length;
		}
		if (special) {
			System.arraycopy(specialChar, 0, goodChar, n, specialChar.length);
			n+=specialChar.length;
		}
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < length; i++) {
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}

	/**
	 * Print one password of the default length, or one for each of the given length(s)
	 * @param args The args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(getNext(MIN_LENGTH));
		} else {
			for (String arg : args) {
				System.out.println(getNext(Integer.parseInt(arg)));
			}
		}
	}
}
