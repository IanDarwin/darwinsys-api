/** Some test cases for GetOpt.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class GetOptTest {
	public static void main(String argv[]) {
		String goodArgChars = "o:h", goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
		};
		String badArgChars = "f1o", badArgs[]  = {
			"-h", "-o", "outfile", "infile"
		};
		process(goodArgChars, goodArgs);
		process(badArgChars, goodArgs);
		process(badArgChars, badArgs);
	}

	/** Private function, for testing. */
	private static void process(String argChars, String[] args) {

		System.out.println("** START ** " + argChars + '(' + args.length + ')');

		GetOpt go = new GetOpt(argChars);

		char c;
		while ((c =go.getopt(args)) != 0) {
			System.out.print("Found " + c);
			if (go.optarg() != null)
				System.out.print("; Option " + go.optarg());
			System.out.println();
		}
		for (int i=go.optind(); i<args.length; i++)
			System.out.println("Filename-like arg " + args[i]);
	}
}
