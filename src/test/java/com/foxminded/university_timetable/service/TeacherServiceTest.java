package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;
	
	@InjectMocks
	private TeacherService teacherService;

	@Test
	void whenFindAll_thenReturnAllTeachers() {
		List<Teacher> expected = new ArrayList<>();
		expected.add(new Teacher(1L, "", "", "", "", "", ""));
		expected.add(new Teacher(2L, "", "", "", "", "", ""));

		when(teacherDao.findAll()).thenReturn(expected);
		
		List<Teacher> actual = teacherDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentTeacherId_whenFindById_thenReturnOptionalOfTeacher() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		Optional<Teacher> expected = Optional.of(teacher);
		when(teacherDao.findById(teacher.getId())).thenReturn(expected);
		
		Optional<Teacher> actual = teacherService.findById(teacher.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentTeachertId_whenFindById_thenReturnEmptyOptional() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		Optional<Teacher> expected = Optional.empty();
		when(teacherDao.findById(teacher.getId())).thenReturn(expected);
		
		Optional<Teacher> actual = teacherService.findById(teacher.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacher_whenDelete_thenDeleteTeacher() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		
		teacherService.delete(teacher);
		
		verify(teacherDao).delete(teacher);
	}
	
	@Test
	void givenTeacher_whenUpdate_thenUpdateTeacher() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		
		teacherService.update(teacher);
		
		verify(teacherDao).update(teacher);
	}
	
	@Test
	void givenTeacher_whenSave_thenSaveTeacher() {
		Teacher expected = new Teacher(1L, "", "", "", "", "", "");
		when(teacherDao.save(expected)).thenReturn(expected);
		
		Teacher actual = teacherService.save(expected);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherAndCourse_whenAddTeacherQualification_thenAddCourseToTeachersQualifications() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		Course course = new Course(1L, "");
		
		teacherDao.addTeacherQualification(teacher, course);
		
		verify(teacherDao).addTeacherQualification(teacher, course);
	}
	
	@Test
	void givenTeacherAndCourse_whenDeleteTeacherQualification_thenRemoveCourseFromTeachersQualifications() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		Course course = new Course(1L, "");
		
		teacherDao.deleteTeacherQualification(teacher, course);
		
		verify(teacherDao).deleteTeacherQualification(teacher, course);
	}
	
	@Test
	void givenTeacher_whenFindAllTeacherQualifications_thenReturnAllTeacherQualifications() {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, ""));
		expected.add(new Course(2L, ""));
		when(teacherDao.findAllTeacherQualifications(teacher)).thenReturn(expected);
		
		List<Course> actual = teacherService.findAllTeacherQualifications(teacher);
		
		assertEquals(expected, actual);
	}
}
