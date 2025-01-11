package com.darwinsys.geo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CountryTest {

	@Test
	void getLongName() {
		assertEquals("Canada", Country.CA.toString());
	}

	@Test
	void testToString() {
		assertEquals("Canada", Country.CA.toString());
	}

	@Test
	void isCanadaOrUSA() {
		assertTrue(Country.CA.isCanadaOrUSA());
		assertTrue(Country.US.isCanadaOrUSA());
		assertFalse(Country.GB.isCanadaOrUSA());
	}

}
