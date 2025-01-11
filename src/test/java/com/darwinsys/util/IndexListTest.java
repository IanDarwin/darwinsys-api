package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndexListTest {

	String[] TESTDATA = { "one", "two", "three" };

	IndexList<String> target = new IndexList<String>();

	@BeforeEach
	void setUp() {
		for (int i = 0; i<TESTDATA.length; i++) {
			target.add(TESTDATA[i]);
		}
	}

	@Test
	void iterator() {
		Iterator<String> it = target.iterator();
		int i = 0;
		while (it.hasNext()) {
			assertEquals(TESTDATA[i++], it.next());
		}
	}

	@Test
	void setAndGet() {
		target.set(1, "deux");
		assertEquals( "deux", target.get(1));
	}

	@Test
	void size() {
		assertEquals( TESTDATA.length, target.size());
	}

	@Test
	void expanding() {
		for (int i = 0; i < 255; i++) {
			target.add(Integer.toString(i));
		}
	}
}
