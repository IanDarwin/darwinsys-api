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
	
	public CalendarEvent(String description, String summary, String location,
			int year, int month, int day, 
			int startHour, int endHour) {
		
		this(description, summary, location, year, month, day, startHour, 0, endHour, 0);
		
	}
	
	public CalendarEvent(String description, String summary, String location,
			int year, int month, int day, 
			int startHour, int startMinute, int endHour, int endMinute) {
		super();
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

	public Person getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Person organizer) {
		this.organizer = organizer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + endHour;
		result = prime * result + endMinute;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + month;
		result = prime * result
				+ ((showStatus == null) ? 0 : showStatus.hashCode());
		result = prime * result + startHour;
		result = prime * result + startMinute;
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + year;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CalendarEvent other = (CalendarEvent) obj;
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
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (month != other.month)
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
