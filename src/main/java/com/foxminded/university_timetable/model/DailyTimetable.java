package com.foxminded.university_timetable.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DailyTimetable {

	private Long id;
	private LocalDate date;
	private List<TimeSlot> timeSlots;

	public DailyTimetable() {
	}

	public DailyTimetable(LocalDate date) {
		this.date = date;
	}

	public DailyTimetable(LocalDate date, List<TimeSlot> timeSlots) {
		this.date = date;
		this.timeSlots = timeSlots;
	}

	public DailyTimetable(Long id, LocalDate date) {
		this.id = id;
		this.date = date;
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
		this.getTimeSlots().add(timeSlot);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, timeSlots);
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
		return Objects.equals(date, other.date) && Objects.equals(id, other.id)
				&& Objects.equals(timeSlots, other.timeSlots);
	}

	@Override
	public String toString() {
		return "DailyTimetable id=" + id + ", date=" + date;
	}
}
