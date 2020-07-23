package com.foxminded.university_timetable.dao.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.DailyTimetable;

@Component
public class DailyTimetableRowMapper implements RowMapper<DailyTimetable> {

	@Override
	public DailyTimetable mapRow(ResultSet rs, int rowNum) throws SQLException {
		DailyTimetable dailyTimetable = new DailyTimetable();
		dailyTimetable.setId(rs.getLong("id"));
		dailyTimetable.setDate(rs.getObject("date", LocalDate.class));
		return dailyTimetable;
	}
}
