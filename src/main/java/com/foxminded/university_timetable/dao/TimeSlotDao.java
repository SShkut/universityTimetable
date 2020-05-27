package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.row_mapper.TimeSlotRowMapper;

@Repository
public class TimeSlotDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM time_slots WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM time_slots";
	private static final String SAVE = "INSERT INTO time_slots (start_time, end_time, course_id, teacher_id, group_id, room_id, daily_timetalbe_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE time_slots SET start_time = ?, end_time = ?, course_id = ?, teacher_id = ?, group_id = ?, room_id = ?, daily_timetalbe_id = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM time_slots WHERE id = ?";
	private static final String FIND_ALL_TIME_SLOTS_OF_DAILY_TIMETABLE = "SELECT * FROM time_slots WHERE daily_timetable_id = ?";
	
	private final JdbcTemplate jdbcTemplate;
	private final TimeSlotRowMapper timeSlotRowMapper;
	
	@Autowired
	public TimeSlotDao(JdbcTemplate jdbcTemplate, TimeSlotRowMapper timeSlotRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timeSlotRowMapper = timeSlotRowMapper;
	}
	
	public Optional<TimeSlot> findById(Long id) {
		try {
			TimeSlot timeSlot = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<TimeSlot> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, timeSlotRowMapper);
	}
	
	public void save(TimeSlot timeSlot) {
		this.jdbcTemplate.update(SAVE, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getCourse().getId(),
				timeSlot.getTeacher().getId(), timeSlot.getGroup().getId(), timeSlot.getRoom().getId());
	}

	public void update(TimeSlot timeSlot) {
		this.jdbcTemplate.update(UPDATE, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getCourse(), timeSlot.getTeacher(), 
				timeSlot.getGroup(), timeSlot.getRoom(), timeSlot.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
	
	public List<TimeSlot> findAllTimeSlotsOfDailyTimetable(DailyTimetable dailyTimetable) {
		return this.jdbcTemplate.query(FIND_ALL_TIME_SLOTS_OF_DAILY_TIMETABLE, new Object[] {dailyTimetable.getId()}, timeSlotRowMapper);
	}
}
