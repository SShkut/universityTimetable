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
		Semester semester = new Semester();
		if (rs.isBeforeFirst()) {
			return null;
		}
		
		group.setId(rs.getLong("id"));
		group.setName(rs.getString("name"));
		group.setDepartment(rs.getString("department"));
		group.setMajor(rs.getString("major"));
		semester.setId(Long.valueOf(rs.getLong("semester_id")));
		semester.setYearOfStudy(rs.getInt("year_of_study"));
		semester.setPeriod(rs.getString("period"));
		group.setSemester(semester);
		
		return group;
	}
}
