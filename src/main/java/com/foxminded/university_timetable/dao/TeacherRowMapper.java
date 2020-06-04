package com.foxminded.university_timetable.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setId(Long.valueOf(rs.getLong("id")));
		teacher.setFirstName(rs.getString("first_name"));
		teacher.setLastName(rs.getString("last_name"));
		teacher.setTaxNumber(rs.getString("tax_number"));
		teacher.setPhoneNumber(rs.getString("phone_number"));
		teacher.setEmail(rs.getString("email"));
		teacher.setDegree(rs.getString("degree"));
		return teacher;
	}
}
