package com.darwinsys.testing;

import java.util.GregorianCalendar;

public class CheckAccessorsDemo {
	public static void main(String[] args) throws Exception {
		Class<?> c = GregorianCalendar.class;
		System.out.printf("Testing the %s class' accessors%n", c);
		CheckAccessors.process(c);
	}
}
