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
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.row_mapper.CourseRowMapper;
import com.foxminded.university_timetable.row_mapper.StudentRowMapper;

@Repository
public class StudentDao {

	private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM students";
	private static final String SAVE = "INSERT INTO students (first_name, last_name, tax_number, phone_number, email, student_card_number) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, tax_number = ?, phone_number = ?, email = ?, student_card_number = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";
	private static final String ADD_STUDENT_TO_GROUP = "INSERT INTO student_group (student_id, group_id) values (?, ?)";
	private static final String DELETE_STUDENT_FROM_GROUP = "DELETE FROM student_group WHERE student_id = ? and group_id = ?";
	private static final String ENROLL_COURSE = "INSERT INTO student_course (student_id, course_id) values (?, ?)";
	private static final String LEAVE_COURSE = "DELETE FROM student_course WHERE student_id = ? AND course_id = ?";
	private static final String FIND_ALL_STUDENT_COURSES = "SELECT c.id, c.name " + "FROM courses c "
			+ "JOIN student_course sc ON c.id = sc.course_id AND sc.student_id = ?";
	private static final String FIND_COURSE_STUDENTS = "SELECT s.id, s.first_name, s.last_name, s.tax_number, s.phone_number, s.email, s.student_card_number "
			+ "FROM students s " + "JOIN student_course sc ON sc.student_id = s.id AND sc.course_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final StudentRowMapper studentRowMapper;
	private final CourseRowMapper courseRowMapper;

	public StudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper studentRowMapper, CourseRowMapper courseRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.studentRowMapper = studentRowMapper;
		this.courseRowMapper = courseRowMapper;
	}

	public Optional<Student> findById(Long id) {
		try {
			Student student = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, studentRowMapper);
			List<Course> courses = findAllStudentCourses(student);
			student.setCourses(courses);
			return Optional.of(student);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Student> findAll() {
		return jdbcTemplate.query(FIND_ALL, studentRowMapper);
	}

	public Student save(Student student) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(
				Arrays.asList(student.getFirstName(), student.getLastName(), student.getTaxNumber(),
						student.getPhoneNumber(), student.getEmail(), student.getStudentCardNumber()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		student.setId(newId);
		return student;
	}

	public Student update(Student student) {
		jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(), student.getTaxNumber(),
				student.getPhoneNumber(), student.getEmail(), student.getStudentCardNumber(), student.getId());
		return student;
	}

	public void deleteById(Long id) {
		jdbcTemplate.update(DELETE_BY_ID, id);
	}

	public void addStudentToGroup(Student student, Group group) {
		jdbcTemplate.update(ADD_STUDENT_TO_GROUP, student.getId(), group.getId());
	}

	public void deleteStudentFromGroup(Student student, Group group) {
		jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP, student.getId(), group.getId());
	}

	public void addStudentToCourse(Student student, Course course) {
		jdbcTemplate.update(ENROLL_COURSE, student.getId(), course.getId());
	}

	public void deleteStudentFromCourse(Student student, Course course) {
		jdbcTemplate.update(LEAVE_COURSE, student.getId(), course.getId());
	}

	public List<Course> findAllStudentCourses(Student student) {
		return jdbcTemplate.query(FIND_ALL_STUDENT_COURSES, new Object[] { student.getId() }, courseRowMapper);
	}

	public List<Student> findCourseStudents(Course course) {
		return jdbcTemplate.query(FIND_COURSE_STUDENTS, new Object[] { course.getId() }, studentRowMapper);
	}
}
