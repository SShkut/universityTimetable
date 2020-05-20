package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.row_mapper.StudentRowMapper;

public class StudentDao {
	
	private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM students";
	private static final String SAVE = "INSERT INTO students (first_name, last_name, tax_number, phone_number, email, student_card_number) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, tax_number = ?, phone_number = ?, email = ?, student_card_number = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";
	private static final String ADD_STUDENT_TO_GROUP = "INSERT INTO student_group (student_id, group_id) values (?, ?)";
	private static final String DELETE_STUDENT_FROM_GROUP = "DELETE FROM student_group WHERE student_id = ? and group_id = ?";
	
	private final JdbcTemplate jdbcTemplate;
	
	public StudentDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Optional<Student> findById(Long id) {
		try {
			Student student = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, new StudentRowMapper());
			return Optional.of(student);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Student> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, new StudentRowMapper());
	}
	
	public void save(Student student) {
		this.jdbcTemplate.update(SAVE, student.getFirstName(), student.getLastName(), student.getTaxNumber(), 
				student.getPhoneNumber(), student.getEmail(), student.getStudentCardNumber());
	}
	
	public void update(Student student) {
		this.jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(), student.getTaxNumber(), 
				student.getPhoneNumber(), student.getEmail(), student.getStudentCardNumber(), student.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
	
	public void addStudentToGroup(Student student, Group group) {
		this.jdbcTemplate.update(ADD_STUDENT_TO_GROUP, student.getId(), group.getId());
	}
	
	public void deleteStudentFromGroup(Student student, Group group) {
		this.jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP, student.getId(), group.getId());
	}
}
