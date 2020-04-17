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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((prerequisits == null) ? 0 : prerequisits.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prerequisits == null) {
			if (other.prerequisits != null)
				return false;
		} else if (!prerequisits.equals(other.prerequisits))
			return false;
		return true;
	}
}
