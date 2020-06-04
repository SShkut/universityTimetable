package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Person {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String taxNumber;
	private String phoneNumber;
	private String email;
	private List<Course> courses;
	
	public Person() {
	}

	public Person(String firstName, String lastName, String taxNumber, String phoneNumber, String email, List<Course> courses) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.taxNumber = taxNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.courses = courses;
	}	

	public Person(Long id, String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.taxNumber = taxNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.courses = courses;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return Objects.hash(courses, email, firstName, id, lastName, phoneNumber, taxNumber);
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
		return Objects.equals(courses, other.courses) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(taxNumber, other.taxNumber);
	}
}
