package 

import java.util.*;

import junit.framework.*;

import com.darwinsys.lang.GetOpt;
import com.darwinsys.lang.GetOptDesc;

/** Some test cases for GetOpt.
 * <br/>XXX TODO - compare with expected 'c' values.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class GetOptTest extends TestCase {

	private String goodArgChars = "o:h";
	private String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	private String goodLongArgs[]  = {
			"-help", "-output-file", "outfile", "infile"
	};

	private 	String badArgChars = "f1o";
	private String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};

	private GetOptDesc[] options = {
		new GetOptDesc('o', "output-file", true),
		new GetOptDesc('h', "help", false),
	};

	public void testBadArgChar() {
		String bad = "abc@";
		try {
			new GetOpt(bad);
			fail("GetOpt(" + bad + ") did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
	}
	public void testOldwayGood() {
		process1(goodArgChars, goodArgs, false);
		process2(goodArgChars, goodArgs, false);
	}
	public void testOldwayBadCharsGoodArgs() {
		process1(badArgChars, goodArgs, true);
		process2(badArgChars, goodArgs, true);
	}
	public void testOldwayBadCharsBadArgs() {
		process1(badArgChars, badArgs, true);
		process2(badArgChars, badArgs, true);
	}

	public void testNewWayShort() {
		GetOpt go = new GetOpt(options);
		Map map = go.parseArguments(goodArgs);
		newWayInner(go, map);
	}
	public void testNewWayLong() {
		GetOpt go = new GetOpt(options);
		Map map = go.parseArguments(goodLongArgs);
		newWayInner(go, map);
	}

	protected void newWayInner(GetOpt go, Map map) {
		assertFalse(map.size() == 0);
		if (map.size() == 0) {
			throw new IllegalArgumentException(
				"Unexpected empty map");
		}
		int errs = 0;
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			char c = key.charAt(0);
			String val = (String)map.get(key);
			switch(c) {
				case '?':
					errs++; break;
				case 'o': assertEquals(val, "outfile"); break;
				case 'f':
				case 'h':
				case '1':
					 assertEquals(val, null);
					break;
				default:
					throw new IllegalArgumentException(
						"Unexpected c value " + c);
			}
		}
		assertEquals(1, go.getFilenameList().size());
		assertEquals("infile", go.getFilenameList().get(0));
	}

	void process1(String argChars, String[] args, boolean shouldFail) {

		System.out.println("** START ** " + argChars);

		GetOpt go = new GetOpt(argChars);

		int errs = 0, ix = 0;

		char c;
		while ((c = go.getopt(args)) != 0) {
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
		for (int i=go.getOptInd(); i<args.length; i++) {
			System.out.println("Filename-like arg " + args[i]);
		}
	}

	void process2(String argChars, String[] args, boolean shouldFail) {
		int errs = 0;

		System.out.println("** START NEW WAY ** " + argChars);
		GetOpt go2 = new GetOpt(argChars);
		Map m = go2.parseArguments(args);
		if (m.size() == 0)
			System.out.println("NO ARGS MATCHED");
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			char c = ((String)key).charAt(0);
			System.out.print("Found " + c);
			if (c == '?')
				errs++;
			String val = (String)m.get(key);
			if (val == null || val.equals(""))
				System.out.print("; (no option)");
			else
				System.out.print("; Option " + val);
			System.out.println();
		}

		List filenames = go2.getFilenameList();
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

	public void testConstructorArgs() {
		try {
			String bad = null;
			new GetOpt(bad);
			fail("GetOpt(null) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
		try {
			new GetOpt("::");
			fail("GetOpt(::) did not throw expected exception");
		} catch (IllegalArgumentException ex) {
			System.err.println("Caught expected exception " + ex);
		}
		new GetOpt("f:c:");		// this failed at one point - multiple : args
		new GetOpt("foo");		// multiple occurrences of same letter - ok?
	}
}
