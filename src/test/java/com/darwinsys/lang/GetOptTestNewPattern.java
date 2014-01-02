package com.darwinsys.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.darwinsys.util.Debug;

/** Some test cases for GetOpt using the "new" coding pattern
* @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class GetOptTestNewPattern {
	
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
		new GetOptDesc('m', "hopping-mad", false),
	};

	@Test
	public void testGood() {
		checkShortArgResults(goodArgChars, goodArgs, false);
	}
	
	//@Test
	public void testBadCharsGoodArgs() {
		checkShortArgResults(badArgChars, goodArgs, false);
	}
	
	@Test
	public void testBadCharsBadArgs() {
		checkShortArgResults(badArgChars, badArgs, true);
	}

	private void checkShortArgResults(String argChars, String[] args, boolean shouldFail) {
		int errs = 0;
		Debug.println("getopt", "** START NEW WAY ** " + argChars);
		GetOpt go2 = new GetOpt(argChars);
		Map<String,String> m = go2.parseArguments(args);
		if (m.size() == 0) {
			Debug.println("getopt", "NO ARGS MATCHED");
		}
		Iterator<Map.Entry<String,String>> it = m.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> e = it.next();
			String key = e.getKey();
			String val = e.getValue();
			char c = key.charAt(0);
			if (c == '?') {
				System.err.print("Found " + c);
				errs++;
			}
			if (val == null || val.equals(""))
				Debug.println("getopt", "; (no option)");
			else
				Debug.println("getopt", "; Option " + val);
		}

		List filenames = go2.getFilenameList();
		for (int i = 0; i < filenames.size(); i++) {
			String fileName = (String)filenames.get(i);
			// System.out.println("Filename-like arg " + fileName);
			assertFalse(fileName.startsWith("-"));
		}

		if (shouldFail) {
			if (errs == 0) {
				fail("** FAILURE ** Expected errors not found");
			}
		} else {
			if (errs != 0) {
				fail("** FAILURE ** Unexpected errors were found");
			}
		}
	}
	
	@Test
	public void testNewWayShort() {
		GetOpt getopt = new GetOpt(newWayLongOptions);
		Map<String,String> map = getopt.parseArguments(goodArgs);
		checkLongArgResults(getopt, map);
	}
	
	@Test
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
			Debug.println("getopt", "checkLongArgResults() - c == " + c);
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
	@Test
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
