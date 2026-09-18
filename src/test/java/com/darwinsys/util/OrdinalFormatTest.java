package com.darwinsys.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.FieldPosition;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdinalFormatTest {

	OrdinalFormat target;

	@BeforeEach
	public void init() {
		target = new OrdinalFormat();
	}

	/*
	 * Class under test for StringBuffer format(int, StringBuffer, FieldPosition)
	 */
	@ParameterizedTest(name = "Input {0} should return {1}")
	@CsvSource("""
		1,  '1st'
		2,  '2nd'
		10, '10th'
		11, '11th'
		""")
	public void formatLongStringBufferFieldPositionTest(long num, String exp) {
		StringBuffer sb = new StringBuffer();
		FieldPosition fp = new FieldPosition(0);
		target.format(1, sb,fp);
		assertEquals("1st", sb.toString());
	}

	/*
	 * Class under test for StringBuffer format(long, StringBuffer, FieldPosition)
	 */
	@Test
	public void formatlongStringBufferFieldPosition() {
	}

	/*
	 * Class under test for StringBuffer format(double, StringBuffer, FieldPosition)
	 */
	@Test
	public void formatdoubleStringBufferFieldPosition() {
	}

	/*
	 * Class under test for Number parse(String, ParsePosition)
	 */
	@Test
	public void parseStringParsePosition() {
	}
}
/*

	@ParameterizedTest(name = "Year {0} should declare Easter as {1}")
	@CsvSource(textBlock = """
		1900,'1900-04-15'
		1951,'1951-03-25'
		1977,'1977-04-10'
		2000,'2000-04-23'
		2001,'2001-04-15'
		2020,'2020-04-12'
		2023,'2023-04-09'
		""")
	void convert(int year, String expected) {
		assertEquals(expected, Easter.findHolyDay(year).toString());
	}
}

 */