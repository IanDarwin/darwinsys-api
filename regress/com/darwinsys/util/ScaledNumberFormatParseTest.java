package regress;
import com.darwinsys.util.ScaledNumberFormat;

import junit.framework.*;

/** A simple test case for ScaledNumberFormat parse and format */

public class ScaledNumberFormatTest extends TestCase {

	class data {
		long number;
		String string;
		data(long n, String s) { number = n; string = s; }
		data(String s, long n) { this(n, s); }
	}

	ScaledNumberFormat sf = new ScaledNumberFormat();

	/** data for scan (parse) test */
	data sdata[] = {
		new data("123", 123),
		new data("1k", 1024),		// lower case
		new data("10099", 10099),
		new data("1M", 1024*1024),
		new data("1.5M", 1572864),		// fractions
		new data("-2K",	-2048),	// negatives
		new data("-2.2k", -2252),
		new data("0.5G", 536870912),
		new data("1G", 1073741824),
		new data("G", 0),
		new data("931G", 999653638144L),
	};

	public void testParseGood() throws Exception {
		for (int i = 0; i<sdata.length; i++) {
			Object o = sf.parseObject(sdata[i].string, null);
			assertEquals(sdata[i].string, sdata[i].number, ((Long)o).longValue());
		}
	}

	/** data for format test.
	  */
	data ddata[] = {
		new data(0,          "0B"),
		new data(999,      "999B"),
		new data(1000,    "1000B"),
		new data(1023,    "1023B"),
		new data(1024,    "1.0K"),
		new data(-1234,  "-1.2K"),
		new data(1025,    "1.1K"),
		new data(123456, "120K"),
		new data(999999999L,   "953M"),
		new data(999999999999L,   "931G"),
	};

	public void testFormatGood() {
		for (int i = 0; i < ddata.length; i++) {
			try {
				String expect = ddata[i].string;
				String actual = sf.format(ddata[i].number);
				System.out.println("Expect: " + expect + "; actual: " + actual);
				assertEquals(expect, expect, actual);
			} catch (Exception ex) {
				System.out.println("ERROR IN FORMAT CASE " + i);
				System.out.println(ddata[i] + " threw " + ex);
			}
		}
	}
	
	public void testFormatThreeArgs() throws Exception {
		StringBuffer sb = new StringBuffer();
		assertEquals("sb return", sb, sf.format("999999999", sb, null));
		assertEquals("format3Args", "953M", sb.toString());
		System.out.println("Format 3 args => " + sb.toString());
		sb.setLength(0);
		sf.format("", sb, null);
		assertEquals("format3Args", "0B", sb.toString());
		try {
			sf.format(null, sb, null);
		} catch (IllegalArgumentException ex) {
			System.out.println("Caught expected IAE on null input");
		}
	}
}
