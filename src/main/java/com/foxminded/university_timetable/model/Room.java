package com.foxminded.university_timetable.model;

public class Room {

	private String sybmol;
	private Integer capacity;
	
	public Room(String sybmol, Integer capacity) {
		this.sybmol = sybmol;
		this.capacity = capacity;
	}

	public String getSybmol() {
		return sybmol;
	}

	public void setSybmol(String sybmol) {
		this.sybmol = sybmol;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}	
}
