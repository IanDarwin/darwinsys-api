package com.darwinsys.html;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test SimpleTagValidator
 */
class SimpleTagValidatorTest {

	SimpleTagValidator val = new SimpleTagValidator();

	@Test
	void validate() {
		assertTrue(val.validate(""), "empty string");
		
		assertTrue(val.validate("< a href='#foo'>Foo</a>"), "leading space");

		assertTrue(val.validate("<p><a href='foo'>Link</a><i></p>?"),
			"variety of tags");
	}

	@Test
	void validateBadInput() {
		System.out.println("Valid tags: '" + val.getTagsAsString(false) + "'");
		assertThrows(NullPointerException.class, () ->
			val.validate(null));
	}

	@Test
	void tagsAsStrings() {
		String[] myTags = {"br", "i", "img" };
		assertEquals("br, i, img",
			new SimpleTagValidator(myTags).getTagsAsString(true),
			"tagsAsStrings");
		
		assertEquals("br i img",
				new SimpleTagValidator(myTags).getTagsAsString(false),
				"tagsAsStrings");
	}

	@Test
	void validateFailures() {
		assertFalse(val.validate("<img size=0>"), "imgtag");
		assertEquals("img", val.getFailedTag(), "get bad tag info");
	}

}
