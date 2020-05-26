package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.row_mapper.DailyTimetableRowMapper;

@Repository
public class DailyTimetableDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM daily_timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM daily_timetables";
	private static final String SAVE = "INSERT INTO daily_timetables (date, timetable_id) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE daily_timetables SET date = ?, timetable_id = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM daily_timetables WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final DailyTimetableRowMapper dailyTimetableRowMapper;

	@Autowired
	public DailyTimetableDao(JdbcTemplate jdbcTemplate, DailyTimetableRowMapper dailyTimetableRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.dailyTimetableRowMapper = dailyTimetableRowMapper;
	}
	
	public Optional<DailyTimetable> findById(Long id) {
		try {
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, dailyTimetableRowMapper);
	}
	
	public void save(DailyTimetable dailyTimetable) {
		this.jdbcTemplate.update(SAVE, dailyTimetable.getDate());
	}
	
	public void update(DailyTimetable dailyTimetable) {
		this.jdbcTemplate.update(UPDATE, dailyTimetable.getDate(), dailyTimetable.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
}
