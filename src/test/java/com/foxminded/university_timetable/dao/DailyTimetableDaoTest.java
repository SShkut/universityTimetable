package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
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
class DailyTimetableDaoTest {

	@Autowired
	private DailyTimetableDao dailyTimetableDao;

	@Test
	void givenExistentDailyTimetableId_whenFindById_thenReturnOptionalOfDailyTimetable() {
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
		Optional<DailyTimetable> dailyTimetable3 = dailyTimetableDao.findById(3L);
		expected.add(dailyTimetable1.orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetable2.orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetable3.orElseThrow(NoSuchElementException::new));

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
		List<DailyTimetable> expected = dailyTimetables.stream().filter(d -> !d.getId().equals(dailyTimetable.getId()))
				.collect(Collectors.toList());

		dailyTimetableDao.delete(dailyTimetable);

		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenFindTimetableDailyTimetables_thenReturnListOfDailyTimetables() {
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
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetable);

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableDailyTimetables(timetable);

		assertEquals(expected, actual);
	}

	@Test
	void givenDailyTimetableAndTimetable_whenAddDailyTimetableToTimetable_thenAddDailyTimetableToTimetable() {
		Optional<DailyTimetable> dailyTimetable = dailyTimetableDao.findById(3L);
		Timetable timetable = new Timetable(2L, null, null);

		dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new),
				timetable);

		DailyTimetable dailyTimetableExpected1 = new DailyTimetable(2L, LocalDate.of(2020, 2, 13), null);
		DailyTimetable dailyTimetableExpected2 = new DailyTimetable(3L, LocalDate.of(2020, 2, 14), new ArrayList<>());
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
		dailyTimetableExpected1.setTimeSlots(timeSlots);
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableExpected1);
		expected.add(dailyTimetableExpected2);
		List<DailyTimetable> actual = dailyTimetableDao.findTimetableDailyTimetables(timetable);
		assertEquals(expected, actual);
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
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");
		LocalDate date = LocalDate.of(2020, 2, 12);
		Optional<DailyTimetable> expected = dailyTimetableDao.findById(1L);

		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForStudent(student, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithoutTimetableAndDate_whenFindDailyTimetableForStudent_thenReturnEmptyList() {
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127");
		LocalDate date = LocalDate.of(2020, 2, 12);
		Optional<DailyTimetable> expected = Optional.empty();

		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForStudent(student, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithTimetableAndDate_whenFindDailyTimetableForTeacher_thenReturnListOfTimetable() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");
		LocalDate date = LocalDate.of(2020, 2, 13);
		Optional<DailyTimetable> expected = dailyTimetableDao.findById(2L);

		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForTeacher(teacher, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWhithoutTimetableAndDate_whenFindDailyTimetableForTeacher_thenReturnEmptyList() {
		Teacher teacher = new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", null,
				"masters");
		LocalDate date = LocalDate.of(2020, 2, 13);
		Optional<DailyTimetable> expected = Optional.empty();

		Optional<DailyTimetable> actual = dailyTimetableDao.findDailyTimetableForTeacher(teacher, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetableDao.findById(2L).orElseThrow(NoSuchElementException::new));
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");

		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForStudent(student, Month.FEBRUARY, 2020);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithoutMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127");

		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForStudent(student, Month.FEBRUARY, 2020);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		Teacher teacher = new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", null,
				"masters");

		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForTeacher(teacher, Month.FEBRUARY, 2020);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithoutMonthlyTimetableMonthAndYear_whenFindMonthlyTimetableForStudent_thenReturnListOfDailyTimetables() {
		List<DailyTimetable> expected = new ArrayList<>();
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");

		List<DailyTimetable> actual = dailyTimetableDao.findMonthlyTimetableForTeacher(teacher, Month.MARCH, 2020);

		assertEquals(expected, actual);
	}
}
