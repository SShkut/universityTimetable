package com.foxminded.university_timetable.model;

import java.time.LocalTime;

public class TimeSlot {

	private LocalTime startTime;
	private LocalTime endTime;
	private Course course;
	private Teacher teacher;
	private Group group;
	private Room room;
	
	public TimeSlot(LocalTime startTime, LocalTime endTime, Course course, Teacher teacher, Group group, Room room) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.room = room;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}	
}
