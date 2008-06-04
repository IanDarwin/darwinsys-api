package com.darwinsys.testdata;

public class GenTestData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Generator g = new DecimalNumberGenerator(10, 2);
		Object[] data = g.nextValues(123);
		for (Object o : data) {
			System.out.println(o);
		}
	}

}
