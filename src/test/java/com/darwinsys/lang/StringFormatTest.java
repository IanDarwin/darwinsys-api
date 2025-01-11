package com.darwinsys.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringFormatTest {
	String mesg = "JavaFun";

	@Test
	void left() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_LEFT).format(mesg));
		assertEquals("JavaFun   ",
			new StringFormat(10, StringFormat.JUST_LEFT).format(mesg));
	}

	@Test
	void centre() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_CENTER).format(mesg));
		assertEquals(" JavaFun  ",
			new StringFormat(10, StringFormat.JUST_CENTER).format(mesg));
	}

	@Test
	void right() {
		assertEquals("JavaF",
			new StringFormat(5, StringFormat.JUST_RIGHT).format(mesg));
		assertEquals("   JavaFun",
			new StringFormat(10, StringFormat.JUST_RIGHT).format(mesg));
	}
}
