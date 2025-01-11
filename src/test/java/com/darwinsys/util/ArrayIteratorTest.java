package com.darwinsys.util;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayIteratorTest {

	String[] TESTDATA = { "one", "two", "three" };

	ArrayIterator<String> it = new ArrayIterator<>(TESTDATA);

	@Test
	void gettingAndGettingTooMany() {
		int i = 0;
		while (it.hasNext()) {
				assertEquals(TESTDATA[i++], it.next());
			}
		assertThrows(NoSuchElementException.class, () ->
			// Do not split method here!!
			it.next());		// EXPECT RUNTIME ERROR
	}
}
