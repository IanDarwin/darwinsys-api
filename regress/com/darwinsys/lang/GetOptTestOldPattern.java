package com.darwinsys.lang;

import com.darwinsys.util.Debug;

import junit.framework.TestCase;

/** Some test cases for GetOpt using the "traditional" coding pattern.
* @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class GetOptTest/*XXXOldPattern*/ extends TestCase {

	@Override
	protected void setUp() throws Exception {
		System.out.println("GetOptTest.setUp()");
		// System.setProperty("debug.getopt", "sure");
	}
	
	private String goodArgChars = "o:h";
	private String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	private 	String badArgChars = "f1o";
	private String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};

	public void testOldwayGood() {
		processUnixWay(goodArgChars, goodArgs, false);
	}
	public void testOldwayBadCharsGoodArgs() {
		processUnixWay(badArgChars, goodArgs, true);
	}
	public void testOldwayBadCharsBadArgs() {
		processUnixWay(badArgChars, badArgs, true);
	}
	
	void processUnixWay(String argChars, String[] args, boolean shouldFail) {

		System.out.println("** START ** Getopt(" + argChars + ")");

		GetOpt getopt = new GetOpt(argChars);

		int errs = 0;

		char c;
		while ((c = getopt.getopt(args)) != 0) {
			if (c == '?') {
				System.out.print("Bad option");
				++errs;
			} else {
				System.out.print("Found " + c);
				if (getopt.optarg() != null)
					System.out.print("; Option " + getopt.optarg());
			}
			System.out.println();
		}

		// Process any filename-like arguments.
		assertTrue(args.length >= getopt.getOptInd());
		for (int i = getopt.getOptInd(); i<args.length; i++) {
			Debug.printf("getopt", "%d %s%n", i, args[i]);
			String fileName = args[i];
			System.out.println("Filename-like arg " + fileName);
			assertFalse(fileName.startsWith("-"));
		}
	}
	
	/** Make sure that all args get parsed, and that optarg is 
	 * null when it should be
	 */
	public void testAllArgsDone() {
		GetOpt getopt = new GetOpt("tn:");
		String[] args = { "-n", "100", "-t" };
		assertTrue('n' == getopt.getopt(args));
		assertEquals("100", getopt.optarg);
		assertEquals((int)'t', (int)getopt.getopt(args));
		assertNull(getopt.optarg);
	}
	
	/**
	 * Make sure she blows up if the arg list ends with an option
	 * that is stated to require an argument
	 */
	public void testEndOfListErrorHandling() {
		GetOpt getopt = new GetOpt("tn:");
		String[] badArgs = { "-t", "-n" };
		try {
			char c;
			while ((c = getopt.getopt(badArgs)) != GetOpt.DONE) {
				System.out.println("Found argument " + c);
			}
			fail("Did not throw exception for -n option missing an argument");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught expected IAE: " + e);
		}
	}
}
