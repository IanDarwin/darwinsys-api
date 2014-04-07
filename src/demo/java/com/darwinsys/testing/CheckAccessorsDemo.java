package com.darwinsys.testing;

import java.util.Calendar;

public class CheckAccessorsDemo {
	public static void main(String[] args) throws Exception {
		Class<?> c = Calendar.getInstance().getClass();
		System.out.printf("Testing the %s class' accessors%n", c);
		CheckAccessors.process(c);
	}
}
