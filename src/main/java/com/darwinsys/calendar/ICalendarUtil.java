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
	 * XXX Some of this is a bit specific to older software;
	 * may need to clean up according to current iCal/vCal standard?
	 * @param pw PrintWriter to output the event to
	 * @param evt The event itself
	 */
	public static void writeEvent(PrintWriter pw, CalendarEvent evt) {
		pw.println("BEGIN:VCALENDAR");
		pw.println("CALSCALE:GREGORIAN");
		pw.println("VERSION:2.0");
		pw.println("METHOD:PUBLISH");
		pw.println("BEGIN:VEVENT");
		pw.printf("UID: %s%n", evt.getUuid());
		Calendar c = Calendar.getInstance();
		pw.printf("LAST-MODIFIED:%s%n", dateString(c));
		pw.printf("DTSTAMP:%s%n", dateString(c));
		pw.printf("DTSTART:%s%n", 
				dateString(evt.getYear(), evt.getMonth(), evt.getDay(), evt.getStartHour(), evt.getStartMinute()));
		pw.printf("DTEND:%s%n", 
				dateString(evt.getYear(), evt.getMonth(), evt.getDay(), evt.getEndHour(), evt.getEndMinute()));
		pw.println("TRANSP:OPAQUE");
		pw.printf("X-MICROSOFT-CDO-BUSYSTATUS: %s%n", evt.getShowStatus());
		pw.println("SEQUENCE:0");
		pw.println("DESCRIPTION: " + evt.getDescription());
		pw.println("SUMMARY: " + evt.getSummary());
		pw.println("LOCATION: " + evt.getLocation());
		pw.println("CLASS:PUBLIC");
		if (evt.getOrganizer() != null) {
			Person o = evt.getOrganizer();
			pw.printf(
			"ORGANIZER;ROLE=REQ-PARTICIPANT;CUTYPE=INDIVIDUAL;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=%s:MAILTO:%s%n",
			o.getFullName(), o.getEmail());
		}
		if (evt.getAttendees() != null) {
			for (Person attendee : evt.getAttendees()) {
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
