package com.foxminded.university_timetable.model;

public class Semestr {

	private Integer yearOfStudy;
	private String period;
	
	public Semestr(int yearOfStudy, String period) {
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
}
