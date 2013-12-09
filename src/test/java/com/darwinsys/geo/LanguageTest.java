package com.darwinsys.geo;

import static org.junit.Assert.*;

import org.junit.Test;

public class LanguageTest {

	@Test
	public void testToString() {
		assertEquals("English", Language.en.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetName() {
		Language.en.setName("Malarkey");
	}

	@Test
	public void testGetName() {
		assertEquals("English", Language.en.getName());
	}

	@Test
	public void testValueOfIgnoreCase() {
		assertSame(Language.en, Language.valueOfIgnoreCase("english"));
	}

}
