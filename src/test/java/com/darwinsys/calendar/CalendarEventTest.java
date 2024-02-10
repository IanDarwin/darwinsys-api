package com.darwinsys.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class CalendarEventTest {
	
	StringWriter sout;
	PrintWriter pout;
	
	@Before
	public void makePout() {
		sout = new StringWriter();
		pout = new PrintWriter(sout);
	}

	protected String getResult() throws IOException {
		pout.close();
		// sout.close(); // done implicitly
		String result = sout.getBuffer().toString();
		return result;
	}

	CalendarEvent subject;

	
	private String getSubjectAsString() throws IOException {
		subject.toVCalEvent(pout, false);
		String result = getResult();
		return result;
	}

	public void testLongFormWithStartDateOnly() throws IOException {
		subject = new CalendarEvent(
				"Happy 200th birthday to Canada!",
				Optional.of("A longer description will be here someday soon"),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(2067, 6, 1), Optional.empty(),
				Optional.empty(), Optional.empty(), // No end-date/time
				Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
				Optional.of("Across Canada"),
				Optional.empty());
		String result = getSubjectAsString();
		assertEquals(subject.summary(), "Happy 200th birthday to Canada!");
		assertSame(subject.eventType(), EventType.APPOINTMENT);
		String startDate = "20670601T000000Z\n";
		assertTrue(result.contains("DTSTART:" + startDate));
	}
	
	@Test
	public void testLongFormWithStartDateTime() throws IOException {
		subject = new CalendarEvent(
				"Happy 200th birthday to Canada!",
				Optional.of("A longer description will be here someday soon"),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(2067, 6, 1), Optional.of(LocalTime.of(12,0)),
				Optional.empty(), Optional.empty(), // No end-date/time
				Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
				Optional.of("Across Canada"),
				Optional.empty());
		String result = getSubjectAsString();
		assertEquals(subject.summary(), "Happy 200th birthday to Canada!");
		assertSame(subject.eventType(), EventType.APPOINTMENT);
		String startDate = "20670601T120000Z";
		assertTrue(result.contains("DTSTART:" + startDate));
	}
	
	@Test
	public void testLongFormWithExtrasMap() throws IOException {
		Map<String,String> extras = Map.of("holodeck", "Planet 10");
		subject = new CalendarEvent(
				"Summary",
				Optional.of("Description"),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(2067, 6, 1), Optional.of(LocalTime.of(12,0)),
				Optional.empty(), Optional.empty(), // No end-date/time
				Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
				Optional.of("Across Canada"),
				Optional.empty());
		// Can't use getSubjectAsString here
		subject.toVCalEvent(pout, false, extras);
		String result = getResult();
		assertTrue(result.contains("HOLODECK:Planet 10"));
	}
	
	@Test
	public void testLongFormWithStartDateTimeEndTime() throws IOException {
		subject = new CalendarEvent(
				"A 200th Birthday Party for Canada!",
				Optional.of("A longer description will be here someday soon"),
				EventType.APPOINTMENT,
				CalendarEvent.makeUUID(),
				LocalDate.of(2067, 6, 1), Optional.of(LocalTime.of(12,0)),
				Optional.empty(), Optional.of(LocalTime.of(13,00)),
				Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
				Optional.of("Across Canada"),
				Optional.empty());
		String result = getSubjectAsString();
		assertEquals(subject.summary(), "A 200th Birthday Party for Canada!");
		assertSame(subject.eventType(), EventType.APPOINTMENT);
		String startDate = "20670601T120000Z";
		assertTrue(result.contains("DTSTART:" + startDate));
		String endDate = "20670601T130000Z";
		System.out.println("result = " + result);
		assertTrue(result.contains("DTEND:" + endDate));
	}
	
	@Test
	public void testNewCalendarEvent1() throws IOException {
		subject = CalendarEvent.newCalendarEvent("A Description", 
			"A summary", "Someplace", 2020, 02, 20);
		assertEquals(subject.summary(), "A summary");
		assertEquals(subject.description().get(), "A Description");
		assertEquals(subject.location().get(), "Someplace");
		assertSame(subject.eventType(), EventType.ALLDAY);
		String result = getSubjectAsString();
		assertTrue(result.contains("SUMMARY:A summary"));
		assertTrue(result.contains("LOCATION:Someplace"));
	}

}
