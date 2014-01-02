package com.darwinsys.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.darwinsys.util.Debug;

/** Some test cases for GetOpt using the "traditional" coding pattern.
* @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class GetOptTestOldPattern {
	
	private String goodArgChars = "o:h";
	private String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	private 	String badArgChars = "f1o";
	private String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};

	@Test
	public void testOldwayGood() {
		processUnixWay(goodArgChars, goodArgs, false);
	}
	
	@Test
	public void testOldwayBadCharsGoodArgs() {
		processUnixWay(badArgChars, goodArgs, true);
	}
	
	@Test
	public void testOldwayBadCharsBadArgs() {
		processUnixWay(badArgChars, badArgs, true);
	}
	
	void processUnixWay(String argChars, String[] args, boolean shouldFail) {

		GetOpt getopt = new GetOpt(argChars);

		int errs = 0;

		char c;
		while ((c = getopt.getopt(args)) != 0) {
			if (c == '?') {
				++errs;
			} else {
				Debug.println("getopt", "Found " + c);
				//if (getopt.optarg() != null)
				//	System.out.print("; Option " + getopt.optarg());
			}
		}

		// Process any filename-like arguments.
		assertTrue(args.length >= getopt.getOptInd());
		for (int i = getopt.getOptInd(); i<args.length; i++) {
			Debug.printf("getopt", "%d %s%n", i, args[i]);
			String fileName = args[i];
			assertFalse(fileName.startsWith("-"));
		}
	}
	
	/** Make sure that all args get parsed, and that optarg is 
	 * null when it should be
	 */
	@Test
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
	@Test(expected=IllegalArgumentException.class)
	public void testEndOfListErrorHandling() {
		GetOpt getopt = new GetOpt("tn:");
		String[] badArgs = { "-t", "-n" };

		char c;
		while ((c = getopt.getopt(badArgs)) != GetOpt.DONE) {
			Debug.println("getopt", "Found argument " + c);
		}
			
	}
}
