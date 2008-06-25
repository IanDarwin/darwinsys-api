package com.darwinsys.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/** Some test cases for GetOpt using the "new" coding pattern
* @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class GetOptTestNewPattern extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// System.out.println("GetOptTestNewPattern.setUp()");
		System.setProperty("debug.getopt", "sure");
	}
	
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

	private GetOptDesc[] newWayLongOptions = {
		new GetOptDesc('o', "output-file", true),
		new GetOptDesc('h', "help", false),
	};

	public void testGood() {
		checkShortArgResults(goodArgChars, goodArgs, false);
	}
	public void testBadCharsGoodArgs() {
		checkShortArgResults(badArgChars, goodArgs, false);
	}
	public void testBadCharsBadArgs() {
		checkShortArgResults(badArgChars, badArgs, true);
	}

	private void checkShortArgResults(String argChars, String[] args, boolean shouldFail) {
		int errs = 0;
		// System.out.println("** START NEW WAY ** " + argChars);
		GetOpt go2 = new GetOpt(argChars);
		Map<String,String> m = go2.parseArguments(args);
		if (m.size() == 0) {
			// System.out.println("NO ARGS MATCHED");
		}
		Iterator<Map.Entry<String,String>> it = m.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> e = it.next();
			String key = e.getKey();
			String val = e.getValue();
			char c = key.charAt(0);
			// System.out.print("Found " + c);
			if (c == '?')
				errs++;
			if (val == null || val.equals(""))
				System.out.print("; (no option)");
			else
				System.out.print("; Option " + val);
			System.out.println();
		}

		List filenames = go2.getFilenameList();
		for (int i = 0; i < filenames.size(); i++) {
			String fileName = (String)filenames.get(i);
			// System.out.println("Filename-like arg " + fileName);
			assertFalse(fileName.startsWith("-"));
		}

		if (shouldFail) {
			if (errs != 0) {
				// System.out.println("Expected error(s) found");
			} else {
				System.out.println("** FAILURE ** Expected errors not found");
			}
		} else {
			if (errs == 0) {
				// System.out.println("Expected errs==0 found");
			} else {
				System.out.println("** FAILURE ** Expected errors not found");
			}
		}
	}
	
	public void testNewWayShort() {
		GetOpt getopt = new GetOpt(newWayLongOptions);
		Map<String,String> map = getopt.parseArguments(goodArgs);
		checkLongArgResults(getopt, map);
	}
	
	public void testNewWayLong() {
		GetOpt getopt = new GetOpt(newWayLongOptions);
		Map<String,String> map = getopt.parseArguments(goodLongArgs);
		checkLongArgResults(getopt, map);
	}

	/** Common code for testing new-style usage
	 * @param getopt - a parser based on
	 * @param parsedArgsMap - result of parsing goodLongArgs
	 */
	protected void checkLongArgResults(GetOpt getopt, Map<String,String> parsedArgsMap) {

		assertFalse(parsedArgsMap.size() == 0);
		int errs = 0;
		Iterator<Map.Entry<String,String>> it = 
			parsedArgsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = it.next();
			String key = (String)e.getKey();
			String val = (String)e.getValue();
			char c = key.charAt(0);
			// System.out.println("checkLongArgResults() - c == " + c);
			switch(c) {
				case '?':
					errs++;
					break;
				case 'o': 
					assertEquals(val, "outfile");
					break;
				case 'f':
				case 'h':
				case '1':
					 assertEquals(val, null);
					break;
				default:
					throw new IllegalArgumentException("Unexpected c value " + c);
			}
		}
		assertEquals(1, getopt.getFilenameList().size());
		assertEquals("infile", getopt.getFilenameList().get(0));
	}
	
	/**
	 * Make sure the correct arguments get left in options,
	 * and that rewind() resets everything(?) correctly.
	 * XXX maybe split this test...
	 */
	public void testRewindLeavesFilenamesLeft() {
		GetOpt getopt = new GetOpt("hn:");
		final String oneArg[]  = {
				"-h", "infile"
		};
		getopt.parseArguments(oneArg);
		assertEquals("testFilenamesLeft", "infile", oneArg[getopt.getOptInd()]);
		final String twoArgs[]  = {
				"-h", "-n", "100", "infile"
		};
		getopt.rewind();
		Map<String,String> results = getopt.parseArguments(twoArgs);
		assertNotNull(results);
		assertTrue(results.size() > 0);
		List<String> nonOptionArgs = getopt.getFilenameList();
		assertEquals("testFilenamesLeft", "infile", 
				nonOptionArgs.get(0));
	}
}
