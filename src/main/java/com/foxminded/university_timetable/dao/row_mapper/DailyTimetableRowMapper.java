package com.foxminded.university_timetable.dao.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.dao.TimeSlotDao;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.TimeSlot;

@Component
public class DailyTimetableRowMapper implements RowMapper<DailyTimetable> {

	private final TimeSlotDao timeSlotDao;

	public DailyTimetableRowMapper(TimeSlotDao timeSlotDao) {
		this.timeSlotDao = timeSlotDao;
	}

	@Override
	public DailyTimetable mapRow(ResultSet rs, int rowNum) throws SQLException {
		DailyTimetable dailyTimetable = new DailyTimetable();
		dailyTimetable.setId(rs.getLong("id"));
		dailyTimetable.setDate(rs.getObject("date", LocalDate.class));
		List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
		dailyTimetable.setTimeSlots(timeSlots);
		return dailyTimetable;
	}
}
