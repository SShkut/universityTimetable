package com.foxminded.university_timetable.model;

import java.util.List;

public class Group {

	private String name;
	private String major;
	private String department;
	private String semester;
	private List<Student> students;
	
	public Group(String name, String major, String department, String semester, List<Student> students) {
		this.name = name;
		this.major = major;
		this.department = department;
		this.semester = semester;
		this.students = students;
	}	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMajor() {
		return major;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getSemester() {
		return semester;
	}
	
	public void setSemester(String semester) {
		this.semester = semester;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
