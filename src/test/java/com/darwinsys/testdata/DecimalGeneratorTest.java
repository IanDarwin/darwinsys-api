package com.darwinsys.testdata;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DecimalGeneratorTest {

	Generator<BigDecimal> subject;

	@Before
	public void setup() {
		subject = new DecimalNumberGenerator(3, 2);
	}

	@Test
	public void testOne() {
		BigDecimal actual = subject.nextValue();
		assertTrue(actual.doubleValue() < 999);
		assertTrue(actual.doubleValue() > -999);
	}
}

	
