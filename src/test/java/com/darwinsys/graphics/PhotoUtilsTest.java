package com.darwinsys.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PhotoUtilsTest {

	@BeforeAll
	static void setUp() throws Exception {
	}

	@Test
	void isoDateToExif() {
		String input = "2018-08-07T09:29:35";
		String expect = "2018:08:07 09:29:35";
		assertEquals(expect, PhotoUtils.isoDateToExif(input));
	}

	@Test
	void exifDateToIso() {
		String input = "2018:08:07 14:29:35";
		String expect = "2018-08-07T14:29:35";
		String actual = PhotoUtils.exifDateToIso(input);
		assertEquals(expect, actual);
		LocalDateTime.parse(actual); // Throws if bad
	}

}
