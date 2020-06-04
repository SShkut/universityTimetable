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

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.row_mapper.CourseRowMapper;
import com.foxminded.university_timetable.row_mapper.TeacherRowMapper;

@Repository
public class TeacherDao {

	private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM teachers";
	private static final String SAVE = "INSERT INTO teachers (first_name, last_name, tax_number, phone_number, email, degree) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, tax_number = ?, phone_number = ?, email = ?, degree = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";
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
			Teacher teacher = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, teacherRowMapper);
			List<Course> qualification = findAllTeacherQualifications(teacher);
			teacher.setCourses(qualification);
			return Optional.of(teacher);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Teacher> findAll() {
		return jdbcTemplate.query(FIND_ALL, teacherRowMapper);
	}

	public Teacher save(Teacher teacher) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory
				.newPreparedStatementCreator(Arrays.asList(teacher.getFirstName(), teacher.getLastName(),
						teacher.getTaxNumber(), teacher.getPhoneNumber(), teacher.getEmail(), teacher.getDegree()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		teacher.setId(newId);
		return teacher;
	}

	public Teacher update(Teacher teacher) {
		jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getTaxNumber(),
				teacher.getPhoneNumber(), teacher.getEmail(), teacher.getDegree(), teacher.getId());
		return teacher;
	}

	public void delete(Teacher teacher) {
		jdbcTemplate.update(DELETE_BY_ID, teacher.getId());
	}

	public void addTeacherQualification(Teacher teacher, Course course) {
		jdbcTemplate.update(ADD_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}

	public void deleteTeacherQualification(Teacher teacher, Course course) {
		jdbcTemplate.update(DELETE_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}

	public List<Course> findAllTeacherQualifications(Teacher teacher) {
		return jdbcTemplate.query(FIND_ALL_TEACHER_QUALIFICATIONS, new Object[] { teacher.getId() },
				courseRowMapper);
	}
}
