package com.darwinsys.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Last few separate tests for ScaledNumberFormat parse and format
 */
public class ScaledNumberFormatFormatZZTest {

	private ScaledNumberFormat sf = new ScaledNumberFormat();

	@Test
	public void testFormatThreeArgs() throws Exception {
		StringBuffer sb = new StringBuffer();
		assertEquals("sb return", sb, sf.format("999999999", sb, null));
		assertEquals("format3Args", "953M", sb.toString());
	}

	@Test
	public void testNullInput() {
		StringBuffer sb = new StringBuffer();
		sf.format("", sb, null);
		assertEquals("format3Args", "0B", sb.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullThrowsIAE() {
		StringBuffer sb = new StringBuffer();
		sf.format(null, sb, null);	// should throw IAE
	}
}
