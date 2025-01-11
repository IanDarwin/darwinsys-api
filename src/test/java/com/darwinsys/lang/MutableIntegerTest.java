package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * name - purpose
 */
class MutableIntegerTest {
	MutableInteger target;

	/*(non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@BeforeEach
	void setUp() throws Exception {
		target = new MutableInteger(42);
	}

	/*
	 * Class under test for void MutableInteger(int)
	 */
	@Test
	void mutableIntegerint() {
		MutableInteger i = new MutableInteger(42);
		assertEquals(42, i.getValue(), "test 1-arg constructor");
	}

	/*
	 * Class under test for void MutableInteger()
	 */
	@Test
	void mutableInteger() {
		MutableInteger i = new MutableInteger();
		assertEquals(0, i.getValue(), "test 0-arg constructor");
	}

	/*
	 * Class under test for int incr()
	 */
	@Test
	void incr() {
		assertEquals(43, target.incr(), "test incr");
	}

	/*
	 * Class under test for int incr(int)
	 */
	@Test
	void incrint() {
		assertEquals(52, target.incr(10), "test incr(int)");
	}

	@Test
	void decr() {
		assertEquals(41, target.decr(), "test decr");
		MutableInteger j = new MutableInteger(Integer.MIN_VALUE);
		assertEquals(2147483647, j.decr(), "decr below MIN_VALUE");
	}

	@Test
	void setValue() {
		MutableInteger i = new MutableInteger(42);
		assertEquals(-123, i.setValue(-123), "test setValue");
	}

	@Test
	void getValue() {
		assertEquals(42, target.getValue(), "getValue");
	}

	/*
	 * Class under test for String toString()
	 */
	@Test
	void testToString() {
		assertEquals("42", target.toString(), "toString");
	}

	/*
	 * Class under test for String toString(int)
	 */
	@Test
	void toStringint() {
		assertEquals("100", MutableInteger.toString(100), "toString(int)");
	}

	@Test
	void parseInt() {
		assertEquals(100, MutableInteger.parseInt("100"), "parseInt");
	}

}
