package com.darwinsys.calendar;

import org.junit.Test;
import java.io.*;
import java.time.*;
import java.util.*;

public class CalendarEventTest {

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
	public void testOne() {
		PrintWriter pout = new PrintWriter(System.out);
		subject.toVCalEvent(pout, false);
		pout.close();
	}
}
