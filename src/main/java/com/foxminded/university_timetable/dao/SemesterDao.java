package com.foxminded.university_timetable.dao;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.row_mapper.SemesterRowMapper;

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
			Semester semester = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, semesterRowMapper);
			return Optional.of(semester);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Semester> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, semesterRowMapper);
	}

	public Semester save(Semester semester) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.INTEGER,
				Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory
				.newPreparedStatementCreator(Arrays.asList(semester.getYearOfStudy(), semester.getPeriod()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		semester.setId(newId);
		return semester;
	}

	public Semester update(Semester semester) {
		this.jdbcTemplate.update(UPDATE, semester.getYearOfStudy(), semester.getPeriod(), semester.getId());
		return semester;
	}

	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
}
