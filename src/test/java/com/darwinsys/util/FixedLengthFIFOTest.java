package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedLengthFIFOTest {
	
	private final boolean chatterbox = false;
	
	private static final int SMALL_TEST_SIZE = 10;
	private static final String JUNKSTRING = "fjdklsjfdls";
	// Number of elements here must be > SMALL_TEST_SIZE to test getting the last element
	private static final String[] moreJunk = {
		JUNKSTRING, "jdklsfj", "abc", "def", "ghi", "jkl", "mno", "pqr", "stuart", "vwx", "zzz", "zzzz"
	};
	FixedLengthFIFO<String> ff;

	@BeforeEach
	void setUp() throws Exception {
		ff = new FixedLengthFIFO<String>(10);
	}

	@Test
	void basics() throws Exception {
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
	void onlyOne() throws Exception {
		ff = new FixedLengthFIFO<String>(10);
		String string = "One";
		ff.add(string);
		ff.remove(string);
	}

	@Test
	void limits() throws Exception {
		assertTrue(moreJunk.length > SMALL_TEST_SIZE);
		for (String d : moreJunk) {
			ff.add(d);
		}
		assertEquals(SMALL_TEST_SIZE, ff.size());
		assertSame(ff.get(SMALL_TEST_SIZE-1), moreJunk[moreJunk.length-1]);
	}

	@Test
	void iterator() {
		addaCoupleOfStrings();
		Iterator<String> it = ff.iterator();
		it.next();
		assertThrows(UnsupportedOperationException.class, () ->
			it.remove());	// should throw
	}

	private void addaCoupleOfStrings() {
		ff.add("Test data one");
		ff.add("Test data two");
	}

	@Test
	void alwaysCreatesIterator() {	
		addaCoupleOfStrings();
		Iterator<String> it = ff.iterator();
		Iterator<String> it2 = ff.iterator();
		assertTrue(it != it2);
		while (it2.hasNext()) {
			final Object next = it2.next();
			if (chatterbox) {
				System.out.println("Iterator 2 output: " + next);
			}
		}
	}

	@Test
	void throwsConcurrentModificationException() {
		Iterator<String> it3 = ff.iterator();
		addaCoupleOfStrings();
		assertThrows(ConcurrentModificationException.class, () ->
			it3.hasNext());	// Should throw CME
	}

	@Test
	void types() {
		FixedLengthFIFO<Date> ff = new FixedLengthFIFO<Date>(15);
		ff.add(new Date());
		Date today = Calendar.getInstance().getTime();
		ff.add(today);
		assertEquals(2, ff.size());
		assertSame(ff.get(1), today);
	}

	@Test
	void toArrayArrayType() {
		String[] o = ff.toArray(new String[0]);
		assertNotNull(o);
		if (chatterbox) {
			System.out.println(o.getClass().getName());
		}
		Vector<String> vv = new Vector<String>();
		Object ov = vv.toArray(new String[0]);
		if (chatterbox) {
			System.out.println(ov.getClass().getName());
		}
	}

	@Test
	void lastIndex() {
		int where = ff.size();
		String d = "JKDJFLSJLKDJF";
		ff.add(d);
		ff.add(new Date().toString());
		assertEquals(where, ff.lastIndexOf(d));
	}

	@Test
	void set() {
		ff.add("hello");
		ff.add("goodbye");
		ff.set(0, "new");
		assertEquals(2, ff.size());
		assertEquals("goodbye", ff.get(1));
		assertEquals("new", ff.get(0));
	}

	@Test
	void subList() {
		FixedLengthFIFO<Integer> gg = new FixedLengthFIFO<Integer>(10);
		Integer i2 = 20;
		Integer i3 = 30;
		gg.add(10);
		gg.add(i2);
		gg.add(i3);
		gg.add(40);
		
		List<Integer> remnant = gg.subList(1,2);
		assertEquals(2, remnant.size());
		assertSame(i2, remnant.get(0));
		assertSame(i3, remnant.get(1));
	}
}
