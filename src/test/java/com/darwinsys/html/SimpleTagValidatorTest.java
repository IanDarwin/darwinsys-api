package com.darwinsys.html;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test SimpleTagValidator
 */
public class SimpleTagValidatorTest {

	SimpleTagValidator val = new SimpleTagValidator();
	
	@Test
	public void testValidate() {
		assertTrue("empty string", val.validate(""));
		
		assertTrue("leading space", val.validate("< a href='#foo'>Foo</a>"));

		assertTrue("variety of tags",
			val.validate("<p><a href='foo'>Link</a><i></p>?"));
	}
	
	@Test(expected=NullPointerException.class)
	public void testValidateBadInput() {
		System.out.println("Valid tags: '" + val.getTagsAsString(false) + "'");
		val.validate(null);
	}

	@Test
	public void testTagsAsStrings() {
		String[] myTags = {"br", "i", "img" };
		assertEquals("tagsAsStrings", "br, i, img",
			new SimpleTagValidator(myTags).getTagsAsString(true));
		
		assertEquals("tagsAsStrings", "br i img",
				new SimpleTagValidator(myTags).getTagsAsString(false));
	}

	@Test
	public void testValidateFailures() {
		assertFalse("imgtag", val.validate("<img size=0>"));
		assertEquals("get bad tag info", "img", val.getFailedTag());
	}

}
