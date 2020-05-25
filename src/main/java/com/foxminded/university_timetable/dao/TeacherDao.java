package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Autowired
	public TeacherDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Optional<Teacher> findById(Long id) {
		try {
			Teacher teacher = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, new TeacherRowMapper());
			return Optional.of(teacher);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Teacher> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, new TeacherRowMapper());
	}
	
	public void save(Teacher teahcer) {
		this.jdbcTemplate.update(SAVE, teahcer.getFirstName(), teahcer.getLastName(), teahcer.getTaxNumber(), 
				teahcer.getPhoneNumber(), teahcer.getEmail(), teahcer.getDegree());
	}
	
	public void update(Teacher teahcer) {
		this.jdbcTemplate.update(UPDATE, teahcer.getFirstName(), teahcer.getLastName(), teahcer.getTaxNumber(), 
				teahcer.getPhoneNumber(), teahcer.getEmail(), teahcer.getDegree(), teahcer.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
	
	public void addTeacherQualification(Teacher teacher, Course course) {
		this.jdbcTemplate.update(ADD_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}
	
	public void deleteTeacherQualification(Teacher teacher, Course course) {
		this.jdbcTemplate.update(DELETE_TEACHER_QUALIFICATION, teacher.getId(), course.getId());
	}

	public List<Course> findAllTeacherQualifications(Teacher teacher) {
		return this.jdbcTemplate.query(FIND_ALL_TEACHER_QUALIFICATIONS, new Object[] {teacher.getId()}, new CourseRowMapper());
	}
}
