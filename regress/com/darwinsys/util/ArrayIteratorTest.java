package com.darwinsys.util;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import org.junit.Test;

import com.darwinsys.util.ArrayIterator;

public class ArrayIteratorTest {

	String[] TESTDATA = { "one", "two", "three" };

	ArrayIterator it = new ArrayIterator(TESTDATA);

	@Test(expected=NoSuchElementException.class)
	public void testGettingAndGettingTooMany() {
		int i = 0;
		while (it.hasNext()) {
			assertEquals(TESTDATA[i++], it.next());
		}
		// Do not split method here!!
		it.next();		// EXPECT RUNTIME ERROR
	}
}
