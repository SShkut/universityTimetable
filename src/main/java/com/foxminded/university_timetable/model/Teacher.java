package com.foxminded.university_timetable.model;

import java.util.List;

public class Teacher extends Person {

	private String degree;

	public Teacher(String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			String degree) {
		super(firstName, lastName, taxNumber, phoneNumber, email);
		this.degree = degree;
	}

	public Teacher(String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			List<Course> courses, String degree) {
		super(firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.degree = degree;
	}
	
	public Teacher(Long id, String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			List<Course> courses, String degree) {
		super(id, firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.degree = degree;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Override
	public String toString() {
		return degree + " " + getFirstName() + " " + getLastName();
	}	
}
