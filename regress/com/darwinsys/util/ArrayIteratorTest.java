package regress;

import junit.framework.*;

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

		// XXX read up on writing JUnit tests that should throw exceptions
		try {
			it.next();		// EXPECT RUNTIME ERROR
			System.err.println("ERROR - DID NOT GET EXPECTED EXCEPTION");
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Got expected exception -- OK!");
		}
	}
}
