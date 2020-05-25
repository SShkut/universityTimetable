package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Autowired
	private final JdbcTemplate jdbcTemplate;
	
	public SemesterDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Optional<Semester> findById(Long id) {
		try {
			Semester semester = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, new SemesterRowMapper());
			return Optional.of(semester);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Semester> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, new SemesterRowMapper());
	}
	
	public void save(Semester semester) {
		this.jdbcTemplate.update(SAVE, semester.getYearOfStudy(), semester.getPeriod());
	}
	
	public void update(Semester semester) {
		this.jdbcTemplate.update(UPDATE, semester.getYearOfStudy(), semester.getPeriod(), semester.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
}
