package com.darwinsys.lang;

import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

/** A class to implement UNIX-style (single-character) command arguments
 * @author Ian F. Darwin, ian@darwinsys.com
 * Originally patterned after (but not using code from) the UNIX 
 * getopt(3) program, this has been redesigned to be more Java-friendly.
 * <p>
 * This is <em>not</em> threadsafe; it is expected to be used only from main().
 * @version $Id$
 */
public class GetOpt {
	/** The List of File Names found after args */
	protected List fileNameArguments;
	
	/** The set of characters to look for */
	protected char[] argLetters;
	/** The list of which options take values, in same order as argLetters */
	protected boolean[] argTakesValue;
	/** Where we are in the options */
	protected int optind = 0;
	/** Public constant for "no more options" */
	public static final int DONE = 0;
	/** Internal flag - whether we are done all the options */
	protected boolean done = false;

	/** The option argument. */
	protected String optarg;

	/** Retrieve the current option argument */
	public String optarg() {
		return optarg;
	}

	/* Construct a GetOpt object, given the option specifications in arrays. */
	public GetOpt(char[] letters, boolean[] takesValues) {
		if (letters.length != takesValues.length)
			throw new IllegalArgumentException(
				"letters.length != takesValues.length");
		for (int i = 0; i < letters.length; i++) {
			if (letters[i] == ':')
				throw new IllegalArgumentException(
				"Letters array contains : as a letter");
		}
		argLetters = letters;
		argTakesValue = takesValues;
	}

	/* Construct a GetOpt object, storing the set of option characters. */
	public GetOpt(String patt) {
		// First just count the letters
		int n = 0;
		for (int i = 0; i<patt.length(); i++) {
			if (patt.charAt(i) != ':')
				++n;
		}
		if (n == 0)
			throw new IllegalArgumentException(
				"No option letters found in " + patt);
		argLetters = new char[n];
		argTakesValue = new boolean[n];
		for (int i = 0, ix = 0; i<patt.length(); i++) {
			char c = patt.charAt(i);
			argLetters[ix] = c;
			if (i < patt.length() - 1 && patt.charAt(i+1) == ':') {
				argTakesValue[ix] = true;
				++i;
			}
			ix++;
		}
		rewind();
	}

	public void rewind() {
		fileNameArguments = null;
		done = false;
		optind = 0;
	}

	/** 
	 * New, simpler way of using GetOpt. Call this once and get all options.
	 * <p>
	 * This parses the options, returns a Map whose keys are the found options.
	 * and whose values are (null) for non-value options and the found value
	 * for value options. Normally followed by a call to getFileArguments().
	 */
	public Map parseArguments(String[] argv) {
		Map optionsAndValues = new Properties();
		fileNameArguments = new ArrayList();
		for (int i = 0; i < argv.length; i++) {
			char c = getopt(argv);
			if (c != DONE)
				optionsAndValues.put(new Character(c), 
					(optarg != null ? optarg : ""));
			else
				fileNameArguments.add(optarg);
		}
		return optionsAndValues;
	}

	/** Get the list of filename-like arguments after options */
	public List getFilenameArguments() {
		if (fileNameArguments == null) {
			throw new IllegalArgumentException(
				"Illegal call to getFileArguments() before parseOptions()");
		}
		return fileNameArguments;
	}

	/** Return one argument. Call repeatedly until it returns DONE.
	 */
	public char getopt(String argv[]) {
		if (optind == (argv.length)) {
			done = true;
		}

		// If we are (now) finished, bail.
		if (done) {
			return DONE;
		}

		// Pick off the next command line argument, check if it starts "-".
		// If so look it up in the list, else in FileNamesList.
		String thisArg = argv[optind++];
		if (thisArg.startsWith("-")) {
			optarg = null;
			for (int i=0; i<argLetters.length; i++) {
				char c = argLetters[i];
				if (thisArg.charAt(1) == c) {	// we found it
					// If it needs an option argument, get it.
					if (argTakesValue[i]) {
						if (optind < argv.length)
							optarg = argv[optind++]; 
						else throw new IllegalArgumentException(
							"Option " + argLetters[i] +
								" needs value but found end of arg list");
					}
					return c;
				}
			}
			// Still no match, and not used all args, so must be error.
			return '?';
		} else {
			// Found non-argument non-option word in argv: end of options.
			done = true;
			return DONE;
		}
	}

	/** Return optind, the index into args of the last option we looked at */
	public int getOptInd() {
		return optind;
	}
}
