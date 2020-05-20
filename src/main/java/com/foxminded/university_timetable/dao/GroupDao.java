package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.row_mapper.GroupRowMapper;
import com.foxminded.university_timetable.row_mapper.StudentRowMapper;
;

public class GroupDao {
	
	private static final String FIND_ALL= "SELECT g.id, g.name, g.major, g.department, g.semester_id, s.year_of_study, s.period "
			+ "FROM groups g "
			+ "JOIN semesters s ON s.id = g.semester_id";
	private static final String FIND_BY_ID = "SELECT g.id, g.name, g.major, g.department, g.semester_id, s.year_of_study, s.period "
			+ "FROM groups g "
			+ "JOIN semesters s ON s.id = g.semester_id AND g.id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";
	private static final String SAVE = "INSERT INTO groups (name, major, department, semester_id) values(?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE groups SET name = ?, major = ?, department = ?, semester_id = ? WHERE id = ?";
	private static final String FIND_STUDENTS_OF_GROUP = "SELECT s.id, s.first_name, s.last_name, s.tax_number, s.phone_number, s.email, s.student_card_number "
			+ "FROM students s "
			+ "JOIN student_group sg ON s.id = sg.student_id and sg.group_id = ?";
	
	private final JdbcTemplate jdbcTemplate;
	
	public GroupDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Optional<Group> findById(Long id) {
		try {
			Group group = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, new GroupRowMapper());
			return Optional.of(group);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Group> findAll() {
		return  jdbcTemplate.query(FIND_ALL, new GroupRowMapper());
	}
	
	public void delteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
	
	public void save(Group group) {
		this.jdbcTemplate.update(SAVE, group.getName(), group.getMajor(), group.getDepartment(), group.getSemester().getId());
	}
	
	public void update(Group group) {
		this.jdbcTemplate.update(UPDATE, group.getName(), group.getMajor(), group.getDepartment(), group.getSemester().getId(), group.getId());
	}
	
	public List<Student> findStudentsOfGroup(Group group) {
		return this.jdbcTemplate.query(FIND_STUDENTS_OF_GROUP, new Object[] {group.getId()}, new StudentRowMapper());
	}
}
