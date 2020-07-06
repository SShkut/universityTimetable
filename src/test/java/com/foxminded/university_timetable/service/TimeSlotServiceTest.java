package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.dao.TimeSlotDao;
import com.foxminded.university_timetable.exception.UniversityResourceNotAvailableException;
import com.foxminded.university_timetable.exception.TeacherHasNoQualificationException;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceTest {

	@Mock
	private TimeSlotDao timeSlotDao;

	@Mock
	private TeacherDao teacherDao;

	@InjectMocks
	private TimeSlotService timeSlotService;

	@Test
	void whenFindAll_thenReturnAllTimeSlots() {
		List<TimeSlot> expected = new ArrayList<>();
		expected.add(new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room()));
		expected.add(new TimeSlot(2L, LocalTime.of(2, 0), LocalTime.of(3, 0), new Course(), new Teacher(), new Group(), new Room()));

		when(timeSlotDao.findAll()).thenReturn(expected);

		List<TimeSlot> actual = timeSlotDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistentTimeSlotId_whenFindById_thenReturnOptionalOfTimeSlot() {
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room());
		Optional<TimeSlot> expected = Optional.of(timeSlot);
		when(timeSlotDao.findById(timeSlot.getId())).thenReturn(expected);

		Optional<TimeSlot> actual = timeSlotService.findById(timeSlot.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentTimeSlotId_whenFindById_thenReturnEmptyOptional() {
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room());
		Optional<TimeSlot> expected = Optional.empty();
		when(timeSlotDao.findById(timeSlot.getId())).thenReturn(expected);

		Optional<TimeSlot> actual = timeSlotService.findById(timeSlot.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenDelete_thenDeleteTimeSlot() {
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room());

		timeSlotService.delete(timeSlot);

		verify(timeSlotDao).delete(timeSlot);
	}

	@Test
	void givenTimeSlot_whenUpdate_thenUpdateTimeSlot() {
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room());

		timeSlotService.update(timeSlot);

		verify(timeSlotDao).update(timeSlot);
	}

	@Test
	void givenCorrectTimeSlot_whenSave_thenSaveTimeSlot() {
		Course course = new Course(1L, "");
		List<Course> qualifications = new ArrayList<>();
		qualifications.add(course);
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		when(teacherDao.findAllTeacherQualifications(teacher)).thenReturn(qualifications);
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), course, teacher, new Group(), new Room());

		timeSlotService.save(timeSlot);

		verify(timeSlotDao).save(timeSlot);
	}

	@Test
	void givenTimeSlotWithNotFittedCourse_whenSave_thenDontSaveTimeSlot() {
		Course course = new Course(1L, "");
		List<Course> qualifications = new ArrayList<>();
		qualifications.add(new Course(2L, ""));
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		when(teacherDao.findAllTeacherQualifications(teacher)).thenReturn(qualifications);
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), course, teacher, new Group(), new Room());

		assertThrows(TeacherHasNoQualificationException.class, () -> timeSlotService.save(timeSlot));

		verify(timeSlotDao, never()).save(timeSlot);
	}

	@Test
	void givenDailyTimetable_whenFindAllDailyTimetableTimeSlots_thenReturnTimeSlotsOfDailyTimetable() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 3));
		List<TimeSlot> expected = new ArrayList<>();
		expected.add(new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), new Room()));
		expected.add(new TimeSlot(2L, LocalTime.of(2, 0), LocalTime.of(3, 0), new Course(), new Teacher(), new Group(), new Room()));
		when(timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable)).thenReturn(expected);

		List<TimeSlot> actual = timeSlotService.findAllDailyTimetableTimeSlots(dailyTimetable);

		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlotWithNotFittedGroupAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenDontAddTimeSlotToTimetable() {
		Group group = new Group(1L, "", "", "", new Semester());
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), group, new Room());
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 3));
		when(timeSlotDao.findByGroupAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), group)).thenReturn(Optional.of(timeSlot));

		assertThrows(UniversityResourceNotAvailableException.class,
				() -> timeSlotService.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable));

		verify(timeSlotDao, never()).addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
	}

	@Test
	void givenTimeSlotWithNotFittedTeacherpAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenDontAddTimeSlotToTimetable() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), teacher, new Group(), new Room());
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 3));
		when(timeSlotDao.findByTeacherAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), teacher)).thenReturn(Optional.of(timeSlot));

		assertThrows(UniversityResourceNotAvailableException.class,
				() -> timeSlotService.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable));

		verify(timeSlotDao, never()).addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
	}

	@Test
	void givenTimeSlotWithNotFittedRoomAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenDontAddTimeSlotToTimetable() {
		Room room = new Room(1L, "", 1);
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), new Teacher(), new Group(), room);
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 3));
		when(timeSlotDao.findByRoomAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), room)).thenReturn(Optional.of(timeSlot));

		assertThrows(UniversityResourceNotAvailableException.class,
				() -> timeSlotService.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable));

		verify(timeSlotDao, never()).addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
	}

	@Test
	void givenFittedTimeSlotAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenAddTimeSlotToTimetable() {
		Group group = new Group(1L, "", "", "", new Semester());
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		Room room = new Room(1L, "", 1);
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), new Course(), teacher, group, room);
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 3));
		when(timeSlotDao.findByGroupAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), group)).thenReturn(Optional.empty());
		when(timeSlotDao.findByTeacherAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), teacher)).thenReturn(Optional.empty());
		when(timeSlotDao.findByRoomAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), room)).thenReturn(Optional.empty());

		timeSlotService.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);

		verify(timeSlotDao).addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
	}
}
