package com.foxminded.university_timetable.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Timetable {
	
	private String name;
	private List<DailyTimetable> timetables;
	
	public Timetable(String name, List<DailyTimetable> timetables) {
		this.name = name;
		this.timetables = timetables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DailyTimetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(List<DailyTimetable> timetables) {
		this.timetables = timetables;
	}
	
	public DailyTimetable getTimetableDay(LocalDate date, Person person) {
	
		return new DailyTimetable();
	}
	
	public List<DailyTimetable> getTimetableMonth(Month month, Person person) {
		return new ArrayList<>();
	}
}
