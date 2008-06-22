package com.darwinsys.util;

import java.util.Calendar; import static org.junit.Assert.*;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.darwinsys.util.FixedLengthFIFO;

public class FixedLengthFIFOTest {
	
	private boolean chatterbox = true;
	
	private static final int SMALL_TEST_SIZE = 10;
	private static final String JUNKSTRING = "fjdklsjfdls";
	// Number of elements here must be > SMALL_TEST_SIZE to test getting the last element
	private static final String[] moreJunk = {
		JUNKSTRING, "jdklsfj", "abc", "def", "ghi", "jkl", "mno", "pqr", "stuart", "vwx", "zzz", "zzzz"
	};
	FixedLengthFIFO<String> ff;
	
	@Before
	public void setUp() throws Exception {
		ff = new FixedLengthFIFO<String>(10);
	}

	@Test
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
	
	@Test
	public void testOnlyOne() throws Exception {
		ff = new FixedLengthFIFO<String>(10);
		String string = "One";
		ff.add(string);
		ff.remove(string);
	}
	
	@Test
	public void testLimits() throws Exception {
		assertTrue(moreJunk.length > SMALL_TEST_SIZE);
		for (String d : moreJunk) {
			ff.add(d);
		}
		assertEquals(SMALL_TEST_SIZE, ff.size());
		assertSame(ff.get(SMALL_TEST_SIZE-1), moreJunk[moreJunk.length-1]);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testIterator() throws Exception {
		ff.add("Test data one");
		ff.add("Test data two");
		Iterator<String> it = ff.iterator();
		it.next();
		it.remove();	// should throw
	}
	
	@Test
	public void testAlwaysCreatesIterator() {	
		Iterator it = ff.iterator();
		Iterator it2 = ff.iterator();
		assertTrue(it != it2);
		while (it2.hasNext()) {
			if (chatterbox) {
				System.out.println("Iterator 2 output: " + it2.next());
			}
		}
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void testThrowsConcurrentModificationException() {
		// Check for concurrentmod
		Iterator it3 = ff.iterator();
		ff.add("any old thing");
		it3.hasNext();	// Should throw CME
	}
	
	@Test
	public void testTypes() {
		FixedLengthFIFO<Date> ff = new FixedLengthFIFO<Date>(15);
		ff.add(new Date());
		Date today = Calendar.getInstance().getTime();
		ff.add(today);
		assertEquals(2, ff.size());
		assertSame(ff.get(1), today);
	}
	
	@Test
	public void testToArrayArrayType() {
		String[] o = ff.toArray(new String[0]);
		assertNotNull(o);
		System.out.println(o.getClass().getName());
		Vector<String> vv = new Vector<String>();
		Object ov = vv.toArray(new String[0]);
		if (chatterbox) {
			System.out.println(ov.getClass().getName());
		}
	}
	
	@Test
	public void testLastIndex() {
		int where = ff.size();
		String d = "JKDJFLSJLKDJF";
		ff.add(d);
		ff.add(new Date().toString());
		assertEquals(where, ff.lastIndexOf(d));
	}
	
	@Test
	public void testSet() {
		ff.add("hello");
		ff.add("goodbye");
		ff.set(0, "new");
		assertEquals(2, ff.size());
		assertEquals("goodbye", ff.get(1));
		assertEquals("new", ff.get(0));
	}
	
	@Test
	public void testSubList() {
		FixedLengthFIFO<Integer> gg = new FixedLengthFIFO<Integer>(10);
		Integer i1 = 10;
		Integer i2 = 20;
		Integer i3 = 30;
		Integer i4 = 40;
		gg.add(i1);
		gg.add(i2);
		gg.add(i3);
		gg.add(i4);
		
		List<Integer> remnant = gg.subList(1,2);
		assertEquals(2, remnant.size());
		assertSame(i2, remnant.get(0));
		assertSame(i3, remnant.get(1));
	}
}
