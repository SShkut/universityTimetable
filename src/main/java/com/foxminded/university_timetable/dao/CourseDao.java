package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.CourseRowMapper;
import com.foxminded.university_timetable.exception.DaoException;
import com.foxminded.university_timetable.model.Course;

@Repository
public class CourseDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);
	private static final String FIND_BY_ID = "SELECT * FROM courses WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM courses";
	private static final String DELETE = "DELETE FROM courses WHERE id = ?";
	private static final String SAVE = "INSERT INTO courses (name) VALUES (?)";
	private static final String UPDATE = "UPDATE courses SET name = ? WHERE id = ?";
	private static final String FIND_COURSE_PREREQUISITES = "WITH RECURSIVE course_prerequisites(course_id, prerequisite_id) AS ("
			+ "SELECT course_id, prerequisite_id " + "FROM course_hierarchy " + "WHERE course_id = ? " + "UNION ALL "
			+ "SELECT ch.course_id, ch.prerequisite_id " + "FROM course_hierarchy ch "
			+ "JOIN course_prerequisites cp ON ch.course_id = cp.prerequisite_id) " + "SELECT DISTINCT c.id, c.name "
			+ "FROM course_prerequisites cp " + "JOIN courses c ON cp.prerequisite_id = c.id;";
	private static final String ADD_COURSE_PREREQUISITE = "INSERT INTO course_hierarchy (course_id, prerequisite_id) values (?, ?)";

	private final JdbcTemplate jdbcTemplate;
	private final CourseRowMapper courseRowMapper;

	public CourseDao(JdbcTemplate jdbcTemplate, CourseRowMapper courseRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.courseRowMapper = courseRowMapper;
	}

	public Optional<Course> findById(Long id) {
		try {
			logger.debug(FIND_BY_ID);	
			logger.debug("Course id = {}", id);	
			Course course = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, courseRowMapper);
			List<Course> prerequisites = findCoursePrerequisites(course);
			course.setPrerequisites(prerequisites);
			return Optional.of(course);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Course with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<Course> findAll() {
		try {
			logger.debug(FIND_ALL);
			return jdbcTemplate.query(FIND_ALL, courseRowMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Can not perform findAll method", e);
		}
	}

	public void delete(Course course) {
		try {
			logger.debug(DELETE);
			logger.debug(course.toString());
			jdbcTemplate.update(DELETE, course.getId());
			
		} catch (DataAccessException e) {
			throw new DaoException("Can not perform delete method", e);
		}
	}

	public void save(Course course) {
		try {
			logger.debug(SAVE);
			logger.debug("Course before insert: " + course.toString());
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, course.getName());
					return ps;
			}, keyHolder);
			Long id = (Long) keyHolder.getKeys().get("id");
			course.setId(id);
			logger.debug("Course after insert: " + course.toString());
		} catch (DataAccessException e) {
			throw new DaoException("Can not perform save method", e);
		}
	}

	public void update(Course course) {
		try {
			logger.debug(UPDATE);
			logger.debug(course.toString());
			jdbcTemplate.update(UPDATE, course.getName(), course.getId());
		} catch (DataAccessException e) {
			throw new DaoException("Can not perform update method", e);
		}
	}

	public List<Course> findCoursePrerequisites(Course course) {
		try {
			logger.debug(FIND_COURSE_PREREQUISITES);
			logger.debug(course.toString());
			return jdbcTemplate.query(FIND_COURSE_PREREQUISITES, new Object[] { course.getId() }, courseRowMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Can not perform findCoursePrerequisites method", e);
		}
	}

	public void addCoursePrerequisite(Course course, Course prerequisite) {
		try {
			logger.debug(FIND_COURSE_PREREQUISITES);
			logger.debug("Course: " + course.toString() + " Prerequisite: " + prerequisite);
			jdbcTemplate.update(ADD_COURSE_PREREQUISITE, course.getId(), prerequisite.getId());
		}  catch (DataAccessException e) {
			throw new DaoException("Can not perform addCoursePrerequisite method", e);
		}
	}
}
