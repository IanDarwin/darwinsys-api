package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Last few separate tests for ScaledNumberFormat parse and format
 */
class ScaledNumberFormatFormatZZTest {

	private ScaledNumberFormat sf = new ScaledNumberFormat();

	@Test
	void formatThreeArgs() throws Exception {
		StringBuffer sb = new StringBuffer();
		assertEquals(sb, sf.format("999999999", sb, null), "sb return");
		assertEquals("953M", sb.toString(), "format3Args");
	}

	@Test
	void nullInput() {
		StringBuffer sb = new StringBuffer();
		sf.format("", sb, null);
		assertEquals("0B", sb.toString(), "format3Args");
	}

	@Test
	void nullThrowsIAE() {
		StringBuffer sb = new StringBuffer();
		assertThrows(IllegalArgumentException.class, () ->
			sf.format(null, sb, null));	// should throw IAE
	}
}
