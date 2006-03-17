package com.darwinsys.util;

public class DateUtils {

	/** The days in each month. */
	public static final int dom[] = {
	31, 28, 31, 30, /* jan feb mar apr */
	31, 30, 31, 31, /* may jun jul aug */
	30, 31, 30, 31 /* sep oct nov dec */
	};

	/** The names of the months */
	public static final String[] months = {
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December"
	};
	
	/**
	 * Convert a month name (english names) (or a valid abbreviation) to an int in {1,12}.
	 * Warning: the current implementation uses startsWith(), so month("J") returns 1.
	 * @param monthName
	 * @return
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
}
