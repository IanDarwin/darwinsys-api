package regress;

import com.darwinsys.util.GetOpt;

/** Some test cases for GetOpt.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class GetOptTest {

	public static void main(String[] args) {
		process(goodArgChars, goodArgs);
		process(badArgChars, goodArgs);
		process(badArgChars, badArgs);
	}

	static String goodArgChars = "o:h";
	static String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	static 	String badArgChars = "f1o";
	static String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};

	/** Private function, for testing. */
	private static void process(String argChars, String[] args) {

		System.out.println("** START ** " + argChars + '(' + args.length + ')');

		GetOpt go = new GetOpt(argChars);

		char c;
		int errs = 0;

		while ((c =go.getopt(args)) != 0) {
			if (c == '?') {
				System.out.print("Bad option");
				++errs;
			} else {
				System.out.print("Found " + c);
				if (go.optarg() != null)
					System.out.print("; Option " + go.optarg());
			}
			System.out.println();
		}

		// Process any filename-like arguments.
		for (int i=go.getOptInd(); i<args.length; i++)
			System.out.println("Filename-like arg " + args[i]);

		if (errs != 0) {
			System.out.println("At least one user error found");
		}
	}
}
