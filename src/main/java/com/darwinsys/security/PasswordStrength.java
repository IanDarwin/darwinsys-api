package com.darwinsys.security;

/**
 * Password Strength Metric, adapted from a JavaScript program at
 * http://www.intelligent-web.co.uk/examples/password.js.html
 * See also http://en.wikipedia.org/wiki/Password_strength and
 * http://cio-asia.com/PrinterFriendly.aspx?articleid=2560&amp;pubid=5&amp;issueid=63
 */
public class PasswordStrength {

	/** Obviously this should be a real dictionary! */
	public static final String[] commonPasswords = { "password", "pass", "1234", "1246", "2468" };

	public static final String DIGITS = "0123456789";

	public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

	public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String PUNCTUATION = "!.@$#*()%~<>{}[]";

	public Strength checkPassword(final String password) {

		int combinations = 0;

		if (containsAny(password, DIGITS)) {
			combinations += 10;
		}

		if (containsAny(password, LOWERCASE)) {
			combinations += 26;
		}

		if (containsAny(password, UPPERCASE)) {
			combinations += 26;
		}

		if (containsAny(password, PUNCTUATION)) {
			combinations += PUNCTUATION.length();
		}

		// work out the total combinations
		int totalCombinations = (int) Math.pow(combinations, password.length());

		// if the password is a common password, then everything changes...
		if (isCommonPassword(password)) {
			return Strength.USELESS;
		}

		// work out how long it would take to crack this (@ 200 attempts per
		// second)
		int timeInSeconds = (totalCombinations / 200) / 2;

		// this is how many days? (there are 86,400 seconds in a day.
		int timeInDays = timeInSeconds / 86400;

		// how long we want it to last
		double lifetime = 365.23;

		// how close is the time to the projected time?
		double ratio = timeInDays / lifetime;

		if (ratio > 1) {
			return Strength.STRONG;
		}

		if (ratio > 0.10) {
			return Strength.WEAK;
		}

		if (ratio <= 0.10) {
			return Strength.USELESS;
		}

		return Strength.MEDIUM;
	}

	private boolean isCommonPassword(final String password) {

		for (int i = 0; i < commonPasswords.length; i++) {
			if (password.equalsIgnoreCase(commonPasswords[i])) {
				return true;
			}
		}
		return false;
	}

	private final boolean containsAny(final String password, final String validChars) {

		for (int i = 0; i < password.length(); i++) {
			char ch = password.charAt(i);
			if (validChars.indexOf(ch) > -1) {
				return true;
			}
		}

		return false;
	}
}
