package com.foxminded.university_timetable.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foxminded.university_timetable.dao.row_mapper.DailyTimetableRowMapper;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.Timetable;

@Repository
public class DailyTimetableDao {

	private static final String FIND_BY_ID = "SELECT * FROM daily_timetables WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM daily_timetables";
	private static final String SAVE = "INSERT INTO daily_timetables (date) VALUES (?)";
	private static final String UPDATE = "UPDATE daily_timetables SET date = ? WHERE id = ?";
	private static final String DELETE_BY_ID = "DELETE FROM daily_timetables WHERE id = ?";
	private static final String ADD_DAILY_TIMETABLE_TO_TIMETABLE = "UPDATE daily_timetables SET timetable_id = ? WHERE id = ?";
	private static final String FIND_BY_DATE = "SELECT * FROM daily_timetables WHERE date = ?";
	private static final String FIND_DAILY_TIMETABLE_FOR_STUDENT = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt " 
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date = ? "
			+ "JOIN groups g ON ts.group_id = g.id "
			+ "JOIN student_group sg ON sg.group_id = g.id and sg.student_id = ? "
			+ "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_DAILY_TIMETABLE_FOR_TEACHER = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt " 
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date = ? "
			+ "JOIN teachers t ON t.id = ts.teacher_id and t.id = ? " 
			+ "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_MONTHLY_TIMETABLE_FOR_STUDENT = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt "
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date BETWEEN ? AND ? "
			+ "JOIN groups g ON ts.group_id = g.id "
			+ "JOIN student_group sg ON sg.group_id = g.id and sg.student_id = ?"
			+ "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_MONTHLY_TIMETABLE_FOR_TEACHER = "SELECT dt.id, dt.date, dt.timetable_id "
			+ "FROM daily_timetables dt "
			+ "JOIN time_slots ts ON ts.daily_timetable_id = dt.id AND dt.date BETWEEN ? AND ? "
			+ "JOIN teachers t ON t.id = ts.teacher_id and t.id = ? " + "GROUP BY dt.id, dt.date, dt.timetable_id";
	private static final String FIND_TIMETABLE_DAILY_TIMETABLES = "SELECT * FROM daily_timetables WHERE timetable_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final DailyTimetableRowMapper dailyTimetableRowMapper;

	public DailyTimetableDao(JdbcTemplate jdbcTemplate, DailyTimetableRowMapper dailyTimetableRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.dailyTimetableRowMapper = dailyTimetableRowMapper;
	}

	public Optional<DailyTimetable> findById(Long id) {
		try {
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { id },
					dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findAll() {
		return jdbcTemplate.query(FIND_ALL, dailyTimetableRowMapper);
	}

	public DailyTimetable save(DailyTimetable dailyTimetable) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, Date.valueOf(dailyTimetable.getDate()));
			return ps;
		}, keyHolder);
		Long id = keyHolder.getKey().longValue();
		dailyTimetable.setId(id);
		return dailyTimetable;
	}

	public void update(DailyTimetable dailyTimetable) {
		jdbcTemplate.update(UPDATE, dailyTimetable.getDate(), dailyTimetable.getId());
	}

	public void delete(DailyTimetable dailyTimetable) {
		jdbcTemplate.update(DELETE_BY_ID, dailyTimetable.getId());
	}

	public void addDailyTimetableToTimetable(DailyTimetable dailyTimetable, Timetable timetable) {
		jdbcTemplate.update(ADD_DAILY_TIMETABLE_TO_TIMETABLE, timetable.getId(), dailyTimetable.getId());
	}

	public Optional<DailyTimetable> findByDate(LocalDate date) {
		try {
			DailyTimetable dailyTimetable = jdbcTemplate.queryForObject(FIND_BY_DATE, new Object[] { date },
					dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<DailyTimetable> findDailyTimetableForStudent(Student student, LocalDate date) {
		try {
			DailyTimetable dailyTimetable = jdbcTemplate.queryForObject(FIND_DAILY_TIMETABLE_FOR_STUDENT,
					new Object[] { date, student.getId() }, dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<DailyTimetable> findDailyTimetableForTeacher(Teacher teacher, LocalDate date) {
		try {
			DailyTimetable dailyTimetable = jdbcTemplate.queryForObject(FIND_DAILY_TIMETABLE_FOR_TEACHER,
					new Object[] { date, teacher.getId() }, dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findMonthlyTimetableForStudent(Student student, Month month, int year) {
		return jdbcTemplate.query(FIND_MONTHLY_TIMETABLE_FOR_STUDENT,
				new Object[] { YearMonth.of(year, month).atDay(1),
						YearMonth.of(year, month).atDay(YearMonth.of(year, month).lengthOfMonth()), student.getId() },
				dailyTimetableRowMapper);
	}

	public List<DailyTimetable> findMonthlyTimetableForTeacher(Teacher teacher, Month month, int year) {
		return jdbcTemplate.query(FIND_MONTHLY_TIMETABLE_FOR_TEACHER,
				new Object[] { YearMonth.of(year, month).atDay(1),
						YearMonth.of(year, month).atDay(YearMonth.of(year, month).lengthOfMonth()), teacher.getId() },
				dailyTimetableRowMapper);
	}
	
	public List<DailyTimetable> findTimetableDailyTimetables(Timetable timetable) {
		return jdbcTemplate.query(FIND_TIMETABLE_DAILY_TIMETABLES, new Object[] { timetable.getId() },
				dailyTimetableRowMapper);
	}
}
