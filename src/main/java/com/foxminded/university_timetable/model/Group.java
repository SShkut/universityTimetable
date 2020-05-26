package com.foxminded.university_timetable.model;

import java.util.List;
import java.util.Objects;

public class Group {

	private Long id;
	private String name;
	private String major;
	private String department;
	private Semester semester;
	private List<Student> students;
	
	public Group() {
	}

	public Group(String name, String major, String department, Semester semester, List<Student> students) {
		this.name = name;
		this.major = major;
		this.department = department;
		this.semester = semester;
		this.students = students;
	}	
	
	public Group(Long id, String name, String major, String department, Semester semester, List<Student> students) {
		this.id = id;
		this.name = name;
		this.major = major;
		this.department = department;
		this.semester = semester;
		this.students = students;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public Semester getSemester() {
		return semester;
	}
	
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
	public void addStudent(Student student) {
		getStudents().add(student);
	}

	@Override
	public int hashCode() {
		return Objects.hash(department, id, major, name, semester, students);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return Objects.equals(department, other.department) && Objects.equals(id, other.id)
				&& Objects.equals(major, other.major) && Objects.equals(name, other.name)
				&& Objects.equals(semester, other.semester) && Objects.equals(students, other.students);
	}
}
