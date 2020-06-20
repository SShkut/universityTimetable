package com.foxminded.university_timetable.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.RoomRowMapper;
import com.foxminded.university_timetable.model.Room;

@Repository
public class RoomDao {
	private static final String FIND_BY_ID = "SELECT * FROM rooms WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM rooms";
	private static final String SAVE = "INSERT INTO rooms (symbol, capacity) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE rooms SET symbol = ?, capacity = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM rooms WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final RoomRowMapper roomRowMapper;

	public RoomDao(JdbcTemplate jdbcTemplate, RoomRowMapper roomRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.roomRowMapper = roomRowMapper;
	}

	public Optional<Room> findById(Long id) {
		try {
			Room room = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, roomRowMapper);
			return Optional.of(room);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<Room> findAll() {
		return jdbcTemplate.query(FIND_ALL, roomRowMapper);
	}

	public void save(Room room) {
		jdbcTemplate.update(SAVE, room.getSybmol(), room.getCapacity());
	}

	public void update(Room room) {
		jdbcTemplate.update(UPDATE, room.getSybmol(), room.getCapacity(), room.getId());
	}

	public void delete(Room room) {
		jdbcTemplate.update(DELETE, room.getId());
	}
}
