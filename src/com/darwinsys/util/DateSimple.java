package com.darwinsys.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * DateSimple represents YYYY-MM-DD without all the overhead and
 * deprecated baggage of java.util.Date.
 * Objects of this type are immutable.
 */

public class DateSimple {
	int year;
	int month;
	int day;

	/**
	 * @param year
	 * @param month
	 * @param day
	 */
	public DateSimple(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public DateSimple() {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month  = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public Calendar getCalendar() {
		return new GregorianCalendar(year, month, day);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DateSimple)) {
			return false;
		}
		DateSimple d = (DateSimple) obj;
		return year == d.year && month == d.month && day == d.day;
	}

	@Override
	public int hashCode() {
		return year<<16 | month << 8 | day;
	}
}
