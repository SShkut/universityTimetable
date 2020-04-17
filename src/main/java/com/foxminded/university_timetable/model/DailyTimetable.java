package com.foxminded.university_timetable.model;

import java.time.LocalDate;
import java.util.List;

public class DailyTimetable {
	
	private LocalDate date;
	private List<TimeSlot> timeSlots;
	
	
	public DailyTimetable() {
		
	}

	public DailyTimetable(LocalDate date, List<TimeSlot> timeSlots) {
		this.date = date;
		this.timeSlots = timeSlots;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}
	
	public void setTimeSlots(List<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((timeSlots == null) ? 0 : timeSlots.hashCode());
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
		DailyTimetable other = (DailyTimetable) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (timeSlots == null) {
			if (other.timeSlots != null)
				return false;
		} else if (!timeSlots.equals(other.timeSlots))
			return false;
		return true;
	}
}
