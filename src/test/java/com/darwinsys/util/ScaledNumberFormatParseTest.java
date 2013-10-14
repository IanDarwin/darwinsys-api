package com.darwinsys.util;

import com.darwinsys.util.ScaledNumberFormat;

import junit.framework.*;

/** A simple test suite for ScaledNumberFormat parse and format */

public class ScaledNumberFormatParseTest extends TestCase {

	static class TestDatum {
		long number;
		String string;
		TestDatum(long n, String s) { number = n; string = s; }
		TestDatum(String s, long n) { this(n, s); }
	}

	ScaledNumberFormat sf = new ScaledNumberFormat();

	/** data for scan (parse) test */
	TestDatum sdata[] = {
		new TestDatum("123", 123),
		new TestDatum("1k", 1024),		// lower case
		new TestDatum("10099", 10099),
		new TestDatum("1M", 1024*1024),
		new TestDatum("1.5M", 1572864),		// fractions
		new TestDatum("-2K",	-2048),	// negatives
		new TestDatum("-2.2k", -2252),
		new TestDatum("0.5G", 536870912),
		new TestDatum("1G", 1073741824),
		new TestDatum("G", 0),
		new TestDatum("931G", 999653638144L),
	};

	public void testParseGood() throws Exception {
		for (int i = 0; i<sdata.length; i++) {
			Object o = sf.parseObject(sdata[i].string, null);
			assertEquals(sdata[i].string, sdata[i].number, ((Long)o).longValue());
		}
	}

}
