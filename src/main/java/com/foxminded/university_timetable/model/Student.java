package com.foxminded.university_timetable.model;

import java.util.List;
import java.util.Objects;

public class Student extends Person {

	private String studentCardNumber;

	public Student() {
	}

	public Student(String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			List<Course> courses, String studentCardNumber) {
		super(firstName, lastName, taxNumber, phoneNumber, email, courses);
		this.studentCardNumber = studentCardNumber;
	}

	public Student(Long id, String firstName, String lastName, String taxNumber, String phoneNumber, String email,
			String studentCardNumber) {
		super(id, firstName, lastName, taxNumber, phoneNumber, email);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(studentCardNumber);
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
		Student other = (Student) obj;
		return Objects.equals(studentCardNumber, other.studentCardNumber);
	}
}
