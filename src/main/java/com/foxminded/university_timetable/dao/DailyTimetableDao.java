package com.foxminded.university_timetable.dao;

import java.sql.Types;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
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

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.Timetable;
import com.foxminded.university_timetable.row_mapper.DailyTimetableRowMapper;

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
			+ "JOIN teachers t ON t.id = ts.teacher_id and t.id = ? " + "GROUP BY dt.id, dt.date, dt.timetable_id";;

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
		return this.jdbcTemplate.query(FIND_ALL, dailyTimetableRowMapper);
	}

	public DailyTimetable save(DailyTimetable dailyTimetable) {
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(SAVE, Types.DATE);
		factory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(Arrays.asList(dailyTimetable.getDate()));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(psc, keyHolder);
		Long newId;
		if (keyHolder.getKeys().size() > 1) {
			newId = Long.parseLong(String.valueOf(keyHolder.getKeys().get("id")));
		} else {
			newId = keyHolder.getKey().longValue();
		}
		dailyTimetable.setId(newId);
		return dailyTimetable;
	}

	public DailyTimetable update(DailyTimetable dailyTimetable) {
		this.jdbcTemplate.update(UPDATE, dailyTimetable.getDate(), dailyTimetable.getId());
		return dailyTimetable;
	}

	public void deleteById(Long id) {
		this.jdbcTemplate.update(DELETE_BY_ID, id);
	}

	public void addDailyTimetableToTimetable(DailyTimetable dailyTimetable, Timetable timetable) {
		this.jdbcTemplate.update(ADD_DAILY_TIMETABLE_TO_TIMETABLE, timetable.getId(), dailyTimetable.getId());
	}

	public Optional<DailyTimetable> findByDate(LocalDate date) {
		try {
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_BY_DATE, new Object[] { date },
					dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<DailyTimetable> findDailyTimetableForStudent(Student student, LocalDate date) {
		try {
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_DAILY_TIMETABLE_FOR_STUDENT,
					new Object[] { date, student.getId() }, dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<DailyTimetable> findDailyTimetableForTeacher(Teacher teacher, LocalDate date) {
		try {
			DailyTimetable dailyTimetable = this.jdbcTemplate.queryForObject(FIND_DAILY_TIMETABLE_FOR_TEACHER,
					new Object[] { date, teacher.getId() }, dailyTimetableRowMapper);
			return Optional.of(dailyTimetable);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public List<DailyTimetable> findMonthlyTimetableForStudent(Student student, Month month, int year) {
		return this.jdbcTemplate.query(FIND_MONTHLY_TIMETABLE_FOR_STUDENT,
				new Object[] { YearMonth.of(year, month).atDay(1),
						YearMonth.of(year, month).atDay(YearMonth.of(year, month).lengthOfMonth()), student.getId() },
				dailyTimetableRowMapper);
	}

	public List<DailyTimetable> findMonthlyTimetableForTeacher(Teacher teacher, Month month, int year) {
		return this.jdbcTemplate.query(FIND_MONTHLY_TIMETABLE_FOR_TEACHER,
				new Object[] { YearMonth.of(year, month).atDay(1),
						YearMonth.of(year, month).atDay(YearMonth.of(year, month).lengthOfMonth()), teacher.getId() },
				dailyTimetableRowMapper);
	}
}
