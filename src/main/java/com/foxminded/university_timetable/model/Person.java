package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
	
	private Long id;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (taxNumber == null) {
			if (other.taxNumber != null)
				return false;
		} else if (!taxNumber.equals(other.taxNumber))
			return false;
		return true;
	}	
}
