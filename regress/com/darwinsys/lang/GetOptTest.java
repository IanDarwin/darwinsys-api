import com.darwinsys.util.GetOpt;
import junit.framework.*;

/** Some test cases for GetOpt.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class GetOptTest extends TestCase {

	/** JUnit test classes require this constructor */
	public GetOptTest(String name) {
		super(name);
	}

	public void testgoodArgChars() {
		process(goodArgChars, goodArgs);
	}
	public void testBadArgChars() {
		process(badArgChars, goodArgs);
	}
	public void testBadArgsAndChars() {
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
