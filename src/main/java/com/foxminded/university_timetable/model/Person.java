package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
	
	private String firstName;
	private String lastName;
	private String taxNumber;
	private String phoneNumber;
	private String email;
	private List<Course> courses;	

	public Person(String firstName, String lastName, String taxNumber, String phoneNumber, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.taxNumber = taxNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.courses = new ArrayList<>();
	}

	public Person(String firstName, String lastName, String taxNumber, String phoneNumber, String email, List<Course> courses) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.taxNumber = taxNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.courses = courses;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		this.courses.add(course);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((taxNumber == null) ? 0 : taxNumber.hashCode());
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
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (taxNumber == null) {
			if (other.taxNumber != null)
				return false;
		} else if (!taxNumber.equals(other.taxNumber))
			return false;
		return true;
	}	
}
