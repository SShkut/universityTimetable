package com.foxminded.university_timetable.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;

@Component
public class TimeSlotRowMapper implements RowMapper<TimeSlot> {

	private final CourseDao courseDao;
	private final TeacherDao teacherDao;
	private final GroupDao groupDao;
	private final RoomDao roomDao;

	public TimeSlotRowMapper(CourseDao courseDao, TeacherDao teacherDao, GroupDao groupDao, RoomDao roomDao) {
		this.courseDao = courseDao;
		this.teacherDao = teacherDao;
		this.groupDao = groupDao;
		this.roomDao = roomDao;
	}

	@Override
	public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSlot timeSlot = new TimeSlot();

		timeSlot.setId(rs.getLong("id"));
		timeSlot.setStartTime(rs.getTime("start_time").toLocalTime());
		timeSlot.setEndTime(rs.getTime("end_time").toLocalTime());

		Optional<Course> course = courseDao.findById(rs.getLong("course_id"));
		Optional<Teacher> teacher = teacherDao.findById(rs.getLong("teacher_id"));
		Optional<Group> group = groupDao.findById(rs.getLong("group_id"));
		Optional<Room> room = roomDao.findById(rs.getLong("room_id"));

		timeSlot.setCourse(course.orElseThrow(NoSuchElementException::new));
		timeSlot.setTeacher(teacher.orElseThrow(NoSuchElementException::new));
		timeSlot.setGroup(group.orElseThrow(NoSuchElementException::new));
		timeSlot.setRoom(room.orElseThrow(NoSuchElementException::new));
		return timeSlot;
	}
}