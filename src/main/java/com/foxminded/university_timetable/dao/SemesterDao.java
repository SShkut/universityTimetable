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

import com.foxminded.university_timetable.model.Semester;

@Repository
public class SemesterDao {

	private static final String FIND_ALL = "SELECT * FROM semesters";
	private static final String FIND_BY_ID = "SELECT * FROM semesters WHERE id = ?";
	private static final String SAVE = "INSERT INTO semesters (year_of_study, period) values (?, ?)";
	private static final String UPDATE = "UPDATE semesters SET year_of_study = ?, period = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM semesters WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final SemesterRowMapper semesterRowMapper;

	public SemesterDao(JdbcTemplate jdbcTemplate, SemesterRowMapper semesterRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.semesterRowMapper = semesterRowMapper;
	}

	public Optional<Semester> findById(Long id) {
		try {
			Semester semester = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, semesterRowMapper);
			return Optional.of(semester);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Semester> findAll() {
		return jdbcTemplate.query(FIND_ALL, semesterRowMapper);
	}

	public Semester save(Semester semester) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, semester.getYearOfStudy());
			ps.setString(2, semester.getPeriod());
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		semester.setId(id);
		return semester;
	}

	public void update(Semester semester) {
		jdbcTemplate.update(UPDATE, semester.getYearOfStudy(), semester.getPeriod(), semester.getId());
	}

	public void delete(Semester semester) {
		jdbcTemplate.update(DELETE_BY_ID, semester.getId());
	}
}
