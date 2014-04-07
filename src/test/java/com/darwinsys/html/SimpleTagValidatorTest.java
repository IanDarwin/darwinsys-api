package com.darwinsys.html;

import junit.framework.TestCase;

/**
 * Test SimpleTagValidator
 */
public class SimpleTagValidatorTest extends TestCase {
	SimpleTagValidator val = new SimpleTagValidator();
	
	public void testValidate() {
		System.out.println("Valid tags: '" + val.getTagsAsString(false) + "'");
		try {
			val.validate(null);
		} catch (NullPointerException ex) {
			System.out.println(" caught expected NPE");
		}
		assertTrue("empty string", val.validate(""));
		
		assertTrue("leading space", val.validate("< a href='#foo'>Foo</a>"));

		assertTrue("variety of tags",
			val.validate("<p><a href='foo'>Link</a><i></p>?"));
	}
	
	public void testTagsAsStrings() {
		String[] myTags = {"br", "i", "img" };
		assertEquals("tagsAsStrings", "br, i, img",
			new SimpleTagValidator(myTags).getTagsAsString(true));
		
		assertEquals("tagsAsStrings", "br i img",
				new SimpleTagValidator(myTags).getTagsAsString(false));
	}
	public void testValidateFailures() {
		assertFalse("imgtag", val.validate("<img size=0>"));
		assertEquals("get bad tag info", "img", val.getFailedTag());
	}

}
