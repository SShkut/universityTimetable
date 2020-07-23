package com.foxminded.university_timetable.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.DailyTimetableRowMapper;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.model.Timetable;

@Repository
public class DailyTimetableDao {

	Logger logger = LoggerFactory.getLogger(DailyTimetableDao.class);

	private static final String FIND_BY_ID = "SELECT * FROM daily_timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM daily_timetables";
	private static final String SAVE = "INSERT INTO daily_timetables (date) VALUES (?)";
	private static final String UPDATE = "UPDATE daily_timetables SET date = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM daily_timetables WHERE id = ?";
	private static final String ADD_DAILY_TIMETABLE_TO_TIMETABLE = "UPDATE daily_timetables SET timetable_id = ? WHERE id = ?";
	private static final String FIND_BY_DATE = "SELECT * FROM daily_timetables WHERE date = ?";
	private static final String FIND_TIMETABLE_FOR_STUDENT = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt "
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date BETWEEN ? AND ? "
			+ "JOIN groups g ON ts.group_id = g.id "
			+ "JOIN student_group sg ON sg.group_id = g.id and sg.student_id = ?"
			+ "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_TIMETABLE_FOR_TEACHER = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt "
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date BETWEEN ? AND ? "
			+ "JOIN teachers t ON t.id = ts.teacher_id and t.id = ? " + "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_TIMETABLE_DAILY_TIMETABLES = "SELECT * FROM daily_timetables WHERE timetable_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final DailyTimetableRowMapper dailyTimetableRowMapper;
	private final TimeSlotDao timeSlotDao;

	public DailyTimetableDao(JdbcTemplate jdbcTemplate, DailyTimetableRowMapper dailyTimetableRowMapper,
			TimeSlotDao timeSlotDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.dailyTimetableRowMapper = dailyTimetableRowMapper;
		this.timeSlotDao = timeSlotDao;
	}

	public Optional<DailyTimetable> findById(Long id) {
		try {
			logger.debug(FIND_BY_ID + " id = {}", id);
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id },
					dailyTimetableRowMapper);
			List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
			dailyTimetable.setTimeSlots(timeSlots);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Daily timetable with id = {} does not exist", id);
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findAll() {
		logger.debug(FIND_ALL);
		return jdbcTemplate.query(FIND_ALL, dailyTimetableRowMapper);
	}

	public void save(DailyTimetable dailyTimetable) {
		logger.debug(SAVE + " {}", dailyTimetable.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, Date.valueOf(dailyTimetable.getDate()));
			return ps;
		}, keyHolder);
		Long id = (Long) keyHolder.getKeys().get("id");
		dailyTimetable.setId(id);
	}

	public void update(DailyTimetable dailyTimetable) {
		logger.debug(UPDATE + " {}", dailyTimetable.toString());
		jdbcTemplate.update(UPDATE, dailyTimetable.getDate(), dailyTimetable.getId());
	}

	public void delete(DailyTimetable dailyTimetable) {
		logger.debug(DELETE + " {}", dailyTimetable.toString());
		jdbcTemplate.update(DELETE, dailyTimetable.getId());
	}

	public void addDailyTimetableToTimetable(DailyTimetable dailyTimetable, Timetable timetable) {
		logger.debug(ADD_DAILY_TIMETABLE_TO_TIMETABLE + " dailyTimetable = {}, timetable = {}",
				dailyTimetable.toString(), timetable.toString());
		jdbcTemplate.update(ADD_DAILY_TIMETABLE_TO_TIMETABLE, timetable.getId(), dailyTimetable.getId());
	}

	public Optional<DailyTimetable> findByDate(LocalDate date) {
		try {
			logger.debug(FIND_BY_DATE + " date: {}", date);
			DailyTimetable dailyTimetable = jdbcTemplate.queryForObject(FIND_BY_DATE, new Object[] { date },
					dailyTimetableRowMapper);
			List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
			dailyTimetable.setTimeSlots(timeSlots);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Daily timetable for date: {} does not exist", date);
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findTimetableForStudent(Student student, LocalDate start, LocalDate end) {
		logger.debug(FIND_TIMETABLE_FOR_STUDENT + " student: {}, start date: {}, end date: {}", student.toString(),
				start, end);
		return jdbcTemplate.query(FIND_TIMETABLE_FOR_STUDENT, new Object[] { start, end, student.getId() },
				dailyTimetableRowMapper);
	}

	public List<DailyTimetable> findTimetableForTeacher(Teacher teacher, LocalDate start, LocalDate end) {
		logger.debug(FIND_TIMETABLE_FOR_TEACHER + " teacher: {}, start date: {}, end date: {}", teacher.toString(),
				start, end);
		return jdbcTemplate.query(FIND_TIMETABLE_FOR_TEACHER, new Object[] { start, end, teacher.getId() },
				dailyTimetableRowMapper);
	}

	public List<DailyTimetable> findTimetableDailyTimetables(Timetable timetable) {
		logger.debug(FIND_TIMETABLE_DAILY_TIMETABLES + " timetable: ", timetable.toString());
		return jdbcTemplate.query(FIND_TIMETABLE_DAILY_TIMETABLES, new Object[] { timetable.getId() },
				dailyTimetableRowMapper);
	}
}
