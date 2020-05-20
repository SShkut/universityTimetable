package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university_timetable.model.Semester;

public class SemesterRowMapper implements RowMapper<Semester> {

	@Override
	public Semester mapRow(ResultSet rs, int rowNum) throws SQLException {
		Semester semester = new Semester();
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		semester.setId(rs.getLong("id"));
		semester.setYearOfStudy(rs.getInt("year_of_study"));
		semester.setPeriod(rs.getString("period"));
		return semester;
	}
}
