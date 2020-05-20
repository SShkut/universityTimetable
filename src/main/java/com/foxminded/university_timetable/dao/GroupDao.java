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
	
	private static final String FIND_ALL= "SELECT * FROM groups";
	private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";
	private static final String SAVE = "INSERT INTO groups (name, major, department, semester) values(?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE groups SET name = ?, major = ?, department = ?, semester = ? WHERE id = ?";
	private static final String FIND_STUDENTS_OF_GROUP = "SELECT * FROM students WHERE group_id = ?";
	
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
		this.jdbcTemplate.update(SAVE, group.getName(), group.getMajor(), group.getDepartment(), group.getSemester());
	}
	
	public void update(Group group) {
		this.jdbcTemplate.update(UPDATE, group.getName(), group.getMajor(), group.getDepartment(), group.getSemester(), group.getId());
	}
	
	public List<Student> findStudentsOfGroup(Group group) {
		return this.jdbcTemplate.query(FIND_STUDENTS_OF_GROUP, new Object[] {group.getId()}, new StudentRowMapper());
	}
}
