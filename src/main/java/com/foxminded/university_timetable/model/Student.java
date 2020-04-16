package com.foxminded.university_timetable.model;

import java.util.List;

public class Student extends Person {
	
	private String studentCartNumber;

	public Student(String firstName, String lastName, String taxNumber, String phoneNumber, String email, 
			List<Course> courses, String studentCartNumber) {
		super(firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.studentCartNumber = studentCartNumber;
	}

	public String getStudentCartNumber() {
		return studentCartNumber;
	}

	public void setStudentCartNumber(String studentCartNumber) {
		this.studentCartNumber = studentCartNumber;
	}
}
