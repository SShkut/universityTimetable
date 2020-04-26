package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

	private Long id;
	private String name;
	private String major;
	private String department;
	private Semester semester;
	private List<Student> students;	
	
	public Group(String name, String major, String department, Semester semester) {
		this.name = name;
		this.major = major;
		this.department = department;
		this.semester = semester;
		this.students = new ArrayList<>();
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((major == null) ? 0 : major.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((semester == null) ? 0 : semester.hashCode());
		result = prime * result + ((students == null) ? 0 : students.hashCode());
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
		Group other = (Group) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (major == null) {
			if (other.major != null)
				return false;
		} else if (!major.equals(other.major))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (semester == null) {
			if (other.semester != null)
				return false;
		} else if (!semester.equals(other.semester))
			return false;
		if (students == null) {
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " major: " + major + " department: " + department + " semester: " + semester;
	}	
}
