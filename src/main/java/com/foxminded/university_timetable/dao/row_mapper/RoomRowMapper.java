package com.foxminded.university_timetable.dao.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.model.Room;

@Component
public class RoomRowMapper implements RowMapper<Room> {

	@Override
	public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
		Room room = new Room();
		room.setId(rs.getLong("id"));
		room.setSybmol(rs.getString("symbol"));
		room.setCapacity(rs.getInt("capacity"));
		return room;
	}
}
