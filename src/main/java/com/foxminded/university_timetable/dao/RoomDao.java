package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.RoomRowMapper;
import com.foxminded.university_timetable.model.Room;

@Repository
public class RoomDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

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
			logger.debug(FIND_BY_ID + " id = {}", id);
			Room room = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, roomRowMapper);
			return Optional.of(room);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Room with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<Room> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, roomRowMapper);
	}

	public void save(Room room) {
		logger.debug(SAVE + " {}", room.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, room.getSybmol());
			ps.setInt(2, room.getCapacity());
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		room.setId(id);
	}

	public void update(Room room) {
		logger.debug(UPDATE + " {}", room.toString());
		jdbcTemplate.update(UPDATE, room.getSybmol(), room.getCapacity(), room.getId());
	}

	public void delete(Room room) {
		logger.debug(DELETE + " {}", room.toString());
		jdbcTemplate.update(DELETE, room.getId());
	}
}
