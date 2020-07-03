package com.foxminded.university_timetable.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.TimeSlotRowMapper;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;

@Repository
public class TimeSlotDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

	private static final String FIND_BY_ID = "SELECT * FROM time_slots WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM time_slots";
	private static final String SAVE = "INSERT INTO time_slots (start_time, end_time, course_id, teacher_id, group_id, room_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE time_slots SET start_time = ?, end_time = ?, course_id = ?, teacher_id = ?, group_id = ?, room_id = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM time_slots WHERE id = ?";
	private static final String FIND_ALL_DAILY_TIMETABLE_TIME_SLOTS = "SELECT * FROM time_slots WHERE daily_timetable_id = ?";
	private static final String ADD_TIME_SLOT_TO_DAILY_TIMETABLE = "UPDATE time_slots SET daily_timetable_id = ? WHERE id = ?";
	private static final String FIND_BY_TEACHER_AND_TIME = "SELECT * FROM time_slots WHERE daily_timetable_id = ? AND start_time = ? AND end_time = ? AND teacher_id = ?";
	private static final String FIND_BY_GROUP_AND_TIME = "SELECT * FROM time_slots WHERE daily_timetable_id = ? AND start_time = ? AND end_time = ? AND group_id = ?";
	private static final String FIND_BY_ROOM_AND_TIME = "SELECT * FROM time_slots WHERE daily_timetable_id = ? AND start_time = ? AND end_time = ? AND room_id = ?";


	private final JdbcTemplate jdbcTemplate;
	private final TimeSlotRowMapper timeSlotRowMapper;

	public TimeSlotDao(JdbcTemplate jdbcTemplate, TimeSlotRowMapper timeSlotRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timeSlotRowMapper = timeSlotRowMapper;
	}

	public Optional<TimeSlot> findById(Long id) {
		try {
			logger.debug(FIND_BY_ID + " id = {}", id);
			TimeSlot timeSlot = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id }, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("TimeSlot with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<TimeSlot> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, timeSlotRowMapper);
	}

	public void save(TimeSlot timeSlot) {
		logger.debug(SAVE + " {}", timeSlot.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setTime(1, Time.valueOf(timeSlot.getStartTime()));
			ps.setTime(2, Time.valueOf(timeSlot.getEndTime()));
			ps.setLong(3, timeSlot.getCourse().getId());
			ps.setLong(4, timeSlot.getTeacher().getId());
			ps.setLong(5, timeSlot.getGroup().getId());
			ps.setLong(6, timeSlot.getRoom().getId());
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		timeSlot.setId(id);
	}

	public void update(TimeSlot timeSlot) {
		logger.debug(UPDATE + " {}", timeSlot.toString());
		jdbcTemplate.update(UPDATE, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getCourse().getId(),
				timeSlot.getTeacher().getId(), timeSlot.getGroup().getId(), timeSlot.getRoom().getId(),
				timeSlot.getId());
	}

	public void delete(TimeSlot timeSlot) {
		logger.debug(DELETE + " {}", timeSlot.toString());
		jdbcTemplate.update(DELETE, timeSlot.getId());
	}

	public List<TimeSlot> findAllDailyTimetableTimeSlots(DailyTimetable dailyTimetable) {
		logger.debug(FIND_ALL_DAILY_TIMETABLE_TIME_SLOTS + " {}", dailyTimetable.toString());
		return jdbcTemplate.query(FIND_ALL_DAILY_TIMETABLE_TIME_SLOTS, new Object[] { dailyTimetable.getId() },
				timeSlotRowMapper);
	}

	public void addTimeSlotToDailyTimetable(TimeSlot timeSlot, DailyTimetable dailyTimetable) {
		logger.debug(ADD_TIME_SLOT_TO_DAILY_TIMETABLE + " timeSlot: {}, dailyTimetable: {}", timeSlot.toString(),
				dailyTimetable.toString());
		jdbcTemplate.update(ADD_TIME_SLOT_TO_DAILY_TIMETABLE, dailyTimetable.getId(), timeSlot.getId());
	}

	public Optional<TimeSlot> findByTeacherAndTime(DailyTimetable dailyTimetable, LocalTime startTime, LocalTime endTime, Teacher teacher) {
		try {
			logger.debug(FIND_BY_TEACHER_AND_TIME + " dailyTimetable: {}, startTime: {}, endTime: {}, teacher: {}",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), teacher.toString());
			TimeSlot timeSlot = jdbcTemplate.queryForObject(FIND_BY_TEACHER_AND_TIME, new Object[] { dailyTimetable.getId(), startTime, endTime, teacher.getId() }, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Time Slot for dailyTimetable: {}, startTime: {}, endTime: {}, teacher: {} does not exist",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), teacher.toString());
			return Optional.empty();
		}
	}

	public Optional<TimeSlot> findByGroupAndTime(DailyTimetable dailyTimetable, LocalTime startTime, LocalTime endTime, Group group) {
		try {
			logger.debug(FIND_BY_GROUP_AND_TIME + " dailyTimetable: {}, startTime: {}, endTime: {}, group: {}",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), group.toString());
			TimeSlot timeSlot = jdbcTemplate.queryForObject(FIND_BY_GROUP_AND_TIME, new Object[] {dailyTimetable.getId(), startTime, endTime, group.getId()}, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Time Slot for dailyTimetable: {}, startTime: {}, endTime: {}, group: {} does not exist",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), group.toString());
			return Optional.empty();
		}
	}

	public Optional<TimeSlot> findByRoomAndTime(DailyTimetable dailyTimetable, LocalTime startTime, LocalTime endTime, Room room) {
		try {
			logger.debug(FIND_BY_GROUP_AND_TIME + " dailyTimetable: {}, startTime: {}, endTime: {}, room: {}",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), room.toString());
			TimeSlot timeSlot = jdbcTemplate.queryForObject(FIND_BY_ROOM_AND_TIME, new Object[] {dailyTimetable.getId(), startTime, endTime, room.getId()}, timeSlotRowMapper);
			return Optional.of(timeSlot);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Time Slot for dailyTimetable: {}, startTime: {}, endTime: {}, room: {} does not exist",
					dailyTimetable.toString(), startTime.toString(), endTime.toString(), room.toString());
			return Optional.empty();
		}
	}
}
