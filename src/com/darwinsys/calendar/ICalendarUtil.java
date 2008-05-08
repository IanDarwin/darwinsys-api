package com.darwinsys.calendar;

import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Utilities for reading/writing CalendarEvents in iCalendar.
 * All methods are static.
 */
public class ICalendarUtil {

	private ICalendarUtil() {
		// you don't need to construct objects of this class
	}

	/**
	 * Write the given event in iCalendar format to the given PrintWriter.
	 * XXX Some of this is a bit Scalix-specific; try to 
	 * clean that up according to the iCal/vCal standard.
	 */
	public static void writeEvent(PrintWriter pw, CalendarEvent a) {
		pw.println("BEGIN:VCALENDAR");
		pw.println("CALSCALE:GREGORIAN");
		pw.println("VERSION:2.0");
		pw.println("METHOD:PUBLISH");
		pw.println("BEGIN:VEVENT");
		pw.printf("UID: %s%n", a.getUuid());
		Calendar c = Calendar.getInstance();
		pw.printf("LAST-MODIFIED:%s%n", dateString(c));
		pw.printf("DTSTAMP:%s%n", dateString(c));
		pw.printf("DTSTART:%s%n", 
				dateString(a.getYear(), a.getMonth(), a.getDay(), a.getStartHour(), a.getStartMinute()));
		pw.printf("DTEND:%s%n", 
				dateString(a.getYear(), a.getMonth(), a.getDay(), a.getEndHour(), a.getEndMinute()));
		pw.println("TRANSP:OPAQUE");
		pw.printf("X-MICROSOFT-CDO-BUSYSTATUS: %s%n", a.getShowStatus());
		pw.println("SEQUENCE:0");
		pw.println("DESCRIPTION: " + a.getDescription());
		pw.println("SUMMARY: " + a.getSummary());
		pw.println("LOCATION: " + a.getLocation());
		pw.println("CLASS:PUBLIC");
		if (a.getOrganizer() != null) {
			Person o = a.getOrganizer();
			pw.printf(
			"ORGANIZER;ROLE=REQ-PARTICIPANT;CUTYPE=INDIVIDUAL;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=%s:MAILTO:%s%n",
			o.getFullName(), o.getEmail());
		}
		if (a.getAttendees() != null) {
			for (Person attendee : a.getAttendees()) {
				pw.printf(
				"ATTENDEE;ROLE=REQ-PARTICIPANT;CUTYPE=INDIVIDUAL;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=%s:MAILTO:%s%n",
				attendee.getFullName(), attendee.getEmail());
			}
		}
		pw.println("END:VEVENT");
		pw.println("END:VCALENDAR");
	}

	public static String dateString(int year, int month, int day, int hour,
			int minute) {
		return String.format("%04d%02d%02dT%02d%02d00", year, month, day,
				hour, minute);
	}
	
	public static String dateString(Calendar d) {
		return dateString(d.get(Calendar.YEAR), 
				d.get(Calendar.MONTH) + 1, 
				d.get(Calendar.DAY_OF_MONTH), 
				d.get(Calendar.HOUR_OF_DAY), 
				d.get(Calendar.MINUTE));
	}
}
