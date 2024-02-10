package com.darwinsys.calendar;

import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * CalendarEvent is a Java 14+ "record" type referring to one event in a calendar.
 * This is not a java.util.Calendar but is application-defined.
 * The list of arguments is long, but there are some factory methods
 * to simplify construction where not all fields are wanted.
 * @param summary Short Description of the event
 * @param description Longer Description of the event
 * @param eventType An EventType enum for this event
 * @param uuid A UUID, can be had from makeUUID()
 * @param startDate The date or begin date
 * @param startTime The beginning time of a partial-day event
 * @param endDate The date or beginning date
 * @param endTime The beginning time of a partial-day event
 * @param organizerName The name of the event organizer (person or organization)
 * @param organizerEmail The contact email for this event
 * @param location - where the event will take place
 * @param calName - the calendar to which this event belongs
 */
public record CalendarEvent (
	String summary,
	Optional<String> description,
	EventType eventType,
	UUID uuid,
	LocalDate startDate,
	Optional<LocalTime>startTime,
	Optional<LocalDate> endDate,
	Optional<LocalTime>endTime,
	Optional<String> organizerName,
	Optional<String> organizerEmail,
	Optional<String> location,
	Optional<String> calName) {

	static String defaultCalendar = "work";
	
	public static void setDefaultCalendar(String calName) {
		defaultCalendar = calName;
	}

	/** Present a bit of the event info for debugging */
	public String toString() {
		return "CalendarEvent: " + summary + " starting " + startDate;
	}
	final static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd'T'kkmm'00Z'");

	/**
	 * Print the event with any extra headers to a writer.
	 * @param out The open PrintWriter to use
	 * @param wrap True to wrap the event in minimal VCALENDAR infra
	 * @param extras A Map of extra headers to append.
	 */
	public void toVCalEvent(PrintWriter out,
			boolean wrap, 
			Map<String,String> extras) {

		if (wrap) {
			putVCalHeader(out);
		}
		out.println("BEGIN:VEVENT");
		out.println("CREATED:" + df.format(LocalDateTime.now()));
		out.println("SUMMARY:" + summary);
		description.ifPresent(s->out.println("DESCRIPTION:"+s));
		location.ifPresent(s->out.println("LOCATION:"+s));

		LocalTime startLocalTime = startTime().isPresent() ? startTime.get() : LocalTime.of(0,0);
		LocalDateTime startDateTime = LocalDateTime.of(startDate, startLocalTime);
		String dtStart = df.format(startDateTime);
		out.println("DTSTART:" + dtStart);

		var endLocalDate = endDate.isPresent() ? endDate.get() : startDate;
		LocalTime endLocalTime = endTime().isPresent() ? endTime.get() : LocalTime.of(0,0);
		LocalDateTime endDateTime = LocalDateTime.of(endLocalDate, endLocalTime);
		String dtEnd = df.format(endDateTime);
		out.println("DTEND:" + dtEnd);

		out.println("SEQUENCE:0");
		out.printf("UID: %s\n", uuid);
		if (organizerName.isPresent() && organizerEmail.isPresent()) {
			out.printf("ORGANIZER;CN=%s:MAILTO:%s\n", organizerName.get(), organizerEmail.get());
		}
		if (calName.isPresent()) {
			out.println("X-WR-CALNAME;VALUE=TEXT:" + calName.get());
		} else {
			out.println("X-WR-CALNAME;VALUE=TEXT:" + defaultCalendar);
		}
		if (extras != null)
			extras.forEach((k,v)-> out.println(k.toUpperCase() + ":" + v));
		out.println("END:VEVENT");
		if (wrap) {
			putVCalTrailer(out);
		}
	}

	/**
	 * Print the event out in full to a writer, in VCALENDAR VEVENT format.
	 * @param out The open PrintWriter to use
	 * @param wrap True to wrap the event in minimal VCALENDAR infra
	 */
	public void toVCalEvent(PrintWriter out, boolean wrap) {
		toVCalEvent(out, wrap, null);
	}

	public static void putVCalTrailer(PrintWriter out) {
		out.println("END:VCALENDAR");
	}

	public static void putVCalHeader(PrintWriter out) {
		out.println("BEGIN:VCALENDAR");
		out.println("VERSION:2.0");
		out.println("X-WR-TIMEZONE;VALUE=TEXT:Canada/Eastern");
		out.println("METHOD:PUBLISH");
		out.println("PRODID:-//Darwin Open Systems//c.d.calendar.CalendarEvent 1.0//EN");

	}

	// STATIC UTILITY METHODS

	public static UUID makeUUID() {
		return UUID.randomUUID();
	}

	// Convenience constructors, for a form of compatibility with previous edition of this class

	/** @return An all-day event for one day only
	 * @param description The text of the event
	 * @param summary Short description
	 * @param location Where the event is
	 * @param year The year
	 * @param month The Month
	 * @param day The day
	 */
	public static CalendarEvent newCalendarEvent(String description, 
			String summary, String location,
			int year, int month, int day) {
		
		// this(EventType.ALLDAY, description, summary, location, year, month, day, 0, 0, 0, 0);
		return new CalendarEvent(
				summary,
				Optional.of(description),
				EventType.ALLDAY,
				CalendarEvent.makeUUID(),
				LocalDate.of(year, month, day), Optional.of(LocalTime.of(0,0)),
				Optional.of(LocalDate.of(year, month, day)), Optional.empty(),
				Optional.empty(), Optional.empty(), // Organizer name, email
				Optional.of(location), 
				Optional.empty()			// Calendar name
				);
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
		
		return new CalendarEvent(
				summary,
				Optional.of(description),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(year, month, day), Optional.of(LocalTime.of(startHour, 0)),
				Optional.empty(), Optional.of(LocalTime.of(endHour, 0)),
				Optional.empty(), Optional.empty(),
				Optional.of(location),
				Optional.empty());
		
	}
	
	/** @return A single-day appointment, having start and end hours AND minutes
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
			int startHour, int startMinute, int endHour, int endMinute) {
		
		return new CalendarEvent(
				summary,
				Optional.of(description),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(year, month, day),
				Optional.of(LocalTime.of(startHour, startMinute)),
				Optional.empty(), Optional.of(LocalTime.of(endHour, endMinute)),
				Optional.empty(), Optional.empty(),
				Optional.of(location),
				Optional.empty());
		
	}

	/**
	 * @return a single-day appointment
	 * @param description The text of the event  (optional)
	 * @param summary Short description
	 * @param location Where the event is (optional)
	 * @param startDt Starting date and time
	 * @param endDt Ending date (should be same as startDt) and time
	 */
	public static CalendarEvent newCalendarEvent(String description, String summary,
		String location, LocalDateTime startDt, LocalDateTime endDt) {
		return new CalendarEvent(
				summary,
				Optional.ofNullable(description),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				startDt.toLocalDate(), 
				Optional.of(startDt.toLocalTime()),
				Optional.empty(),
				Optional.of(endDt.toLocalTime()),
				Optional.empty(),
				Optional.empty(),
				Optional.ofNullable(location),
				Optional.empty());
	}
}
