package com.darwinsys.util;

/** A class to implement UNIX-style (single-character) command arguments
 * @author Ian F. Darwin, ian@darwinsys.com
 * based on the standard UNIX getopt(3) program.
 * @version $Id$
 */
public class GetOpt {
	/** The set of characters to look for */
	protected String pattern;
	/** Where we are in the options */
	protected int optind = 0;
	/** Retreive the option index */
	public int optind() {
		return optind;
	}
	/** The option argument, if there is one. */
	protected String optarg;
	/** Retreive the option argument */
	public String optarg() {
		return optarg;
	}
	/** Whether we are done all the options */
	protected boolean done = false;

	/* Construct a GetOpt object, storing the set of option characters. */
	public GetOpt(String patt) {
		pattern = patt;
		optind = 0;
		rewind();
	}

	public void rewind() {
		done = false;
	}

	/** Return one argument.
	 */
	public char getopt(String argv[]) {
		if (optind == (argv.length))
			done = true;
		if (done)
			return 0;
		String thisArg = argv[optind++];
		optarg = null;

		for (int i=0; i<pattern.length(); i++) {
			char c = pattern.charAt(i);
			if (thisArg.startsWith("-")) {
				if (thisArg.equals("-"+c)) {
					if (i+1 < pattern.length() && pattern.charAt((i++)+1)==':')
						optarg = argv[optind++]; 
					return c;
				}
			} else {
				done = true;
				return 0;
			}
		}
		// Still no match, and not used all args, so must be error.
		return '?';
	}

}
