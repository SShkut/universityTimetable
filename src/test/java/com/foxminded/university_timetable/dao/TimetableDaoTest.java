package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.model.Timetable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimetableDaoTest {

	@Autowired
	private TimetableDao timetableDao;

	@Test
	void givenExistentTimetableId_whenFindById_thenReturnOptionalOfTimetable() {
		Timetable timetable = new Timetable(2L, "archived", null);
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.of(2020, 2, 13), null);
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "CS", null));
		courses.add(new Course(3L, "Physics", null));
		List<Course> prerequisites = new ArrayList<>();
		prerequisites.add(new Course(2L, "Math", null));
		prerequisites.add(new Course(4L, "History", null));
		prerequisites.add(new Course(5L, "Chemistry", null));
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		students.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		List<TimeSlot> timeSlots = new ArrayList<>();
		timeSlots.add(new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50), new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100)));
		dailyTimetable.setTimeSlots(timeSlots);
		List<DailyTimetable> dailyTimetables = Arrays.asList(dailyTimetable);
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

		Timetable timetable = timetableDao.save(new Timetable(null, "new", new ArrayList<>()));

		Optional<Timetable> expected = Optional.of(timetable);
		Optional<Timetable> actual = timetableDao.findById(timetable.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenUpdate_thenUpdateTimetable() {
		Timetable timetable = new Timetable(2L, "new", null);
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.of(2020, 2, 13), null);
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "CS", null));
		courses.add(new Course(3L, "Physics", null));
		List<Course> prerequisites = new ArrayList<>();
		prerequisites.add(new Course(2L, "Math", null));
		prerequisites.add(new Course(4L, "History", null));
		prerequisites.add(new Course(5L, "Chemistry", null));
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		students.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		List<TimeSlot> timeSlots = new ArrayList<>();
		timeSlots.add(new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50), new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100)));
		dailyTimetable.setTimeSlots(timeSlots);
		List<DailyTimetable> dailyTimetables = Arrays.asList(dailyTimetable);
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
