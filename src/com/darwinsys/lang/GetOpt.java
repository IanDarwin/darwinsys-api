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
	/** Public constant for "no more options"
	 * XXX should switch to hasNext()/next() pattern.
	 */
	public static final int DONE = 0;
	/** Internal flag - whether we are done all the options */
	protected boolean done = false;

	/** Retrieve the option index */
	public int getOptInd() {
		return optind;
	}

	/** The option argument, if there is one. */
	protected String optarg;

	/** Retrieve the current option argument */
	public String optarg() {
		return optarg;
	}

	/* Construct a GetOpt object, storing the set of option characters. */
	public GetOpt(String patt) {
		pattern = patt;
		rewind();
	}

	public void rewind() {
		done = false;
		optind = 0;
	}

	/** Return one argument.
	 */
	public char getopt(String argv[]) {
		if (optind == (argv.length)) {
			done = true;
		}

		// Do not combine with previous if statement.
		if (done) {
			return DONE;
		}

		// Pick off the next command line argument, check if it starts "-".
		// If so look it up in the list.
		String thisArg = argv[optind++];
		if (thisArg.startsWith("-")) {
			optarg = null;
			for (int i=0; i<pattern.length(); i++) {
				char c = pattern.charAt(i);
				if (thisArg.equals("-"+c)) {	// we found it
					// If it needs an option argument, get it.
					if (i+1 < pattern.length() && 
						pattern.charAt(i+1)==':' &&
						optind < argv.length)
						optarg = argv[optind++]; 
					return c;
				}
			}
			// Still no match, and not used all args, so must be error.
			return '?';
		} else {
			// Found non-argument non-option word in argv: end of options.
			optind--;
			done = true;
			return DONE;
		}
	}
}
