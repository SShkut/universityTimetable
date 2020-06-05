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

import com.foxminded.university_timetable.dao.row_mapper.GroupRowMapper;
import com.foxminded.university_timetable.dao.row_mapper.StudentRowMapper;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;

@Repository
public class GroupDao {

	private static final String FIND_ALL = "SELECT * FROM groups";
	private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";
	private static final String SAVE = "INSERT INTO groups (name, major, department, semester_id) values(?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE groups SET name = ?, major = ?, department = ?, semester_id = ? WHERE id = ?";
	private static final String FIND_GROUP_STUDENTS = "SELECT s.id, s.first_name, s.last_name, s.tax_number, s.phone_number, s.email, s.student_card_number "
			+ "FROM students s " 
			+ "JOIN student_group sg ON s.id = sg.student_id AND sg.group_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final GroupRowMapper groupRowMapper;
	private final StudentRowMapper studentRowMapper;

	public GroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper groupRowMapper, StudentRowMapper studentRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.groupRowMapper = groupRowMapper;
		this.studentRowMapper = studentRowMapper;
	}

	public Optional<Group> findById(Long id) {
		try {
			Group group = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, groupRowMapper);
			List<Student> students = findGroupStudents(group);
			group.setStudents(students);
			return Optional.of(group);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Group> findAll() {
		return jdbcTemplate.query(FIND_ALL, groupRowMapper);
	}

	public void delete(Group group) {
		jdbcTemplate.update(DELETE_BY_ID, group.getId());
	}

	public Group save(Group group) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, group.getName());
			ps.setString(2, group.getMajor());
			ps.setString(3, group.getDepartment());
			ps.setLong(4, group.getSemester().getId());
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		group.setId(id);
		return group;
	}

	public void update(Group group) {
		jdbcTemplate.update(UPDATE, group.getName(), group.getMajor(), group.getDepartment(),
				group.getSemester().getId(), group.getId());
	}

	public List<Student> findGroupStudents(Group group) {
		return jdbcTemplate.query(FIND_GROUP_STUDENTS, new Object[] { group.getId() }, studentRowMapper);
	}
}
