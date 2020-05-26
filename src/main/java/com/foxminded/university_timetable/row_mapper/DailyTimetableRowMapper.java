package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.DailyTimetable;

@Component
public class DailyTimetableRowMapper implements RowMapper<DailyTimetable> {

	@Override
	public DailyTimetable mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		DailyTimetable dailyTimetable = new DailyTimetable();
		dailyTimetable.setId(rs.getLong("id"));
		dailyTimetable.setDate(rs.getDate("date").toLocalDate());

		return dailyTimetable;
	}
}
