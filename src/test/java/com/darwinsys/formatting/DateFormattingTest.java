package com.darwinsys.formatting;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateFormattingTest {

	@ParameterizedTest(name = "{index} String {0} should give {1}")
		@CsvSource(delimiter='/', value={
				"P12Y1M1D/12 years, 1 month, 1 day",
				"P12Y0M1D/12 years, no months, 1 day",
				"P12Y0M0D/12 years, no months, no days",
				"P2M2D/2 months, 2 days",
				"P2M0D/2 months, no days",
				"P1M2D/1 month, 2 days",
				"P0Y0M0D/no days",
				"P0D/no days",
		})
	public void testPeriodToString(String period, String readable) {
		assertEquals(readable, DateFormatting.periodToString(Period.parse(period)));
	}
}
