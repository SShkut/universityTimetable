package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.TimeSlotRowMapper;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.TimeSlot;

@Repository
public class TimeSlotDao {

	private static final String FIND_BY_ID = "SELECT * FROM time_slots WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM time_slots";
	private static final String SAVE = "INSERT INTO time_slots (start_time, end_time, course_id, teacher_id, group_id, room_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE time_slots SET start_time = ?, end_time = ?, course_id = ?, teacher_id = ?, group_id = ?, room_id = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM time_slots WHERE id = ?";
	private static final String FIND_ALL_DAILY_TIMETABLE_TIME_SLOTS = "SELECT * FROM time_slots WHERE daily_timetable_id = ?";
	private static final String ADD_TIME_SLOT_TO_DAILY_TIMETABLE = "UPDATE time_slots SET daily_timetable_id = ? WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final TimeSlotRowMapper timeSlotRowMapper;

	public TimeSlotDao(JdbcTemplate jdbcTemplate, TimeSlotRowMapper timeSlotRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timeSlotRowMapper = timeSlotRowMapper;
	}

	public Optional<TimeSlot> findById(Long id) {
		try {
			TimeSlot timeSlot = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<TimeSlot> findAll() {
		return jdbcTemplate.query(FIND_ALL, timeSlotRowMapper);
	}

	public TimeSlot save(TimeSlot timeSlot) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setTime(1, Time.valueOf(timeSlot.getStartTime()));
			ps.setTime(2, Time.valueOf(timeSlot.getEndTime()));
			ps.setLong(3, timeSlot.getCourse().getId());
			ps.setLong(4, timeSlot.getTeacher().getId());
			ps.setLong(5, timeSlot.getGroup().getId());
			ps.setLong(6, timeSlot.getRoom().getId());
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		timeSlot.setId(id);
		return timeSlot;
	}

	public void update(TimeSlot timeSlot) {
		jdbcTemplate.update(UPDATE, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getCourse().getId(),
				timeSlot.getTeacher().getId(), timeSlot.getGroup().getId(), timeSlot.getRoom().getId(),
				timeSlot.getId());
	}

	public void delete(TimeSlot timeSlot) {
		jdbcTemplate.update(DELETE_BY_ID, timeSlot.getId());
	}

	public List<TimeSlot> findAllDailyTimetableTimeSlots(DailyTimetable dailyTimetable) {
		return jdbcTemplate.query(FIND_ALL_DAILY_TIMETABLE_TIME_SLOTS, new Object[] { dailyTimetable.getId() },
				timeSlotRowMapper);
	}

	public void addTimeSlotToDailyTimetable(TimeSlot timeSlot, DailyTimetable dailyTimetable) {
		jdbcTemplate.update(ADD_TIME_SLOT_TO_DAILY_TIMETABLE, dailyTimetable.getId(), timeSlot.getId());
	}
}
