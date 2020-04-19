package com.darwinsys.testdata;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class DecimalNumberGenerator implements Generator<BigDecimal> {
	int maxIntPart = 1;
	int maxFracPart = 1;	
	Random r;
	NumberFormat fmt;
	
	public DecimalNumberGenerator(int width, int precision) {
		for (int i = 0; i < width-precision-1; i++)
			this.maxIntPart *= 10;
		for (int i = 0; i < precision; i++)
			this.maxFracPart *= 10;
		fmt = new DecimalFormat();
		fmt.setMaximumIntegerDigits(width - precision - 1);
		fmt.setMaximumFractionDigits(precision);
		r = new Random();
	}
	
	public BigDecimal nextValue() {
		int intPart = r.nextInt(maxIntPart);
		int fracPart = r.nextInt(maxFracPart);
		return new BigDecimal(new String(intPart + "." + fracPart));
	}

	public BigDecimal[] nextValues(int n) {
		BigDecimal[] result = new BigDecimal[n];
		for (int i = 0; i < n; i++) {
			result[i] = nextValue();
		}
		return result;
	}
}
