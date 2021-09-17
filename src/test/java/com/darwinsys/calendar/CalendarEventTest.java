package com.darwinsys.calendar;

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

	CalendarEvent subject = new CalendarEvent(
		"Happy 200th birthday to Canada!",
		Optional.of("A longer description will be here someday soon"),
		EventType.APPOINTMENT,
		CalendarEvent.makeUUID(),
		LocalDate.of(2067, 6, 1), Optional.of(LocalTime.of(0,0)),
		Optional.empty(), Optional.empty(),
		Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
		Optional.of("Across Canada"),
		Optional.empty());

	@Test
	public void testOne() throws IOException {
		subject.toVCalEvent(pout, false);
		String result = getResult();
		// System.out.println(result);
		LocalDate startDate = LocalDate.of(2067, 6, 1);
		assertTrue(result.contains("DTSTART;VALUE=DATE:" + startDate));
	}

}
