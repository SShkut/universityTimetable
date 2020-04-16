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
}
