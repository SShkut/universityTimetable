package com.foxminded.university_timetable.dao;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
	
	public TimeSlot save(TimeSlot timeSlot) {
		PreparedStatementCreator psc = 
				new PreparedStatementCreatorFactory(SAVE, Types.TIME, Types.TIME, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER)
				.newPreparedStatementCreator(
						Arrays.asList(
							timeSlot.getStartTime(),
							timeSlot.getEndTime(),
							timeSlot.getCourse().getId(),
							timeSlot.getTeacher().getId(),
							timeSlot.getGroup().getId(),
							timeSlot.getRoom().getId()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(psc, keyHolder);
		timeSlot.setId(keyHolder.getKey().longValue());
		
		return timeSlot;
	}

	public TimeSlot update(TimeSlot timeSlot) {
		this.jdbcTemplate.update(UPDATE, 
				timeSlot.getStartTime(), 
				timeSlot.getEndTime(), 
				timeSlot.getCourse(), 
				timeSlot.getTeacher(), 
				timeSlot.getGroup(), 
				timeSlot.getRoom(), 
				timeSlot.getId());
		return timeSlot;
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
}
