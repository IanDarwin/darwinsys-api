package com.darwinsys.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
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

	CalendarEvent subject = createFullEvent();

	private CalendarEvent createFullEvent() {
		return new CalendarEvent(
			"Happy 200th birthday to Canada!",
			Optional.of("A longer description will be here someday soon"),
			EventType.APPOINTMENT,
			CalendarEvent.makeUUID(),
			LocalDate.of(2067, 6, 1), Optional.of(LocalTime.of(12,0)),
			Optional.empty(), Optional.empty(), // No end-date/time
			Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
			Optional.of("Across Canada"),
			Optional.empty());
	}
	
	private String getSubjectAsString() throws IOException {
		subject.toVCalEvent(pout, false);
		String result = getResult();
		return result;
	}

	@Test
	public void testLongForm() throws IOException {
		String result = getSubjectAsString();
		assertEquals(subject.summary(), "Happy 200th birthday to Canada!");
		assertSame(subject.eventType(), EventType.APPOINTMENT);
		LocalDate startDate = LocalDate.of(2067, 6, 1);
		assertTrue(result.contains("DTSTART;VALUE=DATE:" + startDate));
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
