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

import com.foxminded.university_timetable.dao.row_mapper.CourseRowMapper;
import com.foxminded.university_timetable.dao.row_mapper.TeacherRowMapper;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Teacher;

@Repository
public class TeacherDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

	private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM teachers";
	private static final String SAVE = "INSERT INTO teachers (first_name, last_name, tax_number, phone_number, email, degree) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, tax_number = ?, phone_number = ?, email = ?, degree = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";
	private static final String ADD_TEACHER_QUALIFICATION = "INSERT INTO teacher_course (teacher_id, course_id) VALUES (?, ?)";
	private static final String DELETE_TEACHER_QUALIFICATION = "DELETE FROM teacher_course WHERE teacher_id = ? AND course_id = ?";
	private static final String FIND_ALL_TEACHER_QUALIFICATIONS = "SELECT c.id, c.name "
			+ "FROM courses c "
			+ "JOIN teacher_course tc ON c.id = tc.course_id AND tc.teacher_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final CourseRowMapper courseRowMapper;
	private final TeacherRowMapper teacherRowMapper;

	public TeacherDao(JdbcTemplate jdbcTemplate, CourseRowMapper courseRowMapper, TeacherRowMapper teacherRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.courseRowMapper = courseRowMapper;
		this.teacherRowMapper = teacherRowMapper;
	}

	public Optional<Teacher> findById(Long id) {
		try {
			logger.debug(FIND_BY_ID + " id = {}", id);
			Teacher teacher = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, teacherRowMapper);
			List<Course> qualification = findAllTeacherQualifications(teacher);
			teacher.setCourses(qualification);
			return Optional.of(teacher);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Teacher with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<Teacher> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, teacherRowMapper);
	}

	public void save(Teacher teacher) {
		logger.debug(SAVE + " {}", teacher.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, teacher.getFirstName());
			ps.setString(2, teacher.getLastName());
			ps.setString(3, teacher.getTaxNumber());
			ps.setString(4, teacher.getPhoneNumber());
			ps.setString(5, teacher.getEmail());
			ps.setString(6, teacher.getDegree());
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		teacher.setId(id);
	}

	public void update(Teacher teacher) {
		logger.debug(UPDATE + " {}", teacher.toString());
		jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getTaxNumber(),
				teacher.getPhoneNumber(), teacher.getEmail(), teacher.getDegree(), teacher.getId());
	}

	public void delete(Teacher teacher) {
		logger.debug(DELETE + " {}", teacher.toString());
		jdbcTemplate.update(DELETE, teacher.getId());
	}

	public void addTeacherQualification(Teacher teacher, Course course) {
		logger.debug(ADD_TEACHER_QUALIFICATION + " teacher: {}, course: {}", teacher.toString(), course.toString());
		jdbcTemplate.update(ADD_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}

	public void deleteTeacherQualification(Teacher teacher, Course course) {
		logger.debug(DELETE_TEACHER_QUALIFICATION + " teacher: {}, course: {}", teacher.toString(), course.toString());
		jdbcTemplate.update(DELETE_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}

	public List<Course> findAllTeacherQualifications(Teacher teacher) {
		logger.debug(FIND_ALL_TEACHER_QUALIFICATIONS + " {}", teacher.toString());
		return jdbcTemplate.query(FIND_ALL_TEACHER_QUALIFICATIONS, new Object[] { teacher.getId() },
				courseRowMapper);
	}
}
