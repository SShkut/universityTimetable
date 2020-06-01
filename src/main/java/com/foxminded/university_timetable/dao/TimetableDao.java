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

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Timetable;
import com.foxminded.university_timetable.row_mapper.DailyTimetableRowMapper;
import com.foxminded.university_timetable.row_mapper.TimetableRowMapper;

@Repository
public class TimetableDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM timetables";
	private static final String SAVE = "INSERT INTO timetables (name) VALUES (?)";
	private static final String UPDATE = "UPDATE timetables SET name = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM timetables WHERE id = ?";
	private static final String FIND_DAILY_TIMETABLES_OF_TIMETABLE = "SELECT * FROM daily_timetables WHERE timetable_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final TimetableRowMapper timetableRowMapper;
	private final DailyTimetableRowMapper dailyTimetableRowMapper;
	
	@Autowired
	public TimetableDao(JdbcTemplate jdbcTemplate, TimetableRowMapper timetableRowMapper, DailyTimetableRowMapper dailyTimetableRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timetableRowMapper = timetableRowMapper;
		this.dailyTimetableRowMapper = dailyTimetableRowMapper;
	}
	
	public Optional<Timetable> findById(Long id) {
		try {
			Timetable timetable = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, timetableRowMapper);
			List<DailyTimetable> dailyTimetables = findDailyTimetablesOfTimetable(timetable);
			timetable.setDailyTimetables(dailyTimetables);
			return Optional.of(timetable);
		} catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Timetable> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, timetableRowMapper);
	}
	
	public Timetable save(Timetable timetable) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(Arrays.asList(timetable.getName()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			 newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id"))); 
		} else {
			newId= keyHolder.getKey().longValue();
		}
		timetable.setId(newId);
		return timetable;
	}
	
	public Timetable update(Timetable timetable) {
		this.jdbcTemplate.update(UPDATE, timetable.getName(), timetable.getId());
		return timetable;
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}	
	
	public List<DailyTimetable> findDailyTimetablesOfTimetable(Timetable timetable) {
		return this.jdbcTemplate.query(FIND_DAILY_TIMETABLES_OF_TIMETABLE, new Object[] {timetable.getId()}, dailyTimetableRowMapper);
	}
}
