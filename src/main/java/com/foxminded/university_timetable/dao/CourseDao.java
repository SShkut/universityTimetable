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

import com.foxminded.university_timetable.dao.row_mapper.CourseRowMapper;
import com.foxminded.university_timetable.model.Course;

@Repository
public class CourseDao {

	private static final String FIND_BY_ID = "SELECT * FROM courses WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM courses";
	private static final String DELETE_BY_ID = "DELETE FROM courses WHERE id = ?";
	private static final String SAVE = "INSERT INTO courses (name) VALUES (?)";
	private static final String UPDATE = "UPDATE courses SET name = ? WHERE id = ?";
	private static final String FIND_PREREQUISITES_OF_COURSE = "WITH RECURSIVE course_prerequisites(course_id, prerequisite_id) AS ("
			+ "SELECT course_id, prerequisite_id " 
			+ "FROM course_hierarchy " + "WHERE course_id = ? " 
			+ "UNION ALL "
			+ "SELECT ch.course_id, ch.prerequisite_id " 
			+ "FROM course_hierarchy ch "
			+ "JOIN course_prerequisites cp ON ch.course_id = cp.prerequisite_id) " 
			+ "SELECT DISTINCT c.id, c.name "
			+ "FROM course_prerequisites cp " 
			+ "JOIN courses c ON cp.prerequisite_id = c.id;";

	private final JdbcTemplate jdbcTemplate;
	private final CourseRowMapper courseRowMapper;

	public CourseDao(JdbcTemplate jdbcTemplate, CourseRowMapper courseRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.courseRowMapper = courseRowMapper;
	}

	public Optional<Course> findById(Long id) {
		try {
			Course course = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, courseRowMapper);
			List<Course> prerequisites = findCoursePrerequisites(course);
			course.setPrerequisites(prerequisites);
			return Optional.of(course);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Course> findAll() {
		return jdbcTemplate.query(FIND_ALL, courseRowMapper);
	}

	public void delete(Course course) {
		jdbcTemplate.update(DELETE_BY_ID, course.getId());
	}

	public Course save(Course course) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, course.getName());
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		course.setId(id);
		return course;
	}

	public void update(Course course) {
		jdbcTemplate.update(UPDATE, course.getName(), course.getId());
	}

	public List<Course> findCoursePrerequisites(Course course) {
		return jdbcTemplate.query(FIND_PREREQUISITES_OF_COURSE, new Object[] { course.getId() }, courseRowMapper);
	}
}
