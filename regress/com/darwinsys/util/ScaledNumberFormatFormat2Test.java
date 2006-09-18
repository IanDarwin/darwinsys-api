package com.darwinsys.util;

import com.darwinsys.util.ScaledNumberFormat;

import junit.framework.*;

/** A few separate tests for ScaledNumberFormat parse and format */
public class ScaledNumberFormatFormat2Test extends TestCase {

	private ScaledNumberFormat sf = new ScaledNumberFormat();

	public void testFormatThreeArgs() throws Exception {
		StringBuffer sb = new StringBuffer();
		assertEquals("sb return", sb, sf.format("999999999", sb, null));
		assertEquals("format3Args", "953M", sb.toString());
		System.out.println("Format 3 args => " + sb.toString());
	}
	public void testNullInput() {
		StringBuffer sb = new StringBuffer();
		sf.format("", sb, null);
		assertEquals("format3Args", "0B", sb.toString());
		try {
			sf.format(null, sb, null);
		} catch (IllegalArgumentException ex) {
			System.out.println("Caught expected IAE on null input");
		}
	}
}
