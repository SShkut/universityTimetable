package com.foxminded.university_timetable.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Timetable;

@Component
public class TimetableRowMapper implements RowMapper<Timetable> {

	@Override
	public Timetable mapRow(ResultSet rs, int rowNum) throws SQLException {
		Timetable timetable = new Timetable();
		timetable.setId(rs.getLong("id"));
		timetable.setName(rs.getString("name"));
		return timetable;
	}

}
