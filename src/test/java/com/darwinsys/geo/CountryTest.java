package com.darwinsys.geo;

import static org.junit.Assert.*;

import org.junit.Test;

public class CountryTest {

	@Test
	public void testGetLongName() {
		assertEquals("Canada", Country.CA.toString());
	}

	@Test
	public void testToString() {
		assertEquals("Canada", Country.CA.toString());
	}

	@Test
	public void testIsCanadaOrUSA() {
		assertTrue(Country.CA.isCanadaOrUSA());
		assertTrue(Country.US.isCanadaOrUSA());
		assertFalse(Country.GB.isCanadaOrUSA());
	}

}
