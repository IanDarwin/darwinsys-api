package com.darwinsys.util;

import junit.framework.TestCase;

public class MiniMaxTest extends TestCase {

	public void testMaxInt() {
		int[] data = { 1, 3, 4};
		assertEquals(4, MiniMax.max(data));
	}
	public void testMaxInteger() {
		Integer[] data = { 1, 3, 4};
		assertEquals(Integer.valueOf(4), MiniMax.max(data));
	}
}
