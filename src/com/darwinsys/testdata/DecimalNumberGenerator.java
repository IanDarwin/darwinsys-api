package com.darwinsys.testdata;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class DecimalNumberGenerator implements Generator {
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
	
	public Object nextValue() {
		int intPart = r.nextInt(maxIntPart);
		int fracPart = r.nextInt(maxFracPart);
		return new String(intPart + "." + fracPart);
	}

	public Object[] nextValues(int n) {
		Object[] result = new Object[n];
		for (int i = 0; i < n; i++) {
			result[i] = nextValue();
		}
		return result;
	}
}
