package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.darwinsys.util.Debug;

/** Some test cases for GetOpt using the "traditional" coding pattern.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
class GetOptTestOldPattern {
	
	private String goodArgChars = "o:h";
	private String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	private String badArgChars = "f1o";
	private String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	
	private String combinedArgChars = "ilr";
	private String[] combinedArgs = {
		"-ril", "infile"	
	};

	@Test
	void oldwayGood() {
		processUnixWay(goodArgChars, goodArgs, false);
	}

	@Test
	void oldwayBadCharsGoodArgs() {
		processUnixWay(badArgChars, goodArgs, true);
	}

	@Test
	void oldwayBadCharsBadArgs() {
		processUnixWay(badArgChars, badArgs, true);
	}

	@Test
	@Disabled("GetOpt cannot handle combined args yet!!")
	void combinedGood() {
		processUnixWay(combinedArgChars, combinedArgs, false);
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
		
		if (errs > 0 && !shouldFail) {
			fail("Errors in this run");
		}
	}

	/** Make sure that all args get parsed, and that optarg is 
	 * null when it should be
	 */
	@Test
	void allArgsDone() {
		GetOpt getopt = new GetOpt("tn:");
		String[] args = { "-n", "100", "-t" };
		assertEquals('n', getopt.getopt(args));
		assertEquals("100", getopt.optarg);
		assertEquals((int)'t', (int)getopt.getopt(args));
		assertNull(getopt.optarg);
	}

	/**
	 * Make sure she blows up if the arg list ends with an option
	 * that is stated to require an argument
	 */
	@Test
	void endOfListErrorHandling() {
		final GetOpt getopt = new GetOpt("tn:");
		final String[] badArgs = {"-t", "-n"};
		assertThrows(IllegalArgumentException.class, () -> {
			char c;
			while ((c = getopt.getopt(badArgs)) != GetOpt.DONE) {
				Debug.println("getopt", "Found argument " + c);
			}

		});

	}
}
