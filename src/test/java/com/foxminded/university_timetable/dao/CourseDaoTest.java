package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dbunit.DatabaseUnitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.config.TestJdbcConfig;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseDaoTest {
	
	@Autowired
	private CourseDao courseDao;
	
	@Test
	void givenCourse_whenFindPrerequisitesOfCourse_thenReturnCoursePrerequisites() {
		Course course = new Course(1L, "CS", null);
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(2L, "Math", null));
		expected.add(new Course(4L, "History", null));
		expected.add(new Course(5L, "Chemistry", null));
		
		List<Course> actual = courseDao.findPrerequisitesOfCourse(course);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentCourseId_whenFindById_thenReturnOptionalOfCourse() {
		Course course = new Course(2L, "Math", null);
		List<Course> prerequisites = courseDao.findPrerequisitesOfCourse(course);
		course.setPrerequisites(prerequisites);
		Optional<Course> expected = Optional.of(course);
		
		Optional<Course> actual = courseDao.findById(course.getId());		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentCourseId_whenFindById_thenReturnEmptyOptional() {
		Optional<Course> expected = Optional.empty();
		
		Optional<Course> actual = courseDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllCourses() {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS",  null));
		expected.add(new Course(2L, "Math", null));
		expected.add(new Course(3L, "Physics", null));
		expected.add(new Course(4L, "History", null));
		expected.add(new Course(5L, "Chemistry", null));
		
		List<Course> actual = courseDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenDelete_thenDeleteCourseWithGivenId() throws DatabaseUnitException, SQLException {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(2L, "Math", null));
		expected.add(new Course(3L, "Physics", null));
		expected.add(new Course(4L, "History", null));
		expected.add(new Course(5L, "Chemistry", null));
		
		courseDao.delteById(1L);
		
		List<Course> actual = courseDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewCourse_whenSave_thenInsertCourse() throws DatabaseUnitException, SQLException {
		Course course = new Course(null, "Calculus", new ArrayList<>());
		
		Course inserted = courseDao.save(course);

		Optional<Course> expected = Optional.of(inserted);
		Optional<Course> actual = courseDao.findById(course.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentCourse_whenUpdate_thenUpdateCourse() throws DatabaseUnitException, SQLException {
		Course course = new Course(1L, "CS-2", null);
		List<Course> prerequisites = courseDao.findPrerequisitesOfCourse(course);
		course.setPrerequisites(prerequisites);
		
		Course updatedCourse = courseDao.update(course);
		
		Optional<Course> expected = Optional.of(updatedCourse);		
		Optional<Course> actual = courseDao.findById(course.getId());
		assertEquals(expected, actual);
	}
	
	
	@Test
	void givenCourse_whenFindStudentsOfCourse_thenReturnListOfStudents() {
		Course course = new Course(1L, "CS-2", null);
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		
		List<Student> actual = courseDao.findStudentsOfCourse(course);
		
		assertEquals(expected, actual);
	}
}
