package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.TimetableRowMapper;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Timetable;

@Repository
public class TimetableDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

	private static final String FIND_BY_ID = "SELECT * FROM timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM timetables";
	private static final String SAVE = "INSERT INTO timetables (name) VALUES (?)";
	private static final String UPDATE = "UPDATE timetables SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM timetables WHERE id = ?";

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
			logger.debug(FIND_BY_ID + " id = {}", id);
			Timetable timetable = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, timetableRowMapper);
			List<DailyTimetable> dailyTimetables = dailyTimetableDao.findTimetableDailyTimetables(timetable);
			timetable.setDailyTimetables(dailyTimetables);
			return Optional.of(timetable);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Timetable with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<Timetable> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, timetableRowMapper);
	}

	public void save(Timetable timetable) {
		logger.debug(SAVE + " {}", timetable.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, timetable.getName());
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		timetable.setId(id);
	}

	public void update(Timetable timetable) {
		logger.debug(UPDATE + " {}", timetable.toString());
		jdbcTemplate.update(UPDATE, timetable.getName(), timetable.getId());
	}

	public void delete(Timetable timetable) {
		logger.debug(DELETE + " {}", timetable.toString());
		jdbcTemplate.update(DELETE, timetable.getId());
	}
}
