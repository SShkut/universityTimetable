package com.foxminded.university_timetable.model;

public class Semester {

	private Integer yearOfStudy;
	private String period;
	
	public Semester(int yearOfStudy, String period) {
		this.yearOfStudy = yearOfStudy;
		this.period = period;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((yearOfStudy == null) ? 0 : yearOfStudy.hashCode());
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
		Semester other = (Semester) obj;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		if (yearOfStudy == null) {
			if (other.yearOfStudy != null)
				return false;
		} else if (!yearOfStudy.equals(other.yearOfStudy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return period + " " + yearOfStudy;
	}	
}
