package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.CourseDao;
import com.foxminded.university_timetable.exception.RecordAlreadyExistsException;
import com.foxminded.university_timetable.model.Course;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {	

	@Mock
	private CourseDao courseDao;

	@InjectMocks
	private CourseService courseService;
	
	@Test
	void whenFindAll_thenReturnAllCourses() {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "Math"));
		expected.add(new Course(2L, "CS"));
		when(courseDao.findAll()).thenReturn(expected);

		List<Course> actual = courseService.findAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenFindById_thenReturnOptionalOfCourse() {
		Course course = new Course(1L, "Math");
		Optional<Course> expected = Optional.of(course);
		when(courseDao.findById(course.getId())).thenReturn(expected);
		
		Optional<Course> actual = courseService.findById(course.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentCourseId_whenFindById_thenReturnEmptyOptional() {
		Course course = new Course(-1L, "");
		Optional<Course> expected = Optional.empty();
		when(courseDao.findById(course.getId())).thenReturn(expected);
		
		Optional<Course> actual = courseService.findById(course.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourse_whenDelete_thenDeleteCourse() {
		Course course = new Course(1L, "Math");
		
		courseService.delete(course);
		
		verify(courseDao).delete(course);
	}
	
	@Test
	void givenCourse_whenUpdate_thenUpdateCourse() {
		Course course = new Course(1L, "Math");
		
		courseService.update(course);
		
		verify(courseDao).update(course);
	}
	
	@Test
	void givenCourse_whenSave_thenSaveCourse() {
		Course course = new Course(1L, "Math");		
		
		courseService.save(course);
		
		verify(courseDao).save(course);
	}	
	
	@Test
	void givenCourseAndDublicatePrerequisite_whenAddCoursePrerequisite_thenDontAddPrerequisiteToCourse() {
		Course course = new Course(1L, "Math");
		Course prerequisite = new Course(2L, "CS");
		when(courseDao.findCoursePrerequisites(course)).thenReturn(Arrays.asList(prerequisite));
		
		assertThrows(RecordAlreadyExistsException.class,
			() -> courseService.addCoursePrerequisite(course, prerequisite));

		verify(courseDao, never()).addCoursePrerequisite(course, prerequisite);
	}	
	
	@Test
	void givenCourseAndPrerequisite_whenAddCoursePrerequisite_thenAddPrerequisiteToCourse() {
		Course course = new Course(1L, "Math");
		Course prerequisite = new Course(2L, "CS");
		when(courseDao.findCoursePrerequisites(course)).thenReturn(new ArrayList<>());
		
		courseService.addCoursePrerequisite(course, prerequisite);

		verify(courseDao).addCoursePrerequisite(course, prerequisite);
	}
}
