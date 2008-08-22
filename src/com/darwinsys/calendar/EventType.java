package com.darwinsys.calendar;

public enum EventType {
	/** An All Day event has no start or stop time, only a day */
	ALLDAY,
	/** An Instant event has a start time but no stop time */
	INSTANT,
	/** An event with Duration has start and stop times. */
	APPOINTMENT
}
