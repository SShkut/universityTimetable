package com.foxminded.university_timetable.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.of(2020, 2, 13));
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "CS"));
		courses.add(new Course(3L, "Physics"));
		List<Course> prerequisites = new ArrayList<>();
		prerequisites.add(new Course(2L, "Math"));
		prerequisites.add(new Course(4L, "History"));
		prerequisites.add(new Course(5L, "Chemistry"));
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		students.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
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
		DailyTimetable dailyTimetable = new DailyTimetable(LocalDate.of(2020, 3, 1), new ArrayList<>());

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
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.now());
		List<DailyTimetable> dailyTimetables = dailyTimetableDao.findAll();
		List<DailyTimetable> expected = dailyTimetables.stream().filter(d -> !d.getId().equals(dailyTimetable.getId()))
				.collect(Collectors.toList());

		dailyTimetableDao.delete(dailyTimetable);

		List<DailyTimetable> actual = dailyTimetableDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenTimetable_whenFindTimetableDailyTimetables_thenReturnListOfDailyTimetables() {
		Timetable timetable = new Timetable(2L, "archived");
		DailyTimetable dailyTimetable = new DailyTimetable(2L, LocalDate.of(2020, 2, 13));
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "CS"));
		courses.add(new Course(3L, "Physics"));
		List<Course> prerequisites = new ArrayList<>();
		prerequisites.add(new Course(2L, "Math"));
		prerequisites.add(new Course(4L, "History"));
		prerequisites.add(new Course(5L, "Chemistry"));
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		students.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
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
		Timetable timetable = new Timetable(2L, "");

		dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new),
				timetable);

		DailyTimetable dailyTimetableExpected1 = new DailyTimetable(2L, LocalDate.of(2020, 2, 13));
		DailyTimetable dailyTimetableExpected2 = new DailyTimetable(3L, LocalDate.of(2020, 2, 14), new ArrayList<>());
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "CS"));
		courses.add(new Course(3L, "Physics"));
		List<Course> prerequisites = new ArrayList<>();
		prerequisites.add(new Course(2L, "Math"));
		prerequisites.add(new Course(4L, "History"));
		prerequisites.add(new Course(5L, "Chemistry"));
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		students.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
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
	void givenStudentWithTimetableAndSameDates_whenFindTimetableForStudent_thenReturnListOfTimetablesForGivenDay() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");
		LocalDate date = LocalDate.of(2020, 2, 12);
		List<DailyTimetable> expected = Arrays.asList(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForStudent(student, date, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithoutTimetableAndDate_whenFindTimetableForStudent_thenReturnEmptyList() {
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127");
		LocalDate date = LocalDate.of(2020, 2, 12);
		List<DailyTimetable> expected = new ArrayList<>();

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForStudent(student, date, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithTimetableAndSameDates_whenFindDailyTimetableForTeacher_thenReturnListOfTimetablesForGivenDay() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		LocalDate date = LocalDate.of(2020, 2, 13);
		List<DailyTimetable> expected = Arrays.asList(dailyTimetableDao.findById(2L).orElseThrow(NoSuchElementException::new));

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForTeacher(teacher, date, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithoutTimetableAndDate_whenFindTimetableForTeacher_thenReturnEmptyList() {
		Teacher teacher = new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", "masters");
		LocalDate date = LocalDate.of(2020, 2, 13);
		List<DailyTimetable> expected = new ArrayList<>();

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForTeacher(teacher, date, date);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithTimetableAndDifferentDates_whenFindTimetableForStudent_thenReturnListOfDailyTimetablesForGivenPeriod() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		expected.add(dailyTimetableDao.findById(2L).orElseThrow(NoSuchElementException::new));
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForStudent(student, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 28));

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentWithoutMonthlyTimetableAndDifferentDates_whenFindTimetableForStudent_thenReturnEmptyList() {
		List<DailyTimetable> expected = new ArrayList<>();
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127");

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForStudent(student, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 28));

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithTimetableAndDifferentDates_whenFindTimetableForTeacher_thenReturnListOfDailyTimetablesForGivenPeriod() {
		List<DailyTimetable> expected = new ArrayList<>();
		expected.add(dailyTimetableDao.findById(1L).orElseThrow(NoSuchElementException::new));
		Teacher teacher = new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", "masters");

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForTeacher(teacher, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 28));

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherWithoutMonthlyTimetableAndDifferentDates_whenFindTimetableForTeacher_thenReturnEmptyList() {
		List<DailyTimetable> expected = new ArrayList<>();
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");

		List<DailyTimetable> actual = dailyTimetableDao.findTimetableForTeacher(teacher, LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31));

		assertEquals(expected, actual);
	}
}
