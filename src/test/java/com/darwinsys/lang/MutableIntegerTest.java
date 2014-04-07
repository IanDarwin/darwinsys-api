package com.darwinsys.lang;

import junit.framework.TestCase;

/**
 * name - purpose
 */
public class MutableIntegerTest extends TestCase {
	MutableInteger target;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		target = new MutableInteger(42);
	}

	/*
	 * Class under test for void MutableInteger(int)
	 */
	public void testMutableIntegerint() {
		MutableInteger i = new MutableInteger(42);
		assertEquals("test 1-arg constructor", 42, i.getValue());
	}

	/*
	 * Class under test for void MutableInteger()
	 */
	public void testMutableInteger() {
		MutableInteger i = new MutableInteger();
		assertEquals("test 0-arg constructor", 0, i.getValue());
	}

	/*
	 * Class under test for int incr()
	 */
	public void testIncr() {
		assertEquals("test incr", 43, target.incr());
	}

	/*
	 * Class under test for int incr(int)
	 */
	public void testIncrint() {
		assertEquals("test incr(int)", 52, target.incr(10));
	}

	public void testDecr() {
		assertEquals("test decr", 41, target.decr());
		MutableInteger j = new MutableInteger(Integer.MIN_VALUE);
		assertEquals("decr below MIN_VALUE", 2147483647, j.decr());
	}

	public void testSetValue() {
		MutableInteger i = new MutableInteger(42);
		assertEquals("test setValue", -123, i.setValue(-123));
	}

	public void testGetValue() {
		assertEquals("getValue", 42, target.getValue());
	}

	/*
	 * Class under test for String toString()
	 */
	public void testToString() {
		assertEquals("toString", "42", target.toString());
	}

	/*
	 * Class under test for String toString(int)
	 */
	public void testToStringint() {
		assertEquals("toString(int)", "100", MutableInteger.toString(100));
	}

	public void testParseInt() {
		assertEquals("parseInt", 100, MutableInteger.parseInt("100"));
	}

}
