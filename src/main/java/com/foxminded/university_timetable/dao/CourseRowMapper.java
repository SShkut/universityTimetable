package com.foxminded.university_timetable.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Course;

@Component
public class CourseRowMapper implements RowMapper<Course> {

	@Override
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs.isBeforeFirst()) {
			return null;
		}

		Course course = new Course();
		course.setId(rs.getLong("id"));
		course.setName(rs.getString("name"));
		return course;
	}
}
