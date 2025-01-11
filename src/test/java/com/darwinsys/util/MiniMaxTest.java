package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MiniMaxTest {

	@Test
	void maxInt() {
		int[] data = { 1, 3, 4};
		assertEquals(4, MiniMax.max(data));
	}

	@Test
	void maxInteger() {
		Integer[] data = { 1, 3, 4};
		assertEquals(Integer.valueOf(4), MiniMax.max(data));
	}
}
