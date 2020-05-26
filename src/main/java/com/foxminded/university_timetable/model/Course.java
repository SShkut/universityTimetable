package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
	
	private Long id;
	private String name;
	private List<Course> prerequisites;
	
	public Course() {
	}

	public Course(Long id, String name, List<Course> prerequisits) {
		this.id = id;
		this.name = name;
		this.prerequisites = prerequisits;
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

	public List<Course> getPrerequisites() {
		return prerequisites;
	}	

	public void setPrerequisites(List<Course> prerequisites) {
		this.prerequisites = prerequisites;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, prerequisites);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(prerequisites, other.prerequisites);
	}
}
