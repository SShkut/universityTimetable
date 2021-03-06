package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.DailyTimetableDao;
import com.foxminded.university_timetable.exception.RecordAlreadyExistsException;
import com.foxminded.university_timetable.exception.WeekendDayNotAllowedException;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.Timetable;

@ExtendWith(MockitoExtension.class)
class DailyTimetableServiceTest {

	@Mock
	private DailyTimetableDao dailyTimetableDao;

	@InjectMocks
	private DailyTimetableService dailyTimetableService;

	@Test
	void whenFindAll_thenReturnAllDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L, LocalDate.of(2020, 2, 2)));
		expected.add(new DailyTimetable(2L, LocalDate.of(2020, 2, 3)));

		when(dailyTimetableDao.findAll()).thenReturn(expected);

		List<DailyTimetable> actual = dailyTimetableService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistentDailyTimetableId_whenFindById_thenReturnOptionalOfDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		when(dailyTimetableDao.findById(dailyTimetable.getId())).thenReturn(expected);

		Optional<DailyTimetable> actual = dailyTimetableService.findById(dailyTimetable.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentDailyTimetableId_whenFindById_thenReturnEmptyOptional() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));
		Optional<DailyTimetable> expected = Optional.empty();
		when(dailyTimetableDao.findById(dailyTimetable.getId())).thenReturn(expected);

		Optional<DailyTimetable> actual = dailyTimetableService.findById(dailyTimetable.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenDailyTimetable_whenDelete_thenDeleteDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));

		dailyTimetableService.delete(dailyTimetable);

		verify(dailyTimetableDao).delete(dailyTimetable);
	}

	@Test
	void givenDailyTimetable_whenUpdate_thenUpdateDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 6, 5));

		dailyTimetableService.update(dailyTimetable);

		verify(dailyTimetableDao).update(dailyTimetable);
	}

	@Test
	void givenDailyTimetablesWithSaturdayDate_whenUpdate_thenDontUpdateTimetable() {
		DailyTimetable dailyTimetableSaturday = new DailyTimetable(1L, LocalDate.of(2020, 6, 6));

		assertThrows(WeekendDayNotAllowedException.class, () -> dailyTimetableService.update(dailyTimetableSaturday));

		verify(dailyTimetableDao, never()).update(dailyTimetableSaturday);
	}

	@Test
	void givenDailyTimetablesWithSundayDate_whenUpdate_thenDontUpdateTimetable() {
		DailyTimetable dailyTimetableSunday = new DailyTimetable(1L, LocalDate.of(2020, 6, 7));

		assertThrows(WeekendDayNotAllowedException.class, () -> dailyTimetableService.update(dailyTimetableSunday));

		verify(dailyTimetableDao, never()).update(dailyTimetableSunday);
	}

	@Test
	void givenDailyTimetable_whenSave_thenSaveDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 6, 5));

		dailyTimetableService.save(dailyTimetable);

		verify(dailyTimetableDao).save(dailyTimetable);
	}

	@Test
	void givenDailyTimetableWithSaturdayDate_whenSave_thenDontSaveDailyTimetable() {
		DailyTimetable dailyTimetableSaturday = new DailyTimetable(1L, LocalDate.of(2020, 6, 6));

		assertThrows(WeekendDayNotAllowedException.class, () -> dailyTimetableService.save(dailyTimetableSaturday));

		verify(dailyTimetableDao, never()).save(dailyTimetableSaturday);
	}

	@Test
	void givenDailyTimetableWithSundayDate_whenSave_thenDontSaveDailyTimetable() {
		DailyTimetable dailyTimetableSunday = new DailyTimetable(1L, LocalDate.of(2020, 6, 7));

		assertThrows(WeekendDayNotAllowedException.class, () -> dailyTimetableService.save(dailyTimetableSunday));

		verify(dailyTimetableDao, never()).save(dailyTimetableSunday);
	}

	@Test
	void givenTimetable_whenFindTimetableDailyTimetables_thenReturnListOfDailyTimetables() {
		Timetable timetable = new Timetable(1L, "");
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L, LocalDate.of(2020, 2, 2)));
		expected.add(new DailyTimetable(2L, LocalDate.of(2020, 2, 3)));
		when(dailyTimetableDao.findTimetableDailyTimetables(timetable)).thenReturn(expected);

		List<DailyTimetable> actual = dailyTimetableService.findTimetableDailyTimetables(timetable);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotFittedDailyTimetableAndTimetable_whenAddDailyTimetableToTimetable_thenDontAddDailyTimetableToTimetable() {
		Timetable timetable = new Timetable(1L, "");
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));
		List<DailyTimetable> dailyTimetables = Arrays.asList(dailyTimetable);
		when(dailyTimetableDao.findTimetableDailyTimetables(timetable)).thenReturn(dailyTimetables);

		assertThrows(RecordAlreadyExistsException.class,
				() -> dailyTimetableService.addDailyTimetableToTimetable(dailyTimetable, timetable));

		verify(dailyTimetableDao, never()).addDailyTimetableToTimetable(dailyTimetable, timetable);
	}

	@Test
	void givenFittedDailyTimetableAndTimetable_whenAddDailyTimetableToTimetable_thenDontAddDailyTimetableToTimetable() {
		Timetable timetable = new Timetable(1L, "");
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));
		DailyTimetable newDailyTimetable = new DailyTimetable(2L, LocalDate.of(2020, 2, 3));
		List<DailyTimetable> dailyTimetables = Arrays.asList(dailyTimetable);
		when(dailyTimetableDao.findTimetableDailyTimetables(timetable)).thenReturn(dailyTimetables);

		dailyTimetableService.addDailyTimetableToTimetable(newDailyTimetable, timetable);

		verify(dailyTimetableDao).addDailyTimetableToTimetable(newDailyTimetable, timetable);
	}

	@Test
	void givenDate_whenFindByDate_thenReturnOptionalOfDailyTimetable() {
		LocalDate date = LocalDate.of(2020, 2, 2);
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 2));
		Optional<DailyTimetable> expected = Optional.of(dailyTimetable);
		when(dailyTimetableDao.findByDate(date)).thenReturn(expected);

		Optional<DailyTimetable> actual = dailyTimetableService.findByDate(date);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentStartDateEndDate_whenFindTimetableForStudent_thenReturnListOfStudentsDailyTimetablesInGivenRange() {
		Student student = new Student(1L, "", "", "", "", "", "");
		LocalDate start = LocalDate.of(2020, 2, 2);
		LocalDate end = LocalDate.of(2020, 2, 3);
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L, start));
		expected.add(new DailyTimetable(2L, end));
		when(dailyTimetableDao.findTimetableForStudent(student, start, end)).thenReturn(expected);

		List<DailyTimetable> actual = dailyTimetableService.findTimetableForStudent(student, start, end);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentStartDateEndDate_whenFindTimetableForTeacher_thenReturnListOfStudentsDailyTimetablesInGivenRange() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		LocalDate start = LocalDate.of(2020, 2, 2);
		LocalDate end = LocalDate.of(2020, 2, 3);
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(new DailyTimetable(1L, start));
		expected.add(new DailyTimetable(2L, end));
		when(dailyTimetableDao.findTimetableForTeacher(teacher, start, end)).thenReturn(expected);

		List<DailyTimetable> actual = dailyTimetableService.findTimetableForTeacher(teacher, start, end);

		assertEquals(expected, actual);
	}
}
