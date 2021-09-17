package com.darwinsys.calendar;

import org.junit.Test;
import java.time.*;
import java.util.Optional;

public class CalendarEventTest {

	CalendarEvent subject = CalendarEvent("Happy 200th birthday to Canada!",
	EventType.APPOINTMENT,
	CalendarEvent.makeUUID(),
	LocalDate.of(2067, 6, 2),
	Optional.empty(),
	Optional.empty(), 
	Optional.empty(),
	Optional.of("Centennial Committee"), Optional.of("nobody@canada.ca"),
	Optional.of("Across Canada"),
	Optional.empty());

	@Test
	public void testOne() {
		System.out.println(subject);
	}
}
