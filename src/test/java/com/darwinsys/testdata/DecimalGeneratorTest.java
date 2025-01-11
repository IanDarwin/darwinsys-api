package com.darwinsys.testdata;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DecimalGeneratorTest {

	Generator<BigDecimal> subject;

	@BeforeEach
	void setup() {
		subject = new DecimalNumberGenerator(3, 2);
	}

	@Test
	void one() {
		BigDecimal actual = subject.nextValue();
		assertTrue(actual.doubleValue() < 999);
		assertTrue(actual.doubleValue() > -999);
	}
}

	
