package com.foxminded.university_timetable.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.row_mapper.CourseRowMapper;

public class CourseDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM courses WHERE id = ?";
	private static final String FIND_ALL= "SELECT * FROM courses";
	private static final String DELETE_BY_ID = "DELETE FROM courses WHERE id = ?";
	private static final String SAVE = "INSERT INTO courses (name) VALUES (?)";
	private static final String UPDATE = "UPDATE courses SET name = ? WHERE id = ?";
	private static final String FIND_PREREQISITES_OF_COURSE = "";
	
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
		return  this.jdbcTemplate.query(FIND_ALL, new CourseRowMapper());
	}
	
	public void delteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
	
	public void save(Course course) {
		this.jdbcTemplate.update(SAVE, course.getName());
	}
	
	public void update(Course course) {
		this.jdbcTemplate.update(UPDATE, course.getName(), course.getId());
	}
	
	public List<Course> findPrerequisitesOfCourse(Course course) {
		
		return new ArrayList<>();
	}
}
