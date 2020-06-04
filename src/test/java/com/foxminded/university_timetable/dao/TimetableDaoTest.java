package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.model.Timetable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimetableDaoTest {

	@Autowired
	private TimetableDao timetableDao;
	
	@Autowired
	private TimeSlotDao timeSlotDao;
	
	@Test
	void givenTimetable_whenFindDailyTimetablesOfTimetable_thenReturnListOfDailyTimetables() {
		Timetable timetable = new Timetable(1L, "actual", null);
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12), null);
		List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
		dailyTimetable.setTimeSlots(timeSlots);
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetable);
		
		List<DailyTimetable> actual = timetableDao.findDailyTimetablesOfTimetable(timetable);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentTimetableId_whenFindById_thenReturnOptionalOfTimetable() {
		Timetable timetable = new Timetable(1L, "actual", null);
		List<DailyTimetable> dailyTimetables = timetableDao.findDailyTimetablesOfTimetable(timetable);
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
		expected.add(new Timetable(1L, "actual", null));
		expected.add(new Timetable(2L, "archived", null));
		
		List<Timetable> actual = timetableDao.findAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenSave_thenInsertTimetable() {
		Timetable timetable = new Timetable(null, "new", null);
		List<DailyTimetable> dailyTimetables = timetableDao.findDailyTimetablesOfTimetable(timetable);
		timetable.setDailyTimetables(dailyTimetables);
		
		Timetable inserted = timetableDao.save(timetable);
		
		Optional<Timetable> expected = Optional.of(inserted);
		Optional<Timetable> actual = timetableDao.findById(timetable.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimetable_whenUpdate_thenUpdateTimetable() {
		Timetable timetable = new Timetable(1L, "new", null);
		List<DailyTimetable> dailyTimetables = timetableDao.findDailyTimetablesOfTimetable(timetable);
		timetable.setDailyTimetables(dailyTimetables);
		Optional<Timetable> expected = Optional.of(timetable);
		
		timetableDao.update(timetable);
		
		Optional<Timetable> actual = timetableDao.findById(timetable.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimetable_whenDelete_thenDeleteTimetable() {
		List<Timetable> expected = new ArrayList<>();
		expected.add(new Timetable(2L, "archived", null));
		
		timetableDao.delete(new Timetable(1L, null, null));
		
		List<Timetable> actual = timetableDao.findAll();
		assertEquals(expected, actual);
	}
}
