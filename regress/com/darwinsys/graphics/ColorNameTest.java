package com.darwinsys.graphics;

import java.awt.Color;

import junit.framework.TestCase;

public class ColorNameTest extends TestCase {
	
	public static void testLookup() {
		assertEquals("WhItE", Color.WHITE, ColorName.getColor("WhItE"));
		assertEquals("magenta", Color.MAGENTA, ColorName.getColor("magenta"));
		assertEquals("BLACK", Color.BLACK, ColorName.getColor("BLACK"));
		assertEquals("Ucky Purple Pink Spots", null, ColorName.getColor("Ucky Purple Pink Spots"));
		assertEquals(null, null, ColorName.getColor(null));
		assertEquals("#c0c0c0", new Color(192,192,192), ColorName.getColor("#c0c0c0"));
	}
}
