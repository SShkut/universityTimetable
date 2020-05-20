package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university_timetable.model.Student;

public class StudentRowMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student();
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		student.setId(Long.valueOf(rs.getLong("id")));
		student.setFirstName(rs.getString("first_name"));
		student.setLastName(rs.getString("last_name"));
		student.setTaxNumber(rs.getString("tax_number"));
		student.setPhoneNumber(rs.getString("phone_number"));
		student.setEmail(rs.getString("email"));
		student.setStudentCardNumber(rs.getString("student_card_number"));
		return student;
	}
	
}
