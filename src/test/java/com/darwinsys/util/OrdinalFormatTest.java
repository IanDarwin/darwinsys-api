package com.darwinsys.util;

import org.junit.jupiter.api.Test;

class OrdinalFormatTest {

	/*
	 * Class under test for StringBuffer format(int, StringBuffer, FieldPosition)
	 */
	@Test
	void formatintStringBufferFieldPosition() {
		StringBuffer sb = new StringBuffer;
		FieldPosition fp = new FieldPosition();
		StringBuilder sb = target.format(1, sb,fp);
		assertEquals("1st", sb.toString);
	}

	/*
	 * Class under test for StringBuffer format(long, StringBuffer, FieldPosition)
	 */
	@Test
	void formatlongStringBufferFieldPosition() {
	}

	/*
	 * Class under test for StringBuffer format(double, StringBuffer, FieldPosition)
	 */
	@Test
	void formatdoubleStringBufferFieldPosition() {
	}

	/*
	 * Class under test for Number parse(String, ParsePosition)
	 */
	@Test
	void parseStringParsePosition() {
	}

}
