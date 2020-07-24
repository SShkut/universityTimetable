package com.foxminded.university_timetable.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.config.TestJdbcConfig;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Timetable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimetableDaoTest {

	@Autowired
	private TimetableDao timetableDao;

	@Test
	void givenExistentTimetableId_whenFindById_thenReturnOptionalOfTimetable() {
		Timetable timetable = new Timetable(2L, "archived");
		List<DailyTimetable> dailyTimetables = Arrays.asList(new DailyTimetable(2L, LocalDate.of(2020, 2, 13)));
		timetable.setDailyTimetables(dailyTimetables);
		Optional<Timetable> expected = Optional.of(timetable);

		Optional<Timetable> actual = timetableDao.findById(timetable.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentTimetableId_whenFindById_thenReturnEmptyOptional() {
		Optional<Timetable> expected = Optional.empty();

		Optional<Timetable> actual = timetableDao.findById(0L);

		assertEquals(expected, actual);
	}

	@Test
	void whenFindAll_thenReturnListOfTimetables() {
		List<Timetable> expected = new ArrayList<>();
		expected.add(new Timetable(1L, "actual"));
		expected.add(new Timetable(2L, "archived"));

		List<Timetable> actual = timetableDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenSave_thenInsertTimetable() {
		int expected = timetableDao.findAll().size() + 1;

		timetableDao.save(new Timetable("new", new ArrayList<>()));

		int actual = timetableDao.findAll().size();
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenUpdate_thenUpdateTimetable() {
		Timetable timetable = new Timetable(2L, "new");
		List<DailyTimetable> dailyTimetables = Arrays.asList(new DailyTimetable(2L, LocalDate.of(2020, 2, 13)));
		timetable.setDailyTimetables(dailyTimetables);
		Optional<Timetable> expected = Optional.of(timetable);

		timetableDao.update(timetable);

		Optional<Timetable> actual = timetableDao.findById(timetable.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenDelete_thenDeleteTimetable() {
		List<Timetable> expected = new ArrayList<>();
		expected.add(new Timetable(2L, "archived"));

		timetableDao.delete(new Timetable(1L, ""));

		List<Timetable> actual = timetableDao.findAll();
		assertEquals(expected, actual);
	}
}
