package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dbunit.DatabaseUnitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class CourseDaoTest {
	
	private CourseDao courseDao;
	private EmbeddedDatabase db;
	
	@BeforeEach
	void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		courseDao = new CourseDao(db);
	}

	@Test
	void givenExistentCourseId_whenFindById_thenReturnOptionalOfCourse() {
		Course course = new Course(2L, "Math", null);
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
		
		List<Course> actual = courseDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenDelete_thenDeleteCourseWithGivenId() throws DatabaseUnitException, SQLException {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(2L, "Math", null));
		expected.add(new Course(3L, "Physics", null));
		expected.add(new Course(4L, "History", null));
		
		courseDao.delteById(1L);
		
		List<Course> actual = courseDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewCourse_whenSave_thenInsertCourse() throws DatabaseUnitException, SQLException {
		Course course = new Course(5L, "Calculus", null);
		Optional<Course> expected = Optional.of(course);
		
		courseDao.save(course);

		Optional<Course> actual = courseDao.findById(course.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentCourse_whenUpdate_thenUpdateCourse() throws DatabaseUnitException, SQLException {
		Course course = new Course(1L, "CS-2", null);
		Optional<Course> expected = Optional.of(course);
		
		courseDao.update(new Course(1L, "CS-2", null));
		
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
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
