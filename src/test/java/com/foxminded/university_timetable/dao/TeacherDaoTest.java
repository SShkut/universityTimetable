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
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class TeacherDaoTest {
	
	private TeacherDao teacherDao;
	private EmbeddedDatabase db;

	@BeforeEach
	void setUp() throws Exception {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		teacherDao = new TeacherDao(db);
	}
	
	@Test
	void givenExistentTeacjertId_whenFindById_thenReturnOptionalOfTeacher() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");
		Optional<Teacher> expected = Optional.of(teacher);
		
		Optional<Teacher> actual = teacherDao.findById(teacher.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentTeacherId_whenFindById_thenReturnOptionalOfTeacher() {
		Optional<Teacher> expected = Optional.empty();
		
		Optional<Teacher> actual = teacherDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllTeachers() {
		List<Teacher> expected = new ArrayList<>();
		expected.add(new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD"));
		expected.add(new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", null, "masters"));
		
		List<Teacher> actual = teacherDao.findAll();
		
		assertEquals(expected, actual);

	}
	
	@Test
	void givenTeacher_whenSave_thenInsertTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(3L, "fnt-3", "lnt-3", "343456789", "6634567890", "lnt-3@unv.com", null, "phD");
		Optional<Teacher> expected = Optional.of(teacher);
		
		teacherDao.save(teacher);
		
		Optional<Teacher> actual = teacherDao.findById(teacher.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacher_whenUpdate_thenUpdateTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-11", "lnt-11", "323456789", "6234567890", "lns-11@unv.com", null, "bs");
		Optional<Teacher> expected = Optional.of(teacher);
		
		teacherDao.update(teacher);
		
		Optional<Teacher> actual = teacherDao.findById(teacher.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherId_whenDeleteById_thenDeleteTeacherWithGivenId() throws DatabaseUnitException, SQLException {
		List<Teacher> expected = new ArrayList<>();
		expected.add(new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", null, "masters"));
		
		teacherDao.deleteById(1L);
		
		List<Teacher> actual = teacherDao.findAll();		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacher_whenFindAllTeacherQualifications_thenReturnListOfCourses() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS", null));
		expected.add(new Course(3L, "Physics", null));
		
		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherAndCourse_whenAddTeacherQualification_thenAddQualificationToTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");
		Course course = new Course(2L, "Math", null);
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS", null));
		expected.add(new Course(2L, "Math", null));
		expected.add(new Course(3L, "Physics", null));
		
		teacherDao.addTeacherQualification(teacher, course);
		
		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);		
		assertEquals(expected, actual);		
	}
	
	@Test
	void givenTeacherAndCourse_whenDeleteTeacherQualification_thenRemoveTeacherQualification() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", null, "phD");
		Course course = new Course(1L, "CS", null);
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(3L, "Physics", null));
		
		teacherDao.deleteTeacherQualification(teacher, course);
		
		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);		
		assertEquals(expected, actual);	
	}
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
