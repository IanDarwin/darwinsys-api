package util;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

import com.darwinsys.util.ArrayIterator;

public class ArrayIteratorTest extends TestCase {

	String[] TESTDATA = { "one", "two", "three" };

	ArrayIterator it = new ArrayIterator(TESTDATA);

	/** JUnit test classes require this constructor */
	public ArrayIteratorTest(String name) {
		super(name);
	}

	/** Simple tryout */
	public void testGetting() {
		int i = 0;
		while (it.hasNext()) {
			assertEquals(TESTDATA[i++], it.next());
		}

		try {
			it.next();		// EXPECT RUNTIME ERROR
			fail("DID NOT GET EXPECTED EXCEPTION");
		} catch (NoSuchElementException e) {
			System.err.println("Got expected exception -- OK!");
		}
	}
}
