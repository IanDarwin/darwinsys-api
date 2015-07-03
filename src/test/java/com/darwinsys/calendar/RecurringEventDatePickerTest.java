package com.darwinsys.calendar;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

import org.junit.Test;

/**
 * Test RecurringEventDatePicker, using dates in May 2010.
 * <pre>
 * $ cal 5 2010
 *       May 2010
 * Su Mo Tu We Th Fr Sa
 *                    1
 *  2  3  4  5  6  7  8
 *  9 10 11 12 13 14 15
 * 16 17 18 19 20 21 22
 * 23 24 25 26 27 28 29
 * 30 31
 * $ cal 6 2010
 *      June 2010
 * Su Mo Tu We Th Fr Sa
 *        1  2  3  4  5
 *  6  7  8  9 10 11 12
 * 13 14 15 16 17 18 19
 * 20 21 22 23 24 25 26
 * 27 28 29 30
 * </pre>
 * @author Ian Darwin, http://www.darwinsys.com/ 
 */
public class RecurringEventDatePickerTest {
	
	// Test constructor error handling first
	@Test(expected=IllegalArgumentException.class)
	public void testLowWeekOfMonth() {
		new RecurringEventDatePicker(0, DayOfWeek.SATURDAY);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testHighWeekOfMonth() {
		new RecurringEventDatePicker(6, DayOfWeek.SATURDAY);
	}
	
	@Test 
	public void testFirstSunday() {
		RecurringEventDatePicker mp = 
			new RecurringEventDatePicker(1, DayOfWeek.SUNDAY);
		mp.now = LocalDate.of(2010, 05, 01);
		LocalDate c = mp.getEventLocalDate(0);
		System.out.printf("%s => %s%n", mp.now, c);
		assertEquals(LocalDate.of(2010,5,2), c);
	}
	
	@Test 
	public void testThirdMonday() {
		RecurringEventDatePicker mp = 
			new RecurringEventDatePicker(3, DayOfWeek.MONDAY);
		mp.now = LocalDate.of(2010, 05, 05);
		LocalDate c = mp.getEventLocalDate(0);
		assertEquals(LocalDate.of(2010, 5, 17), c);
		LocalDate d = mp.getEventLocalDate(1);
		assertEquals(LocalDate.of(2010, 6, 21), d);
	}
	
	@Test
	public void testLegacy() {
		RecurringEventDatePicker mp = 
			new RecurringEventDatePicker(3, DayOfWeek.MONDAY);
		mp.now = LocalDate.of(2010, 05, 05);
		@SuppressWarnings("deprecation")
		Calendar c = mp.getEventDate(0);
		assertEquals(2010, c.get(Calendar.YEAR));
		assertEquals(Calendar.MAY, c.get(Calendar.MONTH));
		assertEquals(17, c.get(Calendar.DAY_OF_MONTH));
	}
}
