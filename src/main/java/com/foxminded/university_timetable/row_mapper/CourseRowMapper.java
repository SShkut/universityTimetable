package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university_timetable.model.Course;

public class CourseRowMapper implements RowMapper<Course> {

	@Override
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		Course course = new Course();
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		course.setId(Long.valueOf(rs.getLong("id")));
		course.setName(rs.getString("name"));
		course.setPrerequisits(new ArrayList<Course>());		
		return course;
	}
}
