package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Semester;

@Component
public class SemesterRowMapper implements RowMapper<Semester> {

	@Override
	public Semester mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		Semester semester = new Semester();
		semester.setId(rs.getLong("id"));
		semester.setYearOfStudy(rs.getInt("year_of_study"));
		semester.setPeriod(rs.getString("period"));
		
		return semester;
	}
}
