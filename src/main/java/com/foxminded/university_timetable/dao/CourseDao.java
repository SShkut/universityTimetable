package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.row_mapper.CourseRowMapper;

public class CourseDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM COURSES WHERE id = ?";
	private static final String FIND_ALL= "SELECT * FROM COURSES";
	
	private final JdbcTemplate jdbcTemplate;
	
	public CourseDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}	
	
	public Optional<Course> findById(Long id) {
		try {
			Course course = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, new CourseRowMapper());
			return Optional.of(course);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Course> findAll() {
		return  jdbcTemplate.query(FIND_ALL, new CourseRowMapper());
	}
}
