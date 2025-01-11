package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

/** Some test cases for GetOpt using the "new" coding pattern
* @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class GetOptTestNewPattern {
	
	Logger log = Logger.getLogger(getClass().getName());
	
	private String goodArgChars = "o:h";
	private String goodArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};
	private String goodLongArgs[]  = {
			"-help", "-output-file", "outfile", "infile"
	};

	private String badArgChars = "f1o";
	private String badArgs[]  = {
			"-h", "-o", "outfile", "infile"
	};

	private GetOptDesc[] newWayLongOptions = {
		new GetOptDesc('o', "output-file", true),
		new GetOptDesc('h', "help", false),
		new GetOptDesc('m', "hopping-mad", false),
	};

	@Test
	void good() {
		checkShortArgResults(goodArgChars, goodArgs, false);
	}
	
	//@Test
	public void testBadCharsGoodArgs() {
		checkShortArgResults(badArgChars, goodArgs, false);
	}

	@Test
	void badCharsBadArgs() {
		checkShortArgResults(badArgChars, badArgs, true);
	}

	private void checkShortArgResults(String argChars, String[] args, boolean shouldFail) {
		int errs = 0;
		log.info("** START NEW WAY ** " + argChars);
		GetOpt go2 = new GetOpt(argChars);
		Map<String,String> m = go2.parseArguments(args);
		if (m.size() == 0) {
			log.info("NO ARGS MATCHED");
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
				log.info("(no option)");
			else
				log.info("Option " + val);
		}

		List<String> filenames = go2.getFilenameList();
		for (int i = 0; i < filenames.size(); i++) {
			String fileName = (String)filenames.get(i);
			log.info("Filename-like arg " + fileName);
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
	void newWayShort() {
		GetOpt getopt = new GetOpt(newWayLongOptions);
		Map<String,String> map = getopt.parseArguments(goodArgs);
		checkLongArgResults(getopt, map);
	}

	@Test
	void newWayLong() {
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
			Entry<String, String> e = it.next();
			String key = (String)e.getKey();
			String val = (String)e.getValue();
			char c = key.charAt(0);
			log.info("checkLongArgResults() - c == " + c);
			switch(c) {
				case '?':
					errs++;
					break;
				case 'o':
					assertEquals("outfile", val);
					break;
				case 'f':
				case 'h':
				case '1':
					assertNull(val);
					break;
				default:
					throw new IllegalArgumentException("Unexpected c value " + c);
			}
		}
		assertEquals(0, errs, "Parse errors");
		assertEquals(1, getopt.getFilenameList().size(), "File names left after options");
		assertEquals("infile", getopt.getFilenameList().get(0), "File name from list");
	}

	/**
	 * Make sure the correct arguments get left in options,
	 * and that rewind() resets everything(?) correctly.
	 * XXX maybe split this test...
	 */
	@Test
	void rewindLeavesFilenamesLeft() {
		GetOpt getopt = new GetOpt("hn:");
		final String oneArg[]  = {
				"-h", "infile"
		};
		getopt.parseArguments(oneArg);
		assertEquals("infile", oneArg[getopt.getOptInd()], "testFilenamesLeft");
		final String twoArgs[]  = {
				"-h", "-n", "100", "infile"
		};
		getopt.rewind();
		Map<String,String> results = getopt.parseArguments(twoArgs);
		assertNotNull(results);
		assertTrue(results.size() > 0);
		List<String> nonOptionArgs = getopt.getFilenameList();
		assertEquals("infile", 
				nonOptionArgs.get(0), 
				"testFilenamesLeft");
	}
}
