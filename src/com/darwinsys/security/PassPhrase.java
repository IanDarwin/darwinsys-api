package com.darwinsys.security;

/** Cheap, lightweight, low-security password generator.
 * See also: java.security.*;
 */
public class PassPhrase {
	/** Minimum length for a decent password */
	public static final int MIN_LENGTH = 10;

	/** The random number generator. */
	protected static final java.util.Random r = new java.util.Random();

	/* Set of characters that is valid. Must be printable, memorable,
	 * and "won't break HTML" (i.e., not '<', '>', '&', '=', ...).
 	 * or break shell commands (i.e., not '<', '>', '$', '!', ...).
	 * I, L and O are good to leave out, as are numeric zero and one.
	 */
	final static char[] goodChar = {
		// Comment out next two lines to make upper-case-only, then
		// use String toUpper() on the user's input before validating.
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
		'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',
		'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'2', '3', '4', '5', '6', '7', '8', '9', 
	};

	/* Generate a Password object with a random password. */
	public static String getNext() {
		return getNext(MIN_LENGTH);
	}
	
	/* Generate a Password object with a random password. */
	public static String getNext(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("Ridiculous password length " + length);
		}
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < length; i++) {
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}
}
