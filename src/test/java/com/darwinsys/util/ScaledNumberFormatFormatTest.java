package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** A Parameterized test suite for ScaledNumberFormat format
 */
public class ScaledNumberFormatFormatTest {

	private ScaledNumberFormat sf = new ScaledNumberFormat();
	private long number;
	private String expect;

	/** data for format test.
	  */
	public static List<Object[]> getData() {
		// Use Hex 2D for "-" to avoid Locale-specific failures!
		return Arrays.asList(new Object[][] {
				{0,          "0B"},
				{999,      "999B"},
				{1000,    "1000B"},
				{1023,    "1023B"},
				{1024,    "1.0K"},
				{-1234,  "\u002d1.2K"},
				{1523, "1.4K"},        /* rounding boundary, down */
				{1524, "1.5K"},        /* rounding boundary, up   */
				{-1523, "\u002d1.4K"},      /* rounding boundary, down */
				{-1524, "\u002d1.5K"},      /* rounding boundary, up   */
				{1025,    "1.1K"},
				{123456, "120K"},
				{999999999L,   "953M"},
				{999999999999L,   "931G"},
				//{1L<<61, "2.0E"},
				//{1L<<62, "4.0E"},
				//{1099512676352L, "1.1T"},
		});
	}

	public void initScaledNumberFormatFormatTest(long number, String expect) {
		this.number = number;
		this.expect = expect;
	}

	@MethodSource("getData")
	@ParameterizedTest
	public void testFormat(long number, String expect) {
		initScaledNumberFormatFormatTest(number, expect);
		String actual = sf.format(number);
		// System.out.println("Expect: " + expect + "; actual: " + actual);
		assertEquals(expect, actual, expect);
	}
}
