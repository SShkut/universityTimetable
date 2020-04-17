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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
		result = prime * result + ((sybmol == null) ? 0 : sybmol.hashCode());
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
		Room other = (Room) obj;
		if (capacity == null) {
			if (other.capacity != null)
				return false;
		} else if (!capacity.equals(other.capacity))
			return false;
		if (sybmol == null) {
			if (other.sybmol != null)
				return false;
		} else if (!sybmol.equals(other.sybmol))
			return false;
		return true;
	}
}
