package com.darwinsys.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

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
		Map<String,String> map = go.parseArguments(goodArgs);
		newWayInner(go, map);
	}
	public void testNewWayLong() {
		GetOpt go = new GetOpt(options);
		Map<String,String> map = go.parseArguments(goodLongArgs);
		newWayInner(go, map);
	}

	protected void newWayInner(GetOpt go, Map<String,String> map) {
		assertFalse(map.size() == 0);
		if (map.size() == 0) {
			throw new IllegalArgumentException(
				"Unexpected empty map");
		}
		int errs = 0;
		Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
		while (it.hasNext()) 
			{
			Map.Entry e = it.next();
			String key = (String)e.getKey();
			String val = (String)e.getValue();
			char c = key.charAt(0);
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

		int errs = 0;

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
		Map<String,String> m = go2.parseArguments(args);
		if (m.size() == 0)
			System.out.println("NO ARGS MATCHED");
		Iterator<Map.Entry<String,String>> it = m.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> e = it.next();
			String key = e.getKey();
			String val = e.getKey();
			char c = key.charAt(0);
			System.out.print("Found " + c);
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
