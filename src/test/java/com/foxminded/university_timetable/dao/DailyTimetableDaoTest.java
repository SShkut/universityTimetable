package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
class DailyTimetableDaoTest {

	@Autowired
	private DailyTimetableDao dailyTimetableDao;
	
	@Autowired
	private TimetableDao timetableDao;
	
	@Autowired
	private TimeSlotDao timeSlotDao;

	@Test
	void givenExistentDailyTimetableId_whenFindById_thenReturnOptionalOfDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12), null);
		List<TimeSlot> timeSlots = timeSlotDao.findAllTimeSlotsOfDailyTimetable(dailyTimetable);
		dailyTimetable.setTimeSlots(timeSlots);
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(dailyTimetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenNonExistentDailyTimetableId_whenFindById_thenReturnEmptyOptional() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(0L);
		
		assertEquals(expected, actual);		
	}
	
	@Test
	void whenFindAll_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		Optional<DailyTimetable> dailyTimetable1 = dailyTimetableDao.findById(1L);
		Optional<DailyTimetable> dailyTimetable2 = dailyTimetableDao.findById(2L);
		expected.add(dailyTimetable1.orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetable2.orElseThrow(NoSuchElementException::new));
		
		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenDailyTimetable_whenSave_thenInsertDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(null, LocalDate.of(2020, 3, 1), null);
		
		DailyTimetable inserted = dailyTimetableDao.save(dailyTimetable);
		
		Optional<DailyTimetable> expected = Optional.of(inserted);
		Optional<DailyTimetable> actual = dailyTimetableDao.findById(dailyTimetable.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenDailyTimetable_whenUpdate_thenUpdateGivenTimetable() {
		Optional<DailyTimetable> dailyTimetable = dailyTimetableDao.findById(1L);
		DailyTimetable expected = dailyTimetable.orElseThrow(NoSuchElementException::new);
		expected.setDate(LocalDate.of(2020, 2, 15));
		
		dailyTimetableDao.update(expected);
		
		Optional<DailyTimetable> fetched = dailyTimetableDao.findById(expected.getId());
		DailyTimetable actual = fetched.orElseThrow(NoSuchElementException::new);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenDailyTimetableId_whenDeleteById_thenDeleteGivenTiemtable() {
		Long idForDelelte = 2L;
		List<DailyTimetable> dailyTimetables = dailyTimetableDao.findAll();
		List<DailyTimetable> expected = dailyTimetables.stream()
				.filter(d -> !d.getId().equals(idForDelelte))
				.collect(Collectors.toList());
		
		dailyTimetableDao.deleteById(idForDelelte);
		
		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenDailyTimetableAndTimetable_whenAddDailyTimetableToTimetable_thenAddDailyTimetableToTimetable() {
		Optional<DailyTimetable> dailyTimetable = dailyTimetableDao.findById(1L);
		Optional<Timetable> timetable = timetableDao.findById(2L);
		
		dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new), 
				timetable.orElseThrow(NoSuchElementException::new));
		
		List<DailyTimetable> dailyTimetables = timetableDao.findDailyTimetablesOfTimetable(timetable.orElseThrow(NoSuchElementException::new));
		Optional<DailyTimetable> dt = dailyTimetables.stream()
				.filter(d -> d.getId().equals(dailyTimetable.get().getId()))
				.findFirst();
		assertEquals(dailyTimetable.get().getId(), dt.get().getId());
	}
	
	@Test
	void givenLocalDate_whenFindByDate_thenReturnOptionalOfDailyTimetable() {
		LocalDate date = LocalDate.of(2020, 2, 12);
		Optional<DailyTimetable> expected = dailyTimetableDao.findById(1L);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findByDate(date);
		
		assertEquals(expected, actual);
	}
}
