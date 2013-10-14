package com.darwinsys.calendar;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/** One Appointment, to be entered into a calendar.
 * Platform-neutral; doesn't contain e.g. any Scalix-specifics.
 * XXX Add Support For:
 * 	Repetitions
 *  Categories
 *  Organizer and Attendees
 */
public class CalendarEvent implements Serializable {

	private static final long serialVersionUID = 2687393401964176535L;
	private int year;
	private int month;
	private int day;
	private EventType eventType = EventType.APPOINTMENT;
	private int startHour, startMinute = 0;
	private int endHour, endMinute = 0;
	private String description = "";
	private String summary;
	private String location = "";
	private UUID uuid;
	private ShowStatus showStatus = ShowStatus.BUSY;
	private Person organizer;
	private List<Person> attendees;
	
	// Remember to re-gen hashCode() and equals() when adding fields!
	
	public CalendarEvent() {
		// all defaults
	}
	
	/** Create an all day event */
	public CalendarEvent(String description, String summary, String location,
			int year, int month, int day) {
		
		this(EventType.ALLDAY, description, summary, location, year, month, day, 0, 0, 0, 0);
		
	}
	
	/** Create an Appointment, having start and end hours */
	public CalendarEvent(String description, String summary, String location,
			int year, int month, int day, 
			int startHour, int endHour) {
		
		this(EventType.APPOINTMENT, description, summary, location, year, month, day, startHour, 0, endHour, 0);
		
	}
	
	/** Create a CalendarEvent with all fields */
	public CalendarEvent(EventType eventType, String description, String summary, String location,
			int year, int month, int day, 
			int startHour, int startMinute, int endHour, int endMinute) {
		super();
		this.eventType = eventType;
		this.year = year;
		this.month = month;
		this.day = day;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
		this.description = description;
		this.summary = summary;
		this.location = location;
		this.uuid = UUID.randomUUID();
		this.showStatus = ShowStatus.BUSY;
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public ShowStatus getShowStatus() {
		return showStatus;
	}
	
	public void setShowStatus(ShowStatus status) {
		this.showStatus = status;
	}

	public List<Person> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<Person> attendees) {
		this.attendees = attendees;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public Person getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Person organizer) {
		this.organizer = organizer;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((attendees == null) ? 0 : attendees.hashCode());
		result = PRIME * result + day;
		result = PRIME * result + ((description == null) ? 0 : description.hashCode());
		result = PRIME * result + endHour;
		result = PRIME * result + endMinute;
		result = PRIME * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = PRIME * result + ((location == null) ? 0 : location.hashCode());
		result = PRIME * result + month;
		result = PRIME * result + ((organizer == null) ? 0 : organizer.hashCode());
		result = PRIME * result + ((showStatus == null) ? 0 : showStatus.hashCode());
		result = PRIME * result + startHour;
		result = PRIME * result + startMinute;
		result = PRIME * result + ((summary == null) ? 0 : summary.hashCode());
		result = PRIME * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = PRIME * result + year;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CalendarEvent other = (CalendarEvent) obj;
		if (attendees == null) {
			if (other.attendees != null)
				return false;
		} else if (!attendees.equals(other.attendees))
			return false;
		if (day != other.day)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endHour != other.endHour)
			return false;
		if (endMinute != other.endMinute)
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (month != other.month)
			return false;
		if (organizer == null) {
			if (other.organizer != null)
				return false;
		} else if (!organizer.equals(other.organizer))
			return false;
		if (showStatus == null) {
			if (other.showStatus != null)
				return false;
		} else if (!showStatus.equals(other.showStatus))
			return false;
		if (startHour != other.startHour)
			return false;
		if (startMinute != other.startMinute)
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}
