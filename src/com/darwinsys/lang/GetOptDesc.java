package com.darwinsys.lang;

/** A GetOptDesc describes one argument that may be accepted by the program.
 */
public class GetOptDesc {
	/** The short-form option letter */
	protected char argLetter;
	/** The long-form option name */
	protected String argName;
	/** True if this option needs an argument after it */
	protected boolean takesArgument;

	/** Construct a GetOpt option.
	 * @param ch The single-character name for this option.
	 * @param nm The word name for this option.
	 * @param ta True if this option requires an argument after it.
	 */
	public GetOptDesc(char ch, String nm, boolean ta) {
		if (!Character.isLetter(ch) && !Character.isDigit(ch)) {
			throw new IllegalArgumentException(ch + ": not letter or digit");
		}
		argLetter = ch;
		argName   = nm;	// may be null, meaning no long name.
		takesArgument = ta;
	}
}
