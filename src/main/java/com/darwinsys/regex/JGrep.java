package com.darwinsys.regex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.darwinsys.lang.GetOpt;
import com.darwinsys.util.Debug;

/** A command-line grep-like program. Accepts some command-line options,
 * and takes a pattern and a list of text files.
 */
public class JGrep {
	/** The pattern we're looking for */
	protected Pattern pattern;
	/** The matcher for this pattern */
	protected Matcher matcher;
	/** The Reader for the current file */
	protected BufferedReader reader;
	/** Are we to only count lines, instead of printing? */
	protected static boolean countOnly = false;
	/** Are we to ignore case? */
	protected static boolean ignoreCase = false;
	/** Are we to suppress printing of filenames? */
	protected static boolean dontPrintFileName = false;
	/** Are we to only list names of files that match? */
	protected static boolean listOnly = false;
	/** are we to print line numbers? */
	protected static boolean numbered = false;
	/** Are we to be silent about errors? */
	protected static boolean silent = false;
	/** are we to print only lines that DONT match? */
	protected static boolean inVert = false;
	/** Are we to process arguments recursively if directories? */
	protected static boolean recursive = false;

	/** Construct a Grep object for each pattern, and run it
	 * on all input files listed in argv.
	 * Be aware that a few of the command-line options are not
	 * acted upon in this version - this is an exercise for the reader!
	 */
	public static void main(String[] argv) {

		if (argv.length < 1) {
		    System.err.println(
			"Usage: JGrep pattern [-chilrsnv][-f pattfile][filename...]");
		    System.exit(1);
		}
		String patt = null;

		GetOpt go = new GetOpt("cf:hilnrRsv");

		char c;
		while ((c = go.getopt(argv)) != 0) {
			switch(c) {
				case 'c':
					countOnly = true;
					break;
				case 'f':
					BufferedReader b = null;
					try {
						b = new BufferedReader(new FileReader(go.optarg()));
						patt = b.readLine();
					} catch (IOException e) {
						System.err.println("Can't read pattern file " + go.optarg());
						System.exit(1);
					} finally {
						if (b != null)
							try {
								b.close();
							} catch (IOException e) {
								/*CANTHAPPEN*/
							}
					}
					break;
				case 'h':
					dontPrintFileName = true;
					break;
				case 'i':
					ignoreCase = true;
					break;
				case 'l':
					listOnly = true;
					break;
				case 'n':
					numbered = true;
					break;
				case 'r':
				case 'R':
					recursive = true;
					break;
				case 's':
					silent = true;
					break;
				case 'v':
					inVert = true;
					break;
			}
		}

		int ix = go.getOptInd();

		if (patt == null)
			patt = argv[ix++];

		JGrep prog = null;
		try {
			prog = new JGrep(patt);
		} catch (PatternSyntaxException ex) {
			System.err.println("RE Syntax error in " + patt);
			return;
		}

		if (argv.length == ix) {
			if (recursive) {
				System.err.println("Warning: recursive search of stdin!");
			}
			dontPrintFileName = true; // Don't print if stdin
			prog.process(new InputStreamReader(System.in), null);
		} else {
			if (!dontPrintFileName)
				dontPrintFileName = ix == argv.length - 1; // Nor if only one file.
			for (int i=ix; i<argv.length; i++) { // note starting index
				try {
					prog.process(new File(argv[i]));
				} catch(Exception e) {
					System.err.println(e);
				}
			}
		}
	}

	/** Construct a JGrep object.
	 * @param patt The pattern to look for
	 * @param args the command-line options.
	 */
	public JGrep(String patt) throws PatternSyntaxException {
		Debug.printf("JGrep.JGrep(%s)%n", patt);
		// compile the regular expression
		int caseMode = ignoreCase ?
			Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE :
			0;
		pattern = Pattern.compile(patt, caseMode);
		matcher = pattern.matcher("");
	}

	/** Process one command line argument (file or directory)
	 * @throws FileNotFoundException 
	 */
	public void process(File file) throws FileNotFoundException {
		if (file.isFile()) {
			process(new BufferedReader(new FileReader(file)), file.getName());
			return;
		}
		if (file.isDirectory()) {
			for (File nf : file.listFiles()) {
				process(nf);
			}
			return;
		}
		System.err.println(
			"ERROR: neither file nor directory: " + file.getAbsolutePath());
	}

	/** Do the work of scanning one file
	 * XXX change to take a File object, so -R can use listFiles
	 * @param	ifile	Reader	Reader object already open
	 * @param	fileName String	Name of the input file
	 */
	public void process(Reader ifile, String fileName) {

		String inputLine;
		int matches = 0;

		if (!dontPrintFileName) {
			File f = new File(fileName);
			if (f.isDirectory()) {
				// We could warn, but better not natter by default...
				return;
			}
		}

		try {
			reader = new BufferedReader(ifile);

			while ((inputLine = reader.readLine()) != null) {
				matcher.reset(inputLine);
				if (matcher.find()) {
					if (countOnly)
						matches++;
					else {
					if (!dontPrintFileName)
						System.out.print(fileName + ": ");
					System.out.println(inputLine);
					}
				} else if (inVert) {
					System.out.println(inputLine);
				}
			}
			if (countOnly)
				System.out.println(matches + " matches in " + fileName);
			reader.close();
		} catch (IOException e) { System.err.println(e); }
	}
}
