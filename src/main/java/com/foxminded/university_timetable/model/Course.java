package com.foxminded.university_timetable.model;

import java.util.List;

public class Course {
	
	private String name;
	private List<Course> prerequisits;
	
	public Course(String name, List<Course> prerequisits) {
		this.name = name;
		this.prerequisits = prerequisits;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Course> getPrerequisits() {
		return prerequisits;
	}
	
	public void setPrerequisits(List<Course> prerequisits) {
		this.prerequisits = prerequisits;
	}	
}
