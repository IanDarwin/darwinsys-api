package com.darwinsys.util;

import java.time.LocalDate;
import java.util.Calendar;


/**
 * DateSimple represents YYYY-MM-DD without all the overhead and
 * deprecated baggage of java.util.Date.
 * Objects of this type are immutable.
 * Re-implemented in terms of java.time.LocalDate.
 */
public class DateSimple {
	final LocalDate date;

	/**
	 * Construct a DateSimple with the given values
	 * @param year The year
	 * @param month The Month
	 * @param day The Day of Month
	 */
	public DateSimple(int year, int month, int day) {
		super();
		date = LocalDate.of(year, month, day);
	}

	/** Construct a DateSimple for today */
	public DateSimple() {
		date = LocalDate.now();
	}

	public int getDay() {
		return date.getDayOfMonth();
	}

	public int getMonth() {
		return date.getMonthValue();
	}

	public int getYear() {
		return date.getYear();
	}

	/**
	 * Return a Calendar object; no longer supported
	 * @return Never
	 * @throws UnsupportedOperationException always. Avoid using Calendar.
	 */
	public Calendar getCalendar() {
		throw new UnsupportedOperationException("Calendar is dead, Jim");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DateSimple)) {
			return false;
		}
		return date.equals((DateSimple) obj);
	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}
}
