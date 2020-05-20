package com.foxminded.university_timetable.model;

import java.util.Objects;

public class Room {

	private Long id;
	private String sybmol;
	private Integer capacity;
	
	public Room() {
		
	}
	
	public Room(String sybmol, Integer capacity) {
		this.sybmol = sybmol;
		this.capacity = capacity;
	}	

	public Room(Long id, String sybmol, Integer capacity) {
		this.id = id;
		this.sybmol = sybmol;
		this.capacity = capacity;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		return Objects.hash(capacity, id, sybmol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return Objects.equals(capacity, other.capacity) && Objects.equals(id, other.id)
				&& Objects.equals(sybmol, other.sybmol);
	}
}
