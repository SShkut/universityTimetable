package com.foxminded.university_timetable.model;

import java.util.Objects;

public class Semester {

	private Long id;
	private Integer yearOfStudy;
	private String period;

	public Semester() {
	}

	public Semester(int yearOfStudy, String period) {
		this.yearOfStudy = yearOfStudy;
		this.period = period;
	}

	public Semester(Long id, Integer yearOfStudy, String period) {
		this.id = id;
		this.yearOfStudy = yearOfStudy;
		this.period = period;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(int yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, period, yearOfStudy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Semester other = (Semester) obj;
		return Objects.equals(id, other.id) && Objects.equals(period, other.period)
				&& Objects.equals(yearOfStudy, other.yearOfStudy);
	}

	@Override
	public String toString() {
		return String.format("%s/%s", yearOfStudy, period);
	}
}
