package util;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;

import com.darwinsys.util.FixedLengthFIFO;

import junit.framework.TestCase;

public class FixedLengthFIFOTest extends TestCase {
	private static final int SMALL_TEST_SIZE = 10;
	private static final String JUNKSTRING = "fjdklsjfdls";
	// Number of elements here must be > SMALL_TEST_SIZE to test getting the last element
	private static final String[] moreJunk = {
		JUNKSTRING, "jdklsfj", "abc", "def", "ghi", "jkl", "mno", "pqr", "stuart", "vwx", "zzz", "zzzz"
	};
	FixedLengthFIFO<String> ff;
	
	protected void setUp() throws Exception {
		ff = new FixedLengthFIFO<String>(10);
	}

	public void testBasics() throws Exception {
		ff.add("hello");
		ff.add("goodbye");
		assertEquals(2, ff.size());
		assertEquals("hello", ff.get(0));
		ff.remove("hello");
		assertEquals(1, ff.size());
		assertFalse(ff.remove("nobody here"));
		ff.add(JUNKSTRING);
		assertEquals(2, ff.size());
		
		// A List is allowed to contain the same element more than once
		ff.add(JUNKSTRING);
		assertEquals(3, ff.size());
		
		assertSame(ff.indexOf(JUNKSTRING), ff.indexOf(JUNKSTRING));
		assertNotSame(ff.indexOf(JUNKSTRING), ff.lastIndexOf(JUNKSTRING));
	}
	
	public void testLimits() throws Exception {
		assertTrue(moreJunk.length > SMALL_TEST_SIZE);
		for (String d : moreJunk) {
			ff.add(d);
		}
		assertEquals(SMALL_TEST_SIZE, ff.size());
		assertSame(ff.get(SMALL_TEST_SIZE-1), moreJunk[moreJunk.length-1]);
	}
	
	public void testIterator() throws Exception {
		ff.add("Test data one");
		ff.add("Test data two");
		Iterator<String> it = ff.iterator();
		try {
			System.out.println("First element from first iterator: " + it.next());
			it.remove();
			fail("Iterator.remove() did not throw expected Exception");
		} catch (IllegalArgumentException e) {
			// nothing to do
		}
		while (it.hasNext()) {
			System.out.println("Iterator 1 output: " + it.next());
		}
		
		// Get another
		Iterator it2 = ff.iterator();
		assertNotSame(it, it2);
		while (it2.hasNext()) {
			System.out.println("Iterator 2 output: " + it2.next());
		}
		
		// Check for concurrentmod
		Iterator it3 = ff.iterator();
		ff.add("any old thing");
		try {
			it3.hasNext();
			fail("did not throw ConcurrentModificationException");
		} catch (ConcurrentModificationException e) {
			System.out.println("Did catch expected ConcurrentModificationException");
		}
	}
	
	public void testTypes() {
		FixedLengthFIFO<Date> ff = new FixedLengthFIFO<Date>(15);
		ff.add(new Date());
		Date today = Calendar.getInstance().getTime();
		ff.add(today);
		assertEquals(2, ff.size());
		assertSame(ff.get(1), today);
	}
}
