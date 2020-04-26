package com.foxminded.university_timetable.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
	
	private Long id;
	private String name;
	private List<Course> prerequisites;	
	
	public Course() {
	}

	public Course(String name) {
		this.name = name;
		this.prerequisites = new ArrayList<>();
	}

	public Course(String name, List<Course> prerequisits) {
		this.name = name;
		this.prerequisites = prerequisits;
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
	
	public void setPrerequisits(List<Course> prerequisites) {
		this.prerequisites = prerequisites;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((prerequisites == null) ? 0 : prerequisites.hashCode());
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
		Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prerequisites == null) {
			if (other.prerequisites != null)
				return false;
		} else if (!prerequisites.equals(other.prerequisites))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}	
}
