package com.foxminded.university_timetable.model;

import java.util.List;
import java.util.Objects;

public class Timetable {

	private Long id;
	private String name;
	private List<DailyTimetable> dailyTimetables;

	public Timetable() {
	}

	public Timetable(String name, List<DailyTimetable> timetables) {
		this.name = name;
		this.dailyTimetables = timetables;
	}

	public Timetable(Long id, String name, List<DailyTimetable> timetables) {
		this.id = id;
		this.name = name;
		this.dailyTimetables = timetables;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DailyTimetable> getDailyTimetables() {
		return dailyTimetables;
	}

	public void setDailyTimetables(List<DailyTimetable> dailyTimetables) {
		this.dailyTimetables = dailyTimetables;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, dailyTimetables);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timetable other = (Timetable) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(dailyTimetables, other.dailyTimetables);
	}

	@Override
	public String toString() {
		return "Timetable id=" + id + ", name=" + name;
	}
}
