package com.darwinsys.numbers;

import java.text.ParseException;
import java.time.LocalDate;


public class RomanNumberSimple {
	public static void main(String[] args) throws Exception {
		// tag::main[]
		RomanNumberFormat rf = new RomanNumberFormat();
		var year = LocalDate.now().getYear();
		var yearStr = rf.format(year);
		System.out.println(year + " -> " + yearStr);
		long newYear = (Long) rf.parseObject(yearStr);
		System.out.println(yearStr + " -> " + newYear);
		if (newYear != year) {
			System.out.println("Error: non-idempotent!");
		}
		// end::main[]
	}
}
