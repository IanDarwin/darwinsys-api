package com.darwinsys.calendar;

import java.util.Date;
import java.util.Calendar;

/**
 * Pick the date that a recurring event will fall on, e.g.,
 * gtabug.ca currently meets on the Third Wednesday of each month:
 * <pre>
 * RecurringEventDatePicker mp = new RecurringEventDatePicker(3, Calendar.WEDNESDAY);
 * Calendar nextMeeting = mp.getNextMeeting(0); // next
 * </pre>
 * In a JSP you might use:
 * <pre>
 * RecurringEventDatePicker mp = new RecurringEventDatePicker(3, Calendar.WEDNESDAY);
 * DateFormat dfm = new SimpleDateFormat("MMMM dd, yyyy");
 * out.println("*" + dfm.format(mp.getEventDate(0));
 * out.println("*" + dfm.format(mp.getEventDate(1));
 * out.println("*" + dfm.format(mp.getEventDate(2)); 
 * </pre>
 * @author Original code by Derek Marcotte.
 * @author Improvements and JUnit tests by Ian Darwin
 */
public class RecurringEventDatePicker {
	
	private int dayOfWeek = Calendar.WEDNESDAY;
	
	private int weekOfMonth = 3;
	
	/** package protected only for testing, not for general use */
	Date today = new Date();

	/**
	 * Construct a RecurringEventDatePicker
	 * @param weekOfMonth
	 * @param dayOfWeek
	 */
	public RecurringEventDatePicker(int weekOfMonth, int dayOfWeek) {
		super();
		if (weekOfMonth < 1 || weekOfMonth > 5) {
			throw new IllegalArgumentException("weekOfMonth must be in 1..5");
		}
		if (dayOfWeek < 0 || dayOfWeek > 6) {
			throw new IllegalArgumentException("weekOfMonth must be in 0..6");
		}
		this.weekOfMonth = weekOfMonth;
		this.dayOfWeek = dayOfWeek;
	}

	public Calendar getEventDate(int meetingsAway) {
		
		// start from today
		Calendar thisMeeting = Calendar.getInstance();
		thisMeeting.setTime(today);
		thisMeeting.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		thisMeeting.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);		
		
		// has the meeting already happened this month
		if (thisMeeting.getTime().compareTo(today) < 0 ) {
			// start from next month
			meetingsAway++;
		}
		
		if (meetingsAway > 0) {
			thisMeeting.add(Calendar.MONTH, meetingsAway);
			thisMeeting.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			thisMeeting.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
		}
		
		return thisMeeting;
	}
}
