package regress;

import java.util.Iterator;
import junit.framework.*;
import com.darwinsys.util.IndexList;

public class IndexListTest extends TestCase {

	String[] TESTDATA = { "one", "two", "three" };

	IndexList victim = new IndexList();

	/** JUnit test classes require this constructor */
	public IndexListTest(String name) {
		super(name);
	}

	public void setUp() {
		for (int i = 0; i<TESTDATA.length; i++) {
			victim.add(TESTDATA[i]);
		}
	}

	public void testIterator() {
		Iterator it = victim.iterator();
		int i = 0;
		while (it.hasNext()) {
			assertEquals(TESTDATA[i++], it.next());
		}
	}

	public void testSetAndGet() {
		victim.set(1, "deux");
		assertEquals( "deux", victim.get(1));
	}

	public void testSize() {
		assertEquals( TESTDATA.length, victim.size());
	}
}
