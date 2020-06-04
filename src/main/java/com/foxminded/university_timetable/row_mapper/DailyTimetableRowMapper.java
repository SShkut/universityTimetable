package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
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
		if (rs.isBeforeFirst()) {
			return null;
		}

		DailyTimetable dailyTimetable = new DailyTimetable();
		dailyTimetable.setId(rs.getLong("id"));
		dailyTimetable.setDate(rs.getDate("date").toLocalDate());
		List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
		if (!timeSlots.isEmpty()) {
			dailyTimetable.setTimeSlots(timeSlots);
		}
		return dailyTimetable;
	}
}
