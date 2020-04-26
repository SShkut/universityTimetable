package com.foxminded.university_timetable.model;

import java.util.List;

public class Student extends Person {
	
	private String studentCardNumber;	

	public Student(String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			String studentCardNumber) {
		super(firstName, lastName, taxNumber, phoneNumber, email);
		this.studentCardNumber = studentCardNumber;
	}

	public Student(String firstName, String lastName, String taxNumber, String phoneNumber, String email, 
			List<Course> courses, String studentCardNumber) {
		super(firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.studentCardNumber = studentCardNumber;
	}
	
	public Student(Long id, String firstName, String lastName, String taxNumber, String phoneNumber, String email, 
			List<Course> courses, String studentCardNumber) {
		super(id, firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.studentCardNumber = studentCardNumber;
	}

	public String getStudentCardNumber() {
		return studentCardNumber;
	}

	public void setStudentCardNumber(String studentCardNumber) {
		this.studentCardNumber = studentCardNumber;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + ": " + studentCardNumber;
	}	
}
