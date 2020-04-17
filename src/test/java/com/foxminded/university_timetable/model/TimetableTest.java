package com.foxminded.university_timetable.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimetableTest {
	
	Timetable timetable;
	Teacher johnTeacher;
	Teacher emptyTeacher;
	Student adamStudent;

	@BeforeEach
	void setUp() throws Exception {
		Course cs111 = new Course("CS", null);
		Course cs222 = new Course("ADT", null);
		Course cs333 = new Course("POSA", null);
		Course cs444 = new Course("Calculus I", null);
		Course cs555 = new Course("Calculus II", Arrays.asList(cs444));
		
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
	void givenStudentAndDate_whenGetTimetableDay_thenReturnTimetableForGivenDay() {
		Course course = new Course("CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);	
		DailyTimetable expected = new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot));
		
		DailyTimetable actual = timetable.getTimetableDay(LocalDate.now(), adamStudent).get();
		
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherAndDate_whenGetTimetableDay_thenReturnTimetableForGivenDay() {
		Course course = new Course("CS", null);
		Group group = new Group("AA-11", "CS", "CS", new Semester(2020, "summer"), Arrays.asList(adamStudent));
		TimeSlot expectedSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30), course, johnTeacher, group, null);
		DailyTimetable expected = new DailyTimetable(LocalDate.now(), Arrays.asList(expectedSlot));
		
		DailyTimetable actual = timetable.getTimetableDay(LocalDate.now(), johnTeacher).get();
		
		assertEquals(expected, actual);
	}

}
