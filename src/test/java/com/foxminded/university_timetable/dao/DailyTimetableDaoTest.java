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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class DailyTimetableDaoTest {

	@Autowired
	private DailyTimetableDao dailyTimetableDao;

	@Test
	void givenExistentDailyTimetableId_whenFindById_thenReturnOptionalOfDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12), null);
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(dailyTimetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenNonExistentDailyTimetalbeId_whenFindById_therReturnEmptOptional() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(0L);
		
		assertEquals(expected, actual);		
	}
	
	@Test
	void whenFindAll_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L , LocalDate.of(2020, 2, 12), null));
		expected.add(new DailyTimetable(2L , LocalDate.of(2020, 2, 13), null));
		
		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenDailyTimetable_whenSave_thenInsertDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(3L, LocalDate.of(2020, 3, 1), null);
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		
		dailyTimetableDao.save(dailyTimetable);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(dailyTimetable.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenDailyTimetable_whenUpdate_thenUpdateGivenTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 02, 15), null);
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		
		dailyTimetableDao.update(dailyTimetable);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(dailyTimetable.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenDailyTimetableId_whenDeleteById_thenDeleteGivenTiemtable() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L, LocalDate.of(2020, 2, 12), null));
		
		dailyTimetableDao.deleteById(2L);
		
		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		assertEquals(expected, actual);
	}
}
