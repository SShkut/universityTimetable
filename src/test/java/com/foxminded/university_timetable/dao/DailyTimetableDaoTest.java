package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
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
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
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
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private TeacherDao teacherDao;

	@Test
	void givenExistentDailyTimetableId_whenFindById_thenReturnOptionalOfDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12), null);
		List<TimeSlot> timeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
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
		DailyTimetable dailyTimetable = new DailyTimetable(null, LocalDate.of(2020, 3, 1), new ArrayList<>());
		
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
	void givenDailyTimetable_whenDelete_thenDeleteGivenTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.now(), null);
		List<DailyTimetable> dailyTimetables = dailyTimetableDao.findAll();
		List<DailyTimetable> expected = dailyTimetables.stream()
				.filter(d -> !d.getId().equals(dailyTimetable.getId()))
				.collect(Collectors.toList());
		
		dailyTimetableDao.delete(dailyTimetable);
		
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
	
	@Test
	void givenStudentWithTimetableAndDate_whenFindDailyTimetableForStudent_thenReturnListOfTimetable() {
		Optional<Student> student = studentDao.findById(1L);
		LocalDate date = LocalDate.of(2020, 2, 12);
		Optional<DailyTimetable> expected = dailyTimetableDao.findById(1L);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForStudent(
				student.orElseThrow(NoSuchElementException::new), date);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentWhithoutTimetableAndDate_whenFindDailyTimetableForStudent_thenReturnEmptyList() {
		Optional<Student> student = studentDao.findById(5L);
		LocalDate date = LocalDate.of(2020, 2, 12);
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForStudent(
				student.orElseThrow(NoSuchElementException::new), date);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherWithTimetableAndDate_whenFindDailyTimetableForTeacher_thenReturnListOfTimetable() {
		Optional<Teacher> teacher= teacherDao.findById(1L);
		LocalDate date = LocalDate.of(2020, 2, 13);
		Optional<DailyTimetable> expected = dailyTimetableDao.findById(2L);
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForTeacher(
				teacher.orElseThrow(NoSuchElementException::new), date);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherWhithoutTimetableAndDate_whenFindDailyTimetableForTeacher_thenReturnEmptyList() {
		Optional<Teacher> teacher= teacherDao.findById(2L);
		LocalDate date = LocalDate.of(2020, 2, 13);
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForTeacher(
				teacher.orElseThrow(NoSuchElementException::new), date);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentWithMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetableDao.findById(2L).orElseThrow(NoSuchElementException::new));
		Optional<Student> student = studentDao.findById(1L);
		
		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForStudent(
				student.orElseThrow(NoSuchElementException::new), Month.FEBRUARY, 2020);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentWithoutMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		Optional<Student> student = studentDao.findById(5L);
		
		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForStudent(
				student.orElseThrow(NoSuchElementException::new), Month.FEBRUARY, 2020);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherWithMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		Optional<Teacher> teacher = teacherDao.findById(2L);
		
		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForTeacher(
				teacher.orElseThrow(NoSuchElementException::new), Month.FEBRUARY, 2020);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherWithoutMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		Optional<Teacher> teacher = teacherDao.findById(1L);
		
		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForTeacher(
				teacher.orElseThrow(NoSuchElementException::new), Month.MARCH, 2020);
		
		assertEquals(expected, actual);
	}
}
