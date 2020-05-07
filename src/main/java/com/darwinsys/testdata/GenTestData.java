package com.darwinsys.testdata;

import java.math.BigDecimal;

public class GenTestData {

	public static void main(String[] args) {
		Generator<BigDecimal> g = new DecimalNumberGenerator(10, 2);
		Object[] data = g.nextValues(123);
		for (Object o : data) {
			System.out.println(o);
		}
	}

}
