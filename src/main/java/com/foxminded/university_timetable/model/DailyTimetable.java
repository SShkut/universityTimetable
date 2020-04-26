package com.foxminded.university_timetable.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DailyTimetable {
	
	private Long id;
	private LocalDate date;
	private List<TimeSlot> timeSlots;	

	public DailyTimetable(LocalDate date) {
		this.date = date;
		this.timeSlots = new ArrayList<>();
	}

	public DailyTimetable(LocalDate date, List<TimeSlot> timeSlots) {
		this.date = date;
		this.timeSlots = timeSlots;
	}	
	
	public DailyTimetable(Long id, LocalDate date, List<TimeSlot> timeSlots) {
		this.id = id;
		this.date = date;
		this.timeSlots = timeSlots;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public void addTimeSlot(TimeSlot timeSlot) {
		getTimeSlots().add(timeSlot);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (timeSlots == null) {
			if (other.timeSlots != null)
				return false;
		} else if (!timeSlots.equals(other.timeSlots))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String newLine = System.lineSeparator();
		return String.valueOf(date) + newLine + timeSlots.stream().map(TimeSlot::toString).collect(Collectors.joining());
	}	
}
