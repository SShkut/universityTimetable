package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.GroupDao;
import com.foxminded.university_timetable.dao.StudentDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
	
	@Mock
	private StudentDao studentDao;
	
	@Mock
	private GroupDao groupDao;
	
	@InjectMocks
	private StudentService studentService;

	@Test
	void whenFindAll_thenReturnAllStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "", "", "", "", "", ""));
		expected.add(new Student(2L, "", "", "", "", "", ""));

		when(studentDao.findAll()).thenReturn(expected);
		
		List<Student> actual = studentDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentStudentId_whenFindById_thenReturnOptionalOfStudent() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Optional<Student> expected = Optional.of(student);
		when(studentDao.findById(student.getId())).thenReturn(expected);
		
		Optional<Student> actual = studentService.findById(student.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentStudentId_whenFindById_thenReturnEmptyOptional() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Optional<Student> expected = Optional.empty();
		when(studentDao.findById(student.getId())).thenReturn(expected);
		
		Optional<Student> actual = studentService.findById(student.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudent_whenDelete_thenDeleteStudent() {
		Student student = new Student(1L, "", "", "", "", "", "");
		
		studentService.delete(student);
		
		verify(studentDao, times(1)).delete(student);
	}
	
	@Test
	void givenStudent_whenUpdate_thenUpdateStudent() {
		Student student = new Student(1L, "", "", "", "", "", "");
		
		studentService.update(student);
		
		verify(studentDao, times(1)).update(student);
	}
	
	@Test
	void givenStudent_whenSave_thenSaveStudent() {
		Student expected = new Student(1L, "", "", "", "", "", "");
		when(studentDao.save(expected)).thenReturn(expected);
		
		Student actual = studentService.save(expected);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndFreeGroup_whenAddStudentToGroup_thenAddStudentToGroup() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Group group = new Group(1L, "", "", "", new Semester());
		List<Student> students = LongStream.rangeClosed(1, 29)
				.mapToObj(i -> new Student(i, "", "", "", "", "", ""))
				.collect(Collectors.toList());
		when(groupDao.findGroupStudents(group)).thenReturn(students);

		studentService.addStudentToGroup(student, group);
		
		verify(studentDao, times(1)).addStudentToGroup(student, group);
	}
	
	@Test
	void givenStudentAndFullGroup_whenAddStudentToGroup_thenDontAddStudentToGroup() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Group group = new Group(1L, "", "", "", new Semester());
		List<Student> students = LongStream.rangeClosed(1, 30)
				.mapToObj(i -> new Student(i, "", "", "", "", "", ""))
				.collect(Collectors.toList());
		when(groupDao.findGroupStudents(group)).thenReturn(students);

		studentService.addStudentToGroup(student, group);
		
		verify(studentDao, times(0)).addStudentToGroup(student, group);
	}
	
	@Test
	void givenStudentAndGroup_whenDeleteStudentFromGroup_thenDeleteStudentFromGroup() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Group group = new Group(1L, "", "", "", new Semester());
		
		studentService.deleteStudentFromGroup(student, group);
		
		verify(studentDao, times(1)).deleteStudentFromGroup(student, group);
	}
	
	@Test
	void givenStudentAndCourse_whenAddStudentToCourse_thenAddStudentToCourse() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Course course = new Course(1L, "");
		
		studentService.addStudentToCourse(student, course);	
		
		verify(studentDao, times(1)).addStudentToCourse(student, course);
	}
	
	@Test
	void givenStudentAndGroup_whenDeleteStudentFromCourse_thenDeleteStudentFromGroup() {
		Student student = new Student(1L, "", "", "", "", "", "");
		Course course = new Course(1L, "");
		
		studentService.deleteStudentFromCourse(student, course);
		
		verify(studentDao, times(1)).deleteStudentFromCourse(student, course);
	}
	
	@Test
	void givenStudent_whenFindAllStudentCourses_thenReturnListOfStudentCourses() {
		Student student = new Student(1L, "", "", "", "", "", "");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, ""));
		expected.add(new Course(2L, ""));
		when(studentDao.findAllStudentCourses(student)).thenReturn(expected);
		
		List<Course> actual = studentService.findAllStudentCourses(student);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourse_whenFindCourseStudents_thenRerturnListOfStudntsOfCourse() {
		Course course = new Course(1L, "");
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "", "", "", "", "", ""));
		expected.add(new Student(2L, "", "", "", "", "", ""));
		when(studentDao.findCourseStudents(course)).thenReturn(expected);
		
		List<Student> actual = studentService.findCourseStudents(course);
		
		assertEquals(expected, actual);
	}
}
