package com.darwinsys.geo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LanguageTest {

	@Test
	void testToString() {
		assertEquals("English", Language.en.toString());
	}

	@Test
	void setName() {
		assertThrows(IllegalArgumentException.class, () ->
			Language.en.setName("Malarkey"));
	}

	@Test
	void getName() {
		assertEquals("English", Language.en.getName());
	}

	@Test
	void valueOfIgnoreCase() {
		assertSame(Language.en, Language.valueOfIgnoreCase("english"));
	}

}
