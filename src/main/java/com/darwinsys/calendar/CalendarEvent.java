package com.darwinsys.calendar;

import java.io.PrintWriter;
import java.time.*;
import java.util.*;
import java.util.UUID;

/**
 * CalendarEvent is a Java 14 "record" type referring to one event in a calendar.
 * This is not a java.util.Calendar but is application-defined,
 * possibly a List<CalendarEvent>.
 * @param event Description of the event
 * @param evtType An EventType enum for this event
 * @param uuid A UUID, can be had from makeUUID()
 * @param startDate The date or begin date
 * @param startTime The begin time of a partial-day event
 * @param endDate The date or begin date
 * @param endTime The begin time of a partial-day event
 * @param organizerName The name of the event organizer (person or organization)
 * @param organizerEmail The contact email for this event
 * @param location - where the event will take place
 * @param calName - the calendar to which this event belongs
 */
public record CalendarEvent(String event,
	EventType evtType,
	UUID uuid, // Get from makeUUID()
	LocalDate startDate, Optional<LocalTime>startTime,
	Optional<LocalDate> endDate, Optional<LocalTime>endTime,
	Optional<String> organizerName, Optional<String> organizerEmail,
	Optional<String> location,
	Optional<String> calName) {

	static Random r = new Random();
	static int nEvent = r.nextInt() + 42;

	/** Present a bit of the event info for debugging */
	public String toString() {
		return "CalendarEvent: " + event + " starting " + startDate;
	}

	/**
	 * Print the event out in full to a writer, in VCALENDAR VEVENT format.
	 * @param out The open PrintWriter to use
	 * @param wrap True to wrap the event in minimal VCALENDAR infra
	 */
	public void toVevent(PrintWriter out, boolean wrap) {
		if (wrap) {
			out.println("BEGIN:VCALENDAR");
			out.println("CALSCALE:GREGORIAN");
			out.println("X-WR-TIMEZONE;VALUE=TEXT:Canada/Eastern");
			out.println("METHOD:PUBLISH");
			out.println("PRODID:-//Darwin Open Systems//c.d.calendar.CalendarEvent 1.0//EN");
			out.println("VERSION:2.0");
		}
		out.println("BEGIN:VEVENT");
		out.println("DTSTAMP:" + LocalDate.now());
		out.println("CREATED:" + LocalDate.now());
		out.println("SUMMARY:" + event);
		out.println("LOCATION:" + location);
		out.print("DTSTART;VALUE=DATE:" + startDate);
		if (startTime.isPresent()) {
			out.print('.' + startTime.get().toString());
			out.println();
		}
		if (endDate.isPresent()) {
			out.print("DTEND;VALUE=DATE:" + endDate.get());
			if (endTime.isPresent()) {
				out.print('.' + endTime.get().toString());
			}
			out.println();
		}
		out.println("SEQUENCE:" + nEvent++);
		out.printf("UID: %s\n", uuid);
		if (organizerName.isPresent() && organizerEmail.isPresent()) {
			out.printf("ORGANIZER;CN=%s:MAILTO:%s\n", organizerName.get(), organizerEmail.get());
		}
		if (calName.isPresent()) {
			out.println("X-WR-CALNAME;VALUE=TEXT:" + calName.get());
		}
		out.println("END:VEVENT");
		if (wrap) {
			out.println("END:VCALENDAR");
		}
	}

	// STATIC UTILITY METHODS

	public static UUID makeUUID() {
		return UUID.randomUUID();
	}

	// Convenience constructors, for a form of compatibility with previous edition of this class

	/** @return An all-day event
	 * @param description The text of the event
	 * @param summary Short description
	 * @param location Where the event is
	 * @param year The year
	 * @param month The Month
	 * @param day The day
	 */
	public static CalendarEvent newCalendarEvent(String description, String summary, String location,
			int year, int month, int day) {
		
		// this(EventType.ALLDAY, description, summary, location, year, month, day, 0, 0, 0, 0);
		
		throw new UnsupportedOperationException();
	}
	
	/** @return A single-day appointment, having start and end hours
	 * @param description The text of the event
	 * @param summary Short description
	 * @param location Where the event is
	 * @param year The year
	 * @param month The Month
	 * @param day The day
	 * @param startHour the starting time
	 * @param endHour the ending time
	 */
	public static CalendarEvent newCalendarEvent(String description, String summary, String location,
			int year, int month, int day, 
			int startHour, int endHour) {
		
		// this(EventType.APPOINTMENT, description, summary, location, year, month, day, startHour, 0, endHour, 0);
		throw new UnsupportedOperationException();
		
	}
}
