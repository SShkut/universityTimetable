package com.foxminded.university_timetable.model;

import java.util.List;

public abstract class Person {
	
	private String firstName;
	private String lastName;
	private String taxNumber;
	private String phoneNumber;
	private String email;
	private List<Course> courses;	

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
}
