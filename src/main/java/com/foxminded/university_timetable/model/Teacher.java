package com.foxminded.university_timetable.model;

import java.util.List;
import java.util.Objects;

public class Teacher extends Person {

	private String degree;	

	public Teacher() {
		
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(degree);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teacher other = (Teacher) obj;
		return Objects.equals(degree, other.degree);
	}
}
