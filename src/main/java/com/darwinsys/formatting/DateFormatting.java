package com.darwinsys.formatting;

import java.time.Period;

public class DateFormatting {

	final static String NO_NAME = "no";

	public static String periodToString(Period p) {
		StringBuilder sb = new StringBuilder();
		int yy = p.getYears();
		if (yy > 0) {
			sb.append(yy).append(' ').append(yearName(yy)).append(", ");
		}
		int months = p.getMonths();
		if (months > 0) {
			sb.append(months).append(' ').append(monthName(months)).append(", ");
		} else if (sb.length() > 0) { // e.g., "P12Y0M1D"
			sb.append(NO_NAME).append(' ').append(monthName(months)).append(", ");
		}
		int days = p.getDays();
		if (days > 0) {
			sb.append(days).append(' ').append(dayName(days));
		} else { // e.g., "P1M0D"
			sb.append(NO_NAME).append(' ').append(dayName(days));
		}
		return sb.toString();
	}

	private static String dayName(int days) {
		return days == 1 ? "day" : "days";
	}

	private static String monthName(int months) {
		return months == 1 ? "month" : "months";
	}

	private static String yearName(int yy) {
		return yy == 1 ? "year" : "years";
	}
}
