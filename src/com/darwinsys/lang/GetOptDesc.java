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

	public GetOptDesc(char c, String n, boolean b) {
		argLetter = c;
		argName   = n;
		takesArgument = b;
	}
}
