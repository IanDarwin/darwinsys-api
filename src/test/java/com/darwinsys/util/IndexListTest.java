package com.darwinsys.util;

import java.util.Iterator;

import junit.framework.TestCase;

import com.darwinsys.util.IndexList;

public class IndexListTest extends TestCase {

	String[] TESTDATA = { "one", "two", "three" };

	IndexList<String> target = new IndexList<String>();

	/** JUnit test classes require this constructor */
	public IndexListTest(String name) {
		super(name);
	}

	public void setUp() {
		for (int i = 0; i<TESTDATA.length; i++) {
			target.add(TESTDATA[i]);
		}
	}

	public void testIterator() {
		Iterator it = target.iterator();
		int i = 0;
		while (it.hasNext()) {
			assertEquals(TESTDATA[i++], it.next());
		}
	}

	public void testSetAndGet() {
		target.set(1, "deux");
		assertEquals( "deux", target.get(1));
	}

	public void testSize() {
		assertEquals( TESTDATA.length, target.size());
	}
	
	public void testExpanding() {
		for (int i = 0; i < 255; i++) {
			target.add(Integer.toString(i));
		}
	}
}
