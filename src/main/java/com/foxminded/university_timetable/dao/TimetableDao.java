package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Timetable;

@Repository
public class TimetableDao {

	private static final String FIND_BY_ID = "SELECT * FROM timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM timetables";
	private static final String SAVE = "INSERT INTO timetables (name) VALUES (?)";
	private static final String UPDATE = "UPDATE timetables SET name = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM timetables WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final TimetableRowMapper timetableRowMapper;
	private final DailyTimetableDao dailyTimetableDao;

	public TimetableDao(JdbcTemplate jdbcTemplate, TimetableRowMapper timetableRowMapper,
			DailyTimetableDao dailyTimetableDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.timetableRowMapper = timetableRowMapper;
		this.dailyTimetableDao = dailyTimetableDao;
	}

	public Optional<Timetable> findById(Long id) {
		try {
			Timetable timetable = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, timetableRowMapper);
			List<DailyTimetable> dailyTimetables = dailyTimetableDao.findTimetableDailyTimetables(timetable);
			timetable.setDailyTimetables(dailyTimetables);
			return Optional.of(timetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Timetable> findAll() {
		return jdbcTemplate.query(FIND_ALL, timetableRowMapper);
	}

	public Timetable save(Timetable timetable) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, timetable.getName());
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		timetable.setId(id);
		return timetable;
	}

	public void update(Timetable timetable) {
		jdbcTemplate.update(UPDATE, timetable.getName(), timetable.getId());
	}

	public void delete(Timetable timetable) {
		jdbcTemplate.update(DELETE_BY_ID, timetable.getId());
	}
}
