package com.foxminded.university_timetable.model;

import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {

	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Course course;
	private Teacher teacher;
	private Group group;
	private Room room;

	public TimeSlot() {
	}

	public TimeSlot(LocalTime startTime, LocalTime endTime, Course course, Teacher teacher, Group group, Room room) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.room = room;
	}

	public TimeSlot(Long id, LocalTime startTime, LocalTime endTime, Course course, Teacher teacher, Group group,
			Room room) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.room = room;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		return Objects.hash(course, endTime, group, id, room, startTime, teacher);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSlot other = (TimeSlot) obj;
		return Objects.equals(course, other.course) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(group, other.group) && Objects.equals(id, other.id)
				&& Objects.equals(room, other.room) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(teacher, other.teacher);
	}

	@Override
	public String toString() {
		return "TimeSlot id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", course=" + course
				+ ", teacher=" + teacher + ", group=" + group + ", room=" + room;
	}

}
