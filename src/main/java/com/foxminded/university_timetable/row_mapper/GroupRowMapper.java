package com.foxminded.university_timetable.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;

public class GroupRowMapper implements RowMapper<Group> {
	
	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group();
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		group.setId(Long.valueOf(rs.getLong("id")));
		group.setName(rs.getString("name"));
		group.setDepartment(rs.getString("department"));
		group.setMajor(rs.getString("major"));
		return group;
	}
}
