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

import com.foxminded.university_timetable.dao.row_mapper.SemesterRowMapper;
import com.foxminded.university_timetable.model.Semester;

@Repository
public class SemesterDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

	private static final String FIND_ALL = "SELECT * FROM semesters";
	private static final String FIND_BY_ID = "SELECT * FROM semesters WHERE id = ?";
	private static final String SAVE = "INSERT INTO semesters (year_of_study, period) values (?, ?)";
	private static final String UPDATE = "UPDATE semesters SET year_of_study = ?, period = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM semesters WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final SemesterRowMapper semesterRowMapper;

	public SemesterDao(JdbcTemplate jdbcTemplate, SemesterRowMapper semesterRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.semesterRowMapper = semesterRowMapper;
	}

	public Optional<Semester> findById(Long id) {
		try {
			logger.debug(FIND_BY_ID + " id = {}", id);
			Semester semester = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, semesterRowMapper);
			return Optional.of(semester);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Course with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<Semester> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, semesterRowMapper);
	}

	public void save(Semester semester) {
		logger.debug(SAVE + " {}", semester.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, semester.getYearOfStudy());
			ps.setString(2, semester.getPeriod());
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		semester.setId(id);
	}

	public void update(Semester semester) {
		logger.debug(UPDATE + " {}", semester.toString());
		jdbcTemplate.update(UPDATE, semester.getYearOfStudy(), semester.getPeriod(), semester.getId());
	}

	public void delete(Semester semester) {
		logger.debug(DELETE + " {}", semester.toString());
		jdbcTemplate.update(DELETE, semester.getId());
	}
}
