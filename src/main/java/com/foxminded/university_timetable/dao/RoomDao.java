package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.row_mapper.RoomRowMapper;

@Repository
public class RoomDao {
	private static final String FIND_BY_ID = "SELECT * FROM rooms WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM rooms";
	private static final String SAVE = "INSERT INTO rooms (symbol, capacity) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE rooms SET symbol = ?, capacity = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM rooms WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final RoomRowMapper roomRowMapper;
	
	@Autowired
	public RoomDao(JdbcTemplate jdbcTemplate, RoomRowMapper roomRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.roomRowMapper = roomRowMapper;
	}
	
	public Optional<Room> findById(Long id) {
		try {
			Room room = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] {id}, roomRowMapper);
			return Optional.of(room);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public List<Room> findAll() {
		return this.jdbcTemplate.query(FIND_ALL, roomRowMapper);
	}
	
	public void save(Room room) {
		this.jdbcTemplate.update(SAVE, room.getSybmol(), room.getCapacity());
	}
	
	public void update(Room room) {
		this.jdbcTemplate.update(UPDATE, room.getSybmol(), room.getCapacity(), room.getId());
	}
	
	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}
}
