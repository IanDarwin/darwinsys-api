package com.darwinsys.calendar;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test RecurringEventDatePicker, using dates in May 2010.
 * <pre>
 * ian:31$ cal 5 2010
 *       May 2010
 * Su Mo Tu We Th Fr Sa
 *                    1
 *  2  3  4  5  6  7  8
 *  9 10 11 12 13 14 15
 * 16 17 18 19 20 21 22
 * 23 24 25 26 27 28 29
 * 30 31
 * ian:32$
 * </pre>
 * @author Ian Darwin, http://www.darwinsys.com/ 
 */
public class RecurringEventDatePickerTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void testLowWeekOfMonth() {
		new RecurringEventDatePicker(0, Calendar.SATURDAY);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testHighWeekOfMonth() {
		new RecurringEventDatePicker(5, Calendar.SATURDAY);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testLowDayOfWeek() {
		new RecurringEventDatePicker(0, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testHighDayOfWeek() {
		new RecurringEventDatePicker(0, 7);
	}
	
	@Test 
	public void testFirstSunday() {
		RecurringEventDatePicker mp = new RecurringEventDatePicker(1, Calendar.SUNDAY);
		mp.today = makeDate(2010, 05, 01);
		Calendar c = mp.getEventDate(0);
		assertEquals(2010, c.get(Calendar.YEAR));
		assertEquals(Calendar.MAY, c.get(Calendar.MONTH));
		assertEquals(2, c.get(Calendar.DAY_OF_MONTH));
	}
	
	@Test 
	public void testThirdMonday() {
		RecurringEventDatePicker mp = new RecurringEventDatePicker(3, Calendar.MONDAY);
		mp.today = makeDate(2010, 05, 05);
		Calendar c = mp.getEventDate(0);
		assertEquals(2010, c.get(Calendar.YEAR));
		assertEquals(Calendar.MAY, c.get(Calendar.MONTH));
		assertEquals(17, c.get(Calendar.DAY_OF_MONTH));
	}
	
	@SuppressWarnings("deprecation")
	private Date makeDate(int y, int m, int d) {
		return new Date(y-1900, m - 1, d);
	}
}
