package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentDaoTest {

	@Autowired
	private StudentDao studentDao;

	@Test
	void givenStudent_whenFindAllStudentCourses_thenReturnListOfStudentCourses() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS"));
		expected.add(new Course(2L, "Math"));

		List<Course> actual = studentDao.findAllStudentCourses(student);

		assertEquals(expected, actual);
	}

	@Test
	void givenExistentStudentId_whenFindById_thenReturnOptionalOfStudent() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");
		List<Course> courses = studentDao.findAllStudentCourses(student);
		student.setCourses(courses);
		Optional<Student> expected = Optional.of(student);

		Optional<Student> actual = studentDao.findById(student.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentStudentId_whenFindById_thenReturnOptionalOfStudent() {
		Optional<Student> expected = Optional.empty();

		Optional<Student> actual = studentDao.findById(0L);

		assertEquals(expected, actual);
	}

	@Test
	void whenFindAll_thenReturnListOfAllStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", "cn-126"));
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127"));

		List<Student> actual = studentDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenSave_thenInsertStudent() {
		Student student = new Student("fn-6", "ln-6", "623456789", "6234567890", "ln-6@unv.com", new ArrayList<>(),
				"cn-623");
		List<Course> courses = studentDao.findAllStudentCourses(student);
		student.setCourses(courses);

		Student inserted = studentDao.save(student);

		Optional<Student> expected = Optional.of(inserted);
		Optional<Student> actual = studentDao.findById(student.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenUpdate_thenUpdateStudent() {
		Student student = new Student(1L, "fn-11", "ln-11", "223456789", "2234567890", "ln-11@unv.com", "cn-1231");
		List<Course> courses = studentDao.findAllStudentCourses(student);
		student.setCourses(courses);
		Optional<Student> expected = Optional.of(student);

		studentDao.update(student);

		Optional<Student> actual = studentDao.findById(student.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenDelete_thenDeleteStudent() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", "cn-126"));
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127"));

		studentDao.delete(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));

		List<Student> actual = studentDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenStudentAndGroup_whenAddStudentToGroup_thenAddStudentToGroup() {
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127");
		Group group = new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"));
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127"));

		studentDao.addStudentToGroup(student, group);

		List<Student> actual = Arrays
				.asList(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", "cn-127"));
		assertEquals(expected, actual);
	}

	@Test
	void givenStudentAndGroup_whenDeleteStudentFromGroup_thenRemoveStudentFromGroup() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");
		Group group = new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"));
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));

		studentDao.deleteStudentFromGroup(student, group);

		List<Student> actual = Arrays
				.asList(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));
		assertEquals(expected, actual);
	}

	@Test
	void givenStudentAndCourse_whenAddStudentToCourse_thenEnrollCourse() {
		Student student = new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", "cn-125");
		Course course = new Course(1L, "Math");
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", "cn-125"));

		studentDao.addStudentToCourse(student, course);

		List<Student> actual = studentDao.findCourseStudents(course);
		assertEquals(expected, actual);
	}

	@Test
	void givenStudentAndCourse_whenDeleteStudentFromCourse_thenLeaveCourse() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123");
		Course course = new Course(2L, "CS");
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", "cn-124"));

		studentDao.deleteStudentFromCourse(student, course);

		List<Student> actual = studentDao.findCourseStudents(course);
		assertEquals(expected, actual);
	}

	@Test
	void givenCourse_whenFindCourseStudents_thenReturnListOfStudents() {
		Course course = new Course(1L, "CS-2");
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", "cn-123"));

		List<Student> actual = studentDao.findCourseStudents(course);

		assertEquals(expected, actual);
	}
}
