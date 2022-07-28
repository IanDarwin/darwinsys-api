package com.darwinsys.lang;

/** A GetOptDesc describes one argument that may be accepted by the program.
 * @param argLetter The single-character name for this option.
 * @param argName The word name for this option.
 * @param takesArgument True if this option requires an argument after it.
 */
public record GetOptDesc(
	/** The short-form option letter */
	char argLetter,
	/** The long-form option name */
	String argName,
	/** True if this option needs an argument after it */
	boolean takesArgument) {

	/** Construct a GetOpt option.
	 */
	public GetOptDesc {
		if (!Character.isLetter(argLetter) && !Character.isDigit(argLetter)) {
			throw new IllegalArgumentException(argLetter + ": not letter or digit");
		}
	}

	// These getters are only necessary for backwards compatibility;
	// Java's "record" type provides accessors with the exact name of
	// each field, e., artLetter()

	public char getArgLetter() {
		return argLetter;
	}

	public String getArgName() {
		return argName;
	}

	public boolean takesArgument() {
		return takesArgument;
	}
}
