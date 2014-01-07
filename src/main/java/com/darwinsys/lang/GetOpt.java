package com.darwinsys.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.darwinsys.util.Debug;

/** GetOpt implements UNIX-style (single-character) command line argument
 * parsing. Originally patterned after (but not using code from) the UNIX 
 * getopt(3) program, this has been redesigned to be more Java-friendly.
 * As a result, there are two ways of using it, referred to as
 * "the Unix way" and "the Java way".
 * <ol><li>Original (UNIX) model:
 * <pre>
 *      GetOpt go = new GetOpt("hno:");
 *      boolean numeric_option = false;
 *      String outFileName = "(standard output)";
 *      char c;
 *      while ((c = go.getopt(args)) != GetOpt.DONE) {
 *          switch(c) {
 *          case 'h':
 *              doHelp(0);
 *              break;
 *          case 'n':
 *              numeric_option = true;
 *              break;
 *          case 'o':
 *              outFileName = go.optarg();
 *              break;
 *          default:
 *              System.err.println("Unknown option character " + c);
 *              doHelp(1);
 *          }
 *      }
 *      System.out.print("Options: ");
 *      System.out.print("Numeric: " + numeric_option + ' ');
 *      System.out.print("Output: " + outFileName + "; ");
 *      System.out.print("Inputs: ");
 *      if (go.getOptInd() == args.length) {
 *          doFile("(standard input)");
 *      } else for (int i = go.getOptInd(); i &lt; args.length; i++) {
 *          doFile(args[i]);
 *      }
 * </pre></li>
 * <li>Newer (Java) model, which allows long-named options (preceded with
 * a single dash e.g., "-output-file /tmp/j":
 * <pre>
 *      boolean numeric_option = false;
 *      boolean errs = false;
 *      String outputFileName = null;
 *
 *      GetOptDesc options[] = {
 *          new GetOptDesc('n', "numeric", false),
 *          new GetOptDesc('o', "output-file", true),
 *      };
 *      GetOpt parser = new GetOpt(options);
 *      Map&lt;String,String&gt; optionsFound = parser.parseArguments(argv);
 *      Iterator&lt;String&gt; it = optionsFound.keySet().iterator();
 *      while (it.hasNext()) {
 *          String key = (String)it.next();
 *          switch (key.charAt(0)) {
 *              case 'n':
 *                  numeric_option = true;
 *                  break;
 *              case 'o':
 *                  outputFileName = optionsFound.get(key);
 *                  break;
 *              case '?':
 *                  errs = true;
 *                  break;
 *              default:
 *                  throw new IllegalStateException(
 *                  "Unexpected option character: " + key);
 *          }
 *      }
 *      if (errs) {
 *          System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
 *      }
 *      System.out.print("Options: ");
 *      System.out.print("Numeric: " + numeric_option + ' ');
 *      System.out.print("Output: " + outputFileName + "; ");
 *      System.out.print("Input files: ");
 *      List&lt;Files&gt; files = parser.getFilenameList();
 *      for (String file : files) {
 *          System.out.print(file);
 *          System.out.print(' ');
 *      }
 *      System.out.println();
 * }
 * </pre></li>
 * </ol>
 * <p>
 * This class is <em>not</em> threadsafe; it is expected to be used only from main().
 * <p>
 * For another way of dealing with command lines, see the
 * <a href="http://jakarta.apache.org/commons/cli/">Jakarta Commons
 * Command Line Interface</a>.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
// BEGIN main
// package com.darwinsys.lang;
public class GetOpt {
	/** The List of File Names found after args */
	protected List<String> fileNameArguments;
	/** The set of characters to look for */
	protected final GetOptDesc[] options;
	/** Where we are in the options */
	protected int optind = 0;
	/** Public constant for "no more options" */
	public static final int DONE = 0;
	/** Internal flag - whether we are done all the options */
	protected boolean done = false;
	/** The current option argument. */
	protected String optarg;

	/** Retrieve the current option argument; UNIX variant spelling. */
	public String optarg() {
		return optarg;
	}
	/** Retrieve the current option argument; Java variant spelling. */
	public String optArg() {
		return optarg;
	}

	/** Construct a GetOpt parser, given the option specifications
	 * in an array of GetOptDesc objects. This is the preferred constructor.
	 */
	public GetOpt(final GetOptDesc[] opt) {
		this.options = opt.clone();
	}

	/** Construct a GetOpt parser, storing the set of option characters.
	 * This is a legacy constructor for backwards compatibility.
	 * That said, it is easier to use if you don't need long-name options,
	 * so it has not been and will not be marked "deprecated".
	 */
	public GetOpt(final String patt) {
		if (patt == null) {
			throw new IllegalArgumentException("Pattern may not be null");
		}
		if (patt.charAt(0) == ':') {
			throw new IllegalArgumentException(
				"Pattern incorrect, may not begin with ':'");
		}

		// Pass One: just count the option letters in the pattern
		int n = 0;
		for (int i = 0; i<patt.length(); i++) {
			if (patt.charAt(i) != ':')
				++n;
		}
		if (n == 0) {
			throw new IllegalArgumentException(
				"No option letters found in " + patt);
		}

		// Pass Two: construct an array of GetOptDesc opjects.
		options = new GetOptDesc[n];
		for (int i = 0, ix = 0; i<patt.length(); i++) {
			final char c = patt.charAt(i);
			boolean argTakesValue = false;
			if (i < patt.length() - 1 && patt.charAt(i+1) == ':') {
				argTakesValue = true;
				++i;
			}
			Debug.println("getopt",
				"CONSTR: options[" + ix + "] = " + c + ", " + argTakesValue);
			options[ix++] = new GetOptDesc(c, null, argTakesValue);
		}
	}

	/** Reset this GetOpt parser */
	public void rewind() {
		fileNameArguments = null;
		done = false;
		optind = 0;
		optarg = null;
	}

	/** 
	 * Modern way of using GetOpt: call this once and get all options.
	 * <p>
	 * This parses the options, returns a Map whose keys are the found options.
	 * Normally followed by a call to getFilenameList().
	 * <br>Side effect: sets "fileNameArguments" to a new List
	 * @return a Map whose keys are Strings of length 1 (containing the char
	 * from the option that was matched) and whose value is a String
	 * containing the value, or null for a non-option argument.
	 */
	public Map<String,String> parseArguments(String[] argv) {
		Map<String, String> optionsValueMap = new HashMap<String, String>();
		fileNameArguments = new ArrayList<String>();
		for (int i = 0; i < argv.length; i++) {	// Can not use foreach, need i
			Debug.println("getopt", "parseArg: i=" + i + ": arg " + argv[i]);
			char c = getopt(argv);	// sets global "optarg"
			if (c == DONE) {
				fileNameArguments.add(argv[i]);
			} else {
				optionsValueMap.put(Character.toString(c), optarg);
				// If this arg takes an option, must arrange here to skip it.
				if (optarg != null) {
					i++;
				}
			}
		}
		return optionsValueMap;
	}

	/** Get the list of filename-like arguments after options;
	 * only for use if you called parseArguments.
	 */
	public List<String> getFilenameList() {
		if (fileNameArguments == null) {
			throw new IllegalArgumentException(
				"Illegal call to getFilenameList() before parseOptions()");
		}
		return fileNameArguments;
	}

	/** The true heart of getopt, whether used old way or new way:
	 * returns one argument; call repeatedly until it returns DONE.
	 * Side-effect: sets globals optarg, optind
	 */
	public char getopt(String argv[]) {
		Debug.println("getopt",
			"optind=" + optind + ", argv.length="+argv.length);

		if (optind >= (argv.length) || !argv[optind].startsWith("-")) {
			done = true;
		}

		// If we are finished (either now OR from before), bail.
		// Do not collapse this into the "if" above
		if (done) {
			return DONE;
		}
		
		optarg = null;

		// XXX TODO - two-pass, 1st check long args, 2nd check for
		// char, to allow advanced usage like "-no outfile" == "-n -o outfile".

		// Pick off next command line argument, if it starts "-",
		// then look it up in the list of valid args.
		String thisArg = argv[optind];

		if (thisArg.startsWith("-")) {
			for (GetOptDesc option : options) {
				if ((thisArg.length() == 2 &&
						option.getArgLetter() == thisArg.charAt(1)) ||
				   (option.getArgName() != null &&
					option.getArgName().equals(thisArg.substring(1)))) { // found it
					// If it needs an option argument, get it.
					if (option.takesArgument()) {
						if (optind < argv.length-1) {
							optarg = argv[++optind]; 							
						} else {
							throw new IllegalArgumentException(
								"Option " + option.getArgLetter() +
								" needs value but found end of arg list");
						}
					}
					++optind;
					return option.getArgLetter();
				}
			}
			// Began with "-" but not matched, so must be error.
			++optind;
			return '?';
		} else {
			// Found non-argument non-option word in argv: end of options.
			++optind;
			done = true;
			return DONE;
		}
		

		
	}

	/** Return optind, the index into args of the last option we looked at */
	public int getOptInd() {
		return optind;
	}

}
// END main
