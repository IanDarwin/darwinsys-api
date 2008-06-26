package com.darwinsys.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/** A Parameterized test suite for ScaledNumberFormat format
 */
@RunWith(Parameterized.class)
public class ScaledNumberFormatFormatTest {

	private ScaledNumberFormat sf = new ScaledNumberFormat();
	private long number;
	private String expect;

	/** data for format test.
	  */
	@Parameters
	public static List<Object[]> getData() {	
		return Arrays.asList(new Object[][] {
				{0,          "0B"},
				{999,      "999B"},
				{1000,    "1000B"},
				{1023,    "1023B"},
				{1024,    "1.0K"},
				{-1234,  "-1.2K"},
				{1484, "1.4K"},        /* rounding boundary, down */
				//new data(1485, "1.5K"},        /* rounding boundary, up   */
				//new data(-1484, "-1.4K"},      /* rounding boundary, down */
				//new data(-1485, "-1.5K"},      /* rounding boundary, up   */
				{1025,    "1.1K"},
				{123456, "120K"},
				{999999999L,   "953M"},
				{999999999999L,   "931G"},
				//{1L<<61, "2.0E"},
				//{1L<<62, "4.0E"},
				//{1099512676352L, "1.1T"},
		});
	};

	public ScaledNumberFormatFormatTest(long number, String expect) {
		this.number = number;
		this.expect = expect;
	}

	@Test
	public void testFormat() {
		String actual = sf.format(number);
		// System.out.println("Expect: " + expect + "; actual: " + actual);
		assertEquals(expect, expect, actual);
	}
}
