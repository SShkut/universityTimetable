package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.dao.SemesterDao;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	private final SemesterDao semesterDao;

	public GroupRowMapper(SemesterDao semesterDao) {
		this.semesterDao = semesterDao;
	}

	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {

		if (rs.isBeforeFirst()) {
			return null;
		}

		Group group = new Group();
		group.setId(rs.getLong("id"));
		group.setName(rs.getString("name"));
		group.setDepartment(rs.getString("department"));
		group.setMajor(rs.getString("major"));

		Optional<Semester> semester = semesterDao.findById(rs.getLong("semester_id"));
		group.setSemester(semester.orElseThrow(NoSuchElementException::new));
		return group;
	}
}
