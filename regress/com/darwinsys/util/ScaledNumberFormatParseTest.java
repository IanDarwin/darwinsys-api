import junit.framework.*;

/** A simple test case for ScaledNumberFormat parse and format */

public class ScaledNumberFormatTest extends TestCase {

	/** JUnit test classes require this constructor */
	public ScaledNumberFormatTest(String name) {
		super(name);
	}

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
		new data("1.5M", 1625292),		// fractions
		new data("-2K",	-2048),	// negatives
		new data("-2.2k", -2252),
		new data("1G", 1073741824),
		new data("G", 0),
	};

	public void testParseGood() {
		for (int i = 0; i<sdata.length; i++) {
			try {
				Object o = sf.parseObject(sdata[i].string, null);
				assert(((Long)o).longValue() == sdata[i].number);
			} catch (Exception ex) {
				System.out.println("ERROR IN PARSE CASE " + i);
				System.out.println(sdata[i] + " threw " + ex);
			}
		}
	}

	/** data for format test */
	data ddata[] = {
		new data(0,      "     0B"),
		new data(999,    "   999B"),
		new data(1000,   "  1000B"),
		new data(1023,   "  1023B"),
		new data(1024,   "   1.0K"),
		new data(-1234,  "  -1.2K"),
		new data(1025,   "   1.1K"),
		new data(123456, "   120K"),
		new data(999999999L,    "   953G"),
	};

	public void testFormatGood() {
		for (int i = 0; i < ddata.length; i++) {
			try {
				assertEquals(ddata[i].string, sf.format(ddata[i].number));
			} catch (Exception ex) {
				System.out.println("ERROR IN FORMAT CASE " + i);
				System.out.println(ddata[i] + " threw " + ex);
			}
		}
	}
}
