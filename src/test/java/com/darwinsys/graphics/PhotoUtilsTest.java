package com.darwinsys.graphics;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

public class PhotoUtilsTest {

	@BeforeClass
	public static void setUp() throws Exception {
	}

	@Test
	public void testIsoDateToExif() {
		String input = "2018-08-07T09:29:35";
		String expect = "2018:08:07 09:29:35";
		assertEquals(expect, PhotoUtils.isoDateToExif(input));
	}
	
	@Test
	public void testExifDateToIso() {
		String input = "2018:08:07 14:29:35";
		String expect = "2018-08-07T14:29:35";
		String actual = PhotoUtils.exifDateToIso(input);
		assertEquals(expect, actual);
		LocalDateTime.parse(actual); // Throws if bad
	}

}
