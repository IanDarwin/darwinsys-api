package com.darwinsys.util;

public class DateUtils {

	/** The days in each month. */
	private static final int dom[] = {
	31, 28, 31, 30, /* jan feb mar apr */
	31, 30, 31, 31, /* may jun jul aug */
	30, 31, 30, 31 /* sep oct nov dec */
	};

	/** The names of the months */
	private static final String[] months = {
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December"
	};
	
	/**
	 * Convert a month name (english names) (or a valid abbreviation) to an int in {1,12}.
	 * Warning: the current implementation uses startsWith(), so month("J") returns 1.
	 * @param monthName
	 * @return the month number, in the range 1-12.
	 */
	public static int month(String monthName) {
		for (int i = 0; i < months.length; i++) {
			String m = months[i];		
			if (m.startsWith(monthName)) {
				return i + 1;
			}
		}
		return -1;
	}
	
	public static String getMonthName(int monthNumber) {
		return months[monthNumber];
	}
	
	public int[] getDaysOfMonths() {
		return dom.clone();
	}
}
