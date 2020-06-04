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

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.row_mapper.GroupRowMapper;
import com.foxminded.university_timetable.row_mapper.StudentRowMapper;

@Repository
public class GroupDao {

	private static final String FIND_ALL = "SELECT * FROM groups";
	private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";
	private static final String SAVE = "INSERT INTO groups (name, major, department, semester_id) values(?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE groups SET name = ?, major = ?, department = ?, semester_id = ? WHERE id = ?";
	private static final String FIND_STUDENTS_OF_GROUP = "SELECT s.id, s.first_name, s.last_name, s.tax_number, s.phone_number, s.email, s.student_card_number "
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
			Group group = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, groupRowMapper);
			List<Student> students = findStudentsOfGroup(group);
			group.setStudents(students);
			return Optional.of(group);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Group> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, groupRowMapper);
	}

	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}

	public Group save(Group group) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.INTEGER);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(
				Arrays.asList(group.getName(), group.getMajor(), group.getDepartment(), group.getSemester().getId()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		group.setId(newId);
		return group;
	}

	public Group update(Group group) {
		this.jdbcTemplate.update(UPDATE, group.getName(), group.getMajor(), group.getDepartment(),
				group.getSemester().getId(), group.getId());
		return group;
	}

	public List<Student> findStudentsOfGroup(Group group) {
		return this.jdbcTemplate.query(FIND_STUDENTS_OF_GROUP, new Object[] { group.getId() }, studentRowMapper);
	}
}
