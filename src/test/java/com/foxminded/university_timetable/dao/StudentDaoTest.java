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
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class StudentDaoTest {
	
	private StudentDao studentDao;
	private EmbeddedDatabase db;

	@BeforeEach
	void setUp() throws Exception {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		studentDao = new StudentDao(db);
	}

	@Test
	void givenExistentStudentId_whenFindById_thenReturnOptionalOfStudent() {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");
		Optional<Student> expected = Optional.of(student);
		
		Optional<Student> actual = studentDao.findById(student.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentStudentId_whenFindById_thenReturnOptionalOfStudent () {
		Optional<Student> expected = Optional.empty();
		
		Optional<Student> actual = studentDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", null, "cn-126"));
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127"));
		
		List<Student> actual = studentDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudent_whenSave_thenInsertStudent() throws DatabaseUnitException, SQLException {
		Student student = new Student(6L, "fn-6", "ln-6", "623456789", "6234567890", "ln-6@unv.com", null, "cn-623");
		Optional<Student> expected = Optional.of(student);
		
		studentDao.save(student);
		
		Optional<Student> actual = studentDao.findById(student.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudent_whenUpdate_thenUpdateStudent() throws DatabaseUnitException, SQLException {
		Student student = new Student(1L, "fn-11", "ln-11", "223456789", "2234567890", "ln-11@unv.com", null, "cn-1231");
		Optional<Student> expected = Optional.of(student);
		
		studentDao.update(student);
		
		Optional<Student> actual = studentDao.findById(student.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentId_whenDeleteById_thenDeleteStudentWithGivenId() throws DatabaseUnitException, SQLException {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", null, "cn-126"));
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127"));
		
		studentDao.deleteById(1L);
		
		List<Student> actual = studentDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndGroup_whenAddStudentToGroup_thenAddStudentToGroup() throws DatabaseUnitException, SQLException {
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127");
		Group group = new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"), null);
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127"));
		
		studentDao.addStudentToGroup(student, group);
		
		GroupDao groupDao = new GroupDao(db);
		List<Student> actual = groupDao.findStudentsOfGroup(group);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndGroup_whenDeleteStudentFromGroup_thenRemoveStudentFromGroup() throws DatabaseUnitException, SQLException {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");
		Group group = new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), null);
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		
		studentDao.deleteStudentFromGroup(student, group);
		
		GroupDao groupDao = new GroupDao(db);
		List<Student> actual = groupDao.findStudentsOfGroup(group);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndCourse_whenAddStudentToCourse_thenEnrollCourse() throws DatabaseUnitException, SQLException {
		Student student = new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125");
		Course course = new Course(1L, "Math", null);
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125"));
		
		studentDao.addStudentToCourse(student, course);
		
		CourseDao courseDao = new CourseDao(db);
		List<Student> actual = courseDao.findStudentsOfCourse(course);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndCourse_whenDeleteStudentFromCourse_thenLeaveCourse() throws DatabaseUnitException, SQLException {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");
		Course course = new Course(2L, "CS", null);
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2L, "fn-2", "ln-2", "123456798", "1234567891", "ln-2@unv.com", null, "cn-124"));
		
		studentDao.deleteStudentFromCourse(student, course);
		
		CourseDao courseDao = new CourseDao(db);
		List<Student> actual = courseDao.findStudentsOfCourse(course);
		assertEquals(expected, actual);
	}
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
