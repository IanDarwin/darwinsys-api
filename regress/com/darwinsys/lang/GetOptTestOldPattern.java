package regress;

import java.util.*;

import com.darwinsys.lang.GetOpt;

/** Some test cases for GetOpt.
 * <br/>XXX REWRITE USING JUNIT
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class GetOptTest {

	public static void main(String[] args) {
		process(goodArgChars, goodArgs, false);
		process(badArgChars, goodArgs, true);
		process(badArgChars, badArgs, true);
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
	private static void process(
		String argChars, String[] args, boolean shouldFail) {

		// System.out.println("** START ** " + argChars);

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

		// System.out.println("** START NEW WAY ** " + argChars);
		GetOpt go2 = new GetOpt(argChars);
		Map m = go2.parseArguments(args);
		if (m.size() == 0)
			System.out.println("NO ARGS MATCHED");
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			c = ((Character)key).charValue();
			System.out.print("Found " + c);
			if (c == '?')
				errs++;
			String val = (String)m.get(key);
			if (!val.equals(""))
				System.out.print("; Option " + val);
			else
				System.out.print("; (no option)");
			System.out.println();
		}

		List filenames = go2.getFilenameArguments();
		for (int i = 0; i < filenames.size(); i++) {
			System.out.println("Filename-like arg " + filenames.get(i));
		}

		if (shouldFail) {
			if (errs != 0)
				System.out.println("Expected error(s) found");
			else
				System.out.println("** FAILURE ** Expected errors not found");
		} else {
			if (errs == 0)
				System.out.println("Expected error(s) found");
			else
				System.out.println("** FAILURE ** Expected errors not found");
		}
	}
}
