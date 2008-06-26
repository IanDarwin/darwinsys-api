package com.darwinsys.util;

import com.darwinsys.util.ScaledNumberFormat;

import junit.framework.*;

/** A better "data driven" test suite for ScaledNumberFormat format
 * ** DO NOT FOURIFY ** Leave as 3.8 to contrast with @Parameter version!
 */
public class ScaledNumberFormatFormatOldTest extends TestCase {

	static class TestDatum {
		long number;
		String expect;
		TestDatum(long n, String s) { this.number = n; this.expect = s; }
	}

	private ScaledNumberFormat sf = new ScaledNumberFormat();
	private long number;
	private String expect;

	/** data for format test.
	  */
	static TestDatum ddata[] = {
		new TestDatum(0,          "0B"),
		new TestDatum(999,      "999B"),
		new TestDatum(1000,    "1000B"),
		new TestDatum(1023,    "1023B"),
		new TestDatum(1024,    "1.0K"),
		new TestDatum(-1234,  "-1.2K"),
		new TestDatum(1484, "1.4K"),        /* rounding boundary, down */
		//new data(1485, "1.5K"),        /* rounding boundary, up   */
		//new data(-1484, "-1.4K"),      /* rounding boundary, down */
		//new data(-1485, "-1.5K"),      /* rounding boundary, up   */
		new TestDatum(1025,    "1.1K"),
		new TestDatum(123456, "120K"),
		new TestDatum(999999999L,   "953M"),
		new TestDatum(999999999999L,   "931G"),
		//new data(1L<<61, "2.0E"),
		//new data(1L<<62, "4.0E"),
		//new data(1099512676352L, "1.1T"),
	};

	public ScaledNumberFormatFormatOldTest(long number, String expect) {
		super("testFormat");
		this.number = number;
		this.expect = expect;
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		for (int i = 0; i < ddata.length; i++) {
			suite.addTest(new ScaledNumberFormatFormatOldTest(ddata[i].number, ddata[i].expect));
		}
		return suite;
	}

	public void testFormat() {
		String actual = sf.format(number);
		// System.out.println("Expect: " + expect + "; actual: " + actual);
		assertEquals(expect, expect, actual);
	}
}
