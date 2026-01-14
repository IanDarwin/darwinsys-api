package com.darwinsys.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class ColorNameTest {

	@Test
	public void lookup() {
		assertEquals(Color.WHITE, ColorName.getColor("WhItE"), "WhItE");
		assertEquals(Color.MAGENTA, ColorName.getColor("magenta"), "magenta");
		assertEquals(Color.BLACK, ColorName.getColor("BLACK"), "BLACK");
		assertNull(ColorName.getColor("Ucky Purple Pink Spots"), "Ucky Purple Pink Spots");
		assertNull(ColorName.getColor(null));
		assertEquals(new Color(192,192,192), ColorName.getColor("#c0c0c0"), "#c0c0c0");
	}
}
