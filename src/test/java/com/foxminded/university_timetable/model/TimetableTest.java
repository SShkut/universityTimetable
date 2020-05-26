package com.foxminded.university_timetable.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Ignore
class TimetableTest {
	
	Timetable timetable;
	Teacher johnTeacher;
	Teacher emptyTeacher;
	Student adamStudent;

	@BeforeEach
	void setUp() throws Exception {
		Course cs111 = new Course(1L, "CS", null);
		Course cs222 = new Course(2L, "ADT", null);
		Course cs333 = new Course(3L, "POSA", null);
		Course cs444 = new Course(4L, "Calculus I", null);
		Course cs555 = new Course(5L, "Calculus II", null);
		
		List<Course> adamCourses = Arrays.asList(cs111, cs222, cs333);
		List<Course> teacherCourses = Arrays.asList(cs111, cs222, cs333, cs444);
				
		adamStudent = new Student("Adam", "Smith", "123456789", "123123123", "adam@mail.com", adamCourses, "SN001");
		List<Student> students = Arrays.asList(adamStudent);
		johnTeacher = new Teacher("John", "Dow", "987654321", "321321321", "dow@mail.com", teacherCourses, "PhD");
		emptyTeacher = new Teacher("", "", "", "", "", new ArrayList<>(), "");
		Group adamGroup = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), students);
		Group emptyGroup = new Group("00", "", "", new Semester(1998, ""), new ArrayList<>());
		
		TimeSlot slot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), cs111, johnTeacher, adamGroup, null);
		TimeSlot slot2 = new TimeSlot(LocalTime.of(10, 40), LocalTime.of(12, 10), cs444, emptyTeacher, emptyGroup, null);
		
		DailyTimetable dailyTimetable = new DailyTimetable(LocalDate.now(), Arrays.asList(slot1, slot2));
		
		timetable = new Timetable("test", Arrays.asList(dailyTimetable));
	}

	@Test
	void givenStudentAndDate_whenGetTimetableDay_thenReturnOptionalTimetableForGivenDay() {
		Course course = new Course(1L, "CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);	
		DailyTimetable expected = new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot));
		
		DailyTimetable actual = timetable.getTimetableDay(LocalDate.now(), adamStudent).get();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndDateWithoutTimetable_whenGetTimetableDay_thenReturnOptionalEmpty() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = timetable.getTimetableDay(LocalDate.now().minusDays(1), adamStudent);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenStudentAndNullAsDate_whenGetTimetableDay_thenthenReturnOptionalEmpty() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = timetable.getTimetableDay(null, adamStudent);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherAndDate_whenGetTimetableDay_thenReturnTimetableForGivenDay() {
		Course course = new Course(1L, "CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);
		DailyTimetable expected = new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot));
		
		DailyTimetable actual = timetable.getTimetableDay(LocalDate.now(), johnTeacher).get();
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenTeacherAndDateWhithoutTimetable_whenGetTimetableDay_thenReturnOptionalEmpty() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = timetable.getTimetableDay(LocalDate.now().minusDays(1), johnTeacher);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenTeacherAndNullAsDate_whenGetTimetableDay_thenthenReturnOptionalEmpty() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = timetable.getTimetableDay(null, johnTeacher);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNullAsPersonAndDate_whenGetTimetableDay_thenReturnOptionalEmpty() {
		Optional<DailyTimetable> expected = Optional.empty();
		
		Optional<DailyTimetable> actual = timetable.getTimetableDay(LocalDate.now(), null);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenStudentAndMonth_whenGetTimetableMonth_thenReturnTimetableForMonth() {
		Course course = new Course(1L, "CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);	
		List<DailyTimetable> expected = Arrays.asList(new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot)));
		
		List<DailyTimetable> actual = timetable.getTimetableMonth(LocalDate.now().getMonth(), adamStudent);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherAndMonth_whenGetTimetableMonth_thenthenReturnTimetableForMonth() {
		Course course = new Course(1L, "CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);
		List<DailyTimetable> expected = Arrays.asList(new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot)));
		
		List<DailyTimetable> actual = timetable.getTimetableMonth(LocalDate.now().getMonth(), johnTeacher);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNullAsPersonAndMonth_whenGetTimetableMonth_thenReturnEmptyList() {
		List<DailyTimetable> expected = new ArrayList<>();
		
		List<DailyTimetable> actual = timetable.getTimetableMonth(LocalDate.now().getMonth(), null);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndMonthWithoutTimetable_whenGetTimetableMonth_thenReturnEmptyList() {
		List<DailyTimetable> expected = new ArrayList<>();
		
		List<DailyTimetable> actual = timetable.getTimetableMonth(Month.of(LocalDate.now().getMonth().getValue() - 1), adamStudent);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherAndMonthWithoutTimetable_whenGetTimetableMonth_thenReturnEmptyList() {
		List<DailyTimetable> expected = new ArrayList<>();
		
		List<DailyTimetable> actual = timetable.getTimetableMonth(Month.of(LocalDate.now().getMonth().getValue() - 1), johnTeacher);
		
		assertEquals(expected, actual);
	}
}
