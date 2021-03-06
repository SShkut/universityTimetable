package com.foxminded.university_timetable.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseDaoTest {

	@Autowired
	private CourseDao courseDao;

	@Test
	void givenCourse_whenFindCoursePrerequisites_thenReturnCoursePrerequisites() {
		Course course = new Course(1L, "CS");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(2L, "Math"));
		expected.add(new Course(4L, "History"));
		expected.add(new Course(5L, "Chemistry"));

		List<Course> actual = courseDao.findCoursePrerequisites(course);

		assertEquals(expected, actual);
	}

	@Test
	void givenExistentCourseId_whenFindById_thenReturnOptionalOfCourse() {
		Course course = new Course(2L, "Math");
		List<Course> prerequisites = courseDao.findCoursePrerequisites(course);
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
		expected.add(new Course(1L, "CS"));
		expected.add(new Course(2L, "Math"));
		expected.add(new Course(3L, "Physics"));
		expected.add(new Course(4L, "History"));
		expected.add(new Course(5L, "Chemistry"));

		List<Course> actual = courseDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenCourseId_whenDelete_thenDeleteCourse() {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(2L, "Math"));
		expected.add(new Course(3L, "Physics"));
		expected.add(new Course(4L, "History"));
		expected.add(new Course(5L, "Chemistry"));

		courseDao.delete(new Course(1L, "CS"));

		List<Course> actual = courseDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenNewCourse_whenSave_thenInsertCourse() {
		Course course = new Course(1L, "Calculus", new ArrayList<>());
		int expected = courseDao.findAll().size() + 1;

		courseDao.save(course);
		
		int actual = courseDao.findAll().size();
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentCourse_whenUpdate_thenUpdateCourse() {
		Course course = new Course(1L, "CS-2");
		List<Course> prerequisites = courseDao.findCoursePrerequisites(course);
		course.setPrerequisites(prerequisites);

		courseDao.update(course);

		Optional<Course> expected = Optional.of(course);
		Optional<Course> actual = courseDao.findById(course.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenCourseAndPrerequisite_whenAddCoursePrerequisite_thenAddPrerequisiteToCourse() {
		Course course = new Course(4L, "History");
		Course prerequisite = new Course(5L, "Chemistry");
		List<Course> expected = new ArrayList<>();
		expected.add(prerequisite);

		courseDao.addCoursePrerequisite(course, prerequisite);

		List<Course> actual = courseDao.findCoursePrerequisites(course);
		assertEquals(expected, actual);
	}
}
