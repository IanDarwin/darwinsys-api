package nativeregress;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.darwinsys.io.NextFD;

import junit.framework.TestCase;

public class NextFDTest extends TestCase {

	/**
	 * A file that exists on most POSIX systems; it is the file we open for
	 * reading iff no argument is passed on the command line.
	 */
	public static final String COMMON_FILE = "/etc/passwd";

	/**
	 * Test method for 'com.darwinsys.io.NextFD.getNextFD()'
	 */
	public void testGetNextFD() throws IOException {

		int start = NextFD.getNextFD();
		System.out.println("nextfd returned " + start);

		String fileName = COMMON_FILE;

		InputStream is = new FileInputStream(fileName);
		System.out.printf("File to %s is open.%n", fileName);

		int high = NextFD.getNextFD();
		System.out.println("nextfd returned " + high);
		if (high != start + 1) {
			throw new IllegalStateException("high != start + 1");
		}

		is.close();
		System.out.printf("File to %s is closed.%n", fileName);

		int end = NextFD.getNextFD();
		System.out.println("nextfd returned " + end);
		if (end != start) {
			throw new IllegalStateException("end != start");
		}
	}
}
