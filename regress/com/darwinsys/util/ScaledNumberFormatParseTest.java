public class ScaledNumberFormatTest {

	static String sdata[] = {
		"123",
		"1k",		// lower case
		"10099",
		"1M",
		"1.5M",		// fractions
		"-2K",		// negatives
		"-2.2k",
		"1G",
		"G",		// might == 0G?
		"1.5Q"		// should fail
	};

	static long ddata[] = {
		0,
		999,
		1000,
		1023,
		1024,
		-1234,
		1025,
		123456,
		999999999L,
	};

	public static void main(String[] argv) {
		int i;

		ScaledNumberFormat sf = new ScaledNumberFormat();

		System.out.println("Start scan tests\n");
		for (i=0; i<sdata.length; i++) {
			try {
				System.out.println(sdata[i] + "\t" + sf.parseObject(sdata[i], null));
			} catch (Exception ex) {
				System.out.println(sdata[i] + " threw " + ex);
			}
		}

		System.out.println("Start fmt_scaled tests\n");
		for (i=0; i<ddata.length; i++) {
			try {
				System.out.println(ddata[i] + "\t" + sf.format(ddata[i]));
			} catch (Exception ex) {
				System.out.println(ddata[i] + " threw " + ex);
			}
		}
	}
}
