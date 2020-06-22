package com.foxminded.university_timetable.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimeSlotDaoTest {

	@Autowired
	private TimeSlotDao timeSlotDao;

	@Test
	void givenExistentTimeSlotId_whenFindById_thenReturnOptionalOfTimeSlot() {
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
		TimeSlot timeSlot = new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		Optional<TimeSlot> expected = Optional.of(timeSlot);

		Optional<TimeSlot> actual = timeSlotDao.findById(timeSlot.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenFindAll_returnListOfTimeSlots() {
		List<TimeSlot> expected = new ArrayList<>();
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
		TimeSlot timeSlot1 = new TimeSlot(1L, LocalTime.of(9, 0), LocalTime.of(9, 30),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		expected.add(timeSlot1);

		List<Course> courses2 = new ArrayList<>();
		courses2.add(new Course(3L, "Physics"));
		courses2.add(new Course(4L, "History"));
		List<Course> prerequisites2 = new ArrayList<>();
		prerequisites2.add(new Course(5L, "Chemistry"));
		List<Student> students2 = new ArrayList<>();
		students2.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		students2.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", "cn-125"));
		students2.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", "cn-126"));
		TimeSlot timeSlot2 = new TimeSlot(2L, LocalTime.of(9, 0), LocalTime.of(9, 30),
				new Course(2L, "Math", prerequisites2),
				new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", courses2, "masters"),
				new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), students2),
				new Room(2L, "b-1", 70));
		expected.add(timeSlot2);

		TimeSlot timeSlot3 = new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		expected.add(timeSlot3);

		List<TimeSlot> actual = timeSlotDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenSave_thenInsertTimeSlot() {
		TimeSlot timeSlot = new TimeSlot(LocalTime.of(19, 0), LocalTime.of(19, 30), new Course(1L, ""),
						new Teacher(1L, "", "", "", "", "", "phD"),
						new Group(1L, "", "", "", new Semester(1L, 2020, "")),
						new Room(1L, "", 1));
		int expected = timeSlotDao.findAll().size() + 1;
		
		timeSlotDao.save(timeSlot);
		
		int actual = timeSlotDao.findAll().size();
		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenUpdate_thenUpdateGivenTimeSlot() {
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
		TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(12, 0), LocalTime.of(13, 30),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		Optional<TimeSlot> expected = Optional.of(timeSlot);

		timeSlotDao.update(timeSlot);

		Optional<TimeSlot> actual = timeSlotDao.findById(timeSlot.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenDelete_thenDeleteTimeSlot() {
		Optional<TimeSlot> timeSlot = timeSlotDao.findById(1L);
		assertTrue(timeSlot.isPresent());

		timeSlotDao.delete(new TimeSlot(1L, LocalTime.of(0, 0), LocalTime.of(0, 0), new Course(), new Teacher(),
				new Group(), new Room()));

		timeSlot = timeSlotDao.findById(1L);
		assertTrue(!timeSlot.isPresent());
	}

	@Test
	void givenDailyTimetable_whenFindAllDailyTimetableTimeSlots_thenReturnListOfTimeSlots() {
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
		List<TimeSlot> expected = new ArrayList<>();
		expected.add(new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50), new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100)));

		List<TimeSlot> actual = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);

		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlotAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenAddTimeSlotToDailyTimetable() {
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
		List<TimeSlot> expected = new ArrayList<>();
		TimeSlot timeSlot1 = new TimeSlot(1L, LocalTime.of(9, 0), LocalTime.of(9, 30),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		TimeSlot timeSlot2 = new TimeSlot(3L, LocalTime.of(10, 0), LocalTime.of(11, 50),
				new Course(1L, "CS", prerequisites),
				new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", courses, "phD"),
				new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), students),
				new Room(1L, "a-1", 100));
		expected.add(timeSlot1);
		expected.add(timeSlot2);

		timeSlotDao.addTimeSlotToDailyTimetable(timeSlot1, dailyTimetable);

		List<TimeSlot> actual = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCorrectStartTimeEndTimeTeacher_whenIsTeacherAvailable_returnTrue() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(12, 00);
		LocalTime endTime = LocalTime.of(13, 30);
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		
		Boolean isTeacherAvailable = timeSlotDao.isTeacherAvailable(dailyTimetable, startTime, endTime, teacher);
		
		assertTrue(isTeacherAvailable);
	}
	
	@Test
	void givenIncorrectStartTimeEndTimeTeacher_whenIsTeacherAvailable_returnFalse() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(9, 00);
		LocalTime endTime = LocalTime.of(9, 30);
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		
		Boolean isTeacherAvailable = timeSlotDao.isTeacherAvailable(dailyTimetable, startTime, endTime, teacher);
		
		assertFalse(isTeacherAvailable);
	}
	
	@Test
	void givenCorrectStartTimeEndTimeGroup_whenIsGroupAvailable_returnTrue() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(10, 00);
		LocalTime endTime = LocalTime.of(11, 50);
		Group group = new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"));
		
		Boolean isGroupAvailable = timeSlotDao.isGroupAvailable(dailyTimetable, startTime, endTime, group);
		
		assertTrue(isGroupAvailable);
	}
	
	@Test
	void givenIncorrectStartTimeEndTimeGroup_whenIsGroupAvailable_returnFalse() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(9, 00);
		LocalTime endTime = LocalTime.of(9, 30);
		Group group = new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"));
		
		Boolean isGroupAvailable = timeSlotDao.isGroupAvailable(dailyTimetable, startTime, endTime, group);
		
		assertFalse(isGroupAvailable);
	}
	
	@Test
	void givenCorrectStartTimeEndTimeRoom_whenIsRoomAvailable_returnTrue() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(10, 00);
		LocalTime endTime = LocalTime.of(11, 50);
		Room room = new Room(2L, "b-1", 70);
		
		Boolean isRoomAvailable = timeSlotDao.isRoomAvailable(dailyTimetable, startTime, endTime, room);
		
		assertTrue(isRoomAvailable);
	}
	
	@Test
	void givenIncorrectStartTimeEndTimeRoom_whenIsRoomAvailable_returnFalse() {
		DailyTimetable dailyTimetable = new DailyTimetable(1L, LocalDate.of(2020, 2, 12));
		LocalTime startTime = LocalTime.of(9, 00);
		LocalTime endTime = LocalTime.of(9, 30);
		Room room = new Room(1L, "a-1", 100);
		
		Boolean isRoomAvailable = timeSlotDao.isRoomAvailable(dailyTimetable, startTime, endTime, room);
		
		assertFalse(isRoomAvailable);
	}
}
