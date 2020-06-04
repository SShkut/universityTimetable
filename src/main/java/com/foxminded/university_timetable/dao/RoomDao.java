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

	public Room save(Room room) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.VARCHAR,
				Types.INTEGER);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory
				.newPreparedStatementCreator(Arrays.asList(room.getSybmol(), room.getCapacity()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		room.setId(newId);
		return room;
	}

	public Room update(Room room) {
		jdbcTemplate.update(UPDATE, room.getSybmol(), room.getCapacity(), room.getId());
		return room;
	}

	public void delete(Room room) {
		jdbcTemplate.update(DELETE_BY_ID, room.getId());
	}
}
