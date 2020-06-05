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
import com.foxminded.university_timetable.model.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherDaoTest {

	@Autowired
	private TeacherDao teacherDao;

	@Test
	void givenTeacherAndCourse_whenAddTeacherQualification_thenAddQualificationToTeacher()
			throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		Course course = new Course(2L, "Math");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS"));
		expected.add(new Course(2L, "Math"));
		expected.add(new Course(3L, "Physics"));

		teacherDao.addTeacherQualification(teacher, course);

		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherAndCourse_whenDeleteTeacherQualification_thenRemoveTeacherQualification()
			throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		Course course = new Course(1L, "CS");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(3L, "Physics"));

		teacherDao.deleteTeacherQualification(teacher, course);

		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentTeacjertId_whenFindById_thenReturnOptionalOfTeacher() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		List<Course> qualification = teacherDao.findAllTeacherQualifications(teacher);
		teacher.setCourses(qualification);
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
		expected.add(new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD"));
		expected.add(new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", "masters"));

		List<Teacher> actual = teacherDao.findAll();

		assertEquals(expected, actual);

	}

	@Test
	void givenTeacher_whenSave_thenInsertTeacher() {
		Teacher teacher = new Teacher("fnt-3", "lnt-3", "343456789", "6634567890", "lnt-3@unv.com", new ArrayList<>(),
				"phD");

		Teacher inserted = teacherDao.save(teacher);

		Optional<Teacher> expected = Optional.of(inserted);
		Optional<Teacher> actual = teacherDao.findById(teacher.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacher_whenUpdate_thenUpdateTeacher() {
		Teacher teacher = new Teacher(1L, "fnt-11", "lnt-11", "323456789", "6234567890", "lns-11@unv.com",
				new ArrayList<>(), "bs");
		List<Course> qualification = teacherDao.findAllTeacherQualifications(teacher);
		teacher.setCourses(qualification);
		Optional<Teacher> expected = Optional.of(teacher);

		teacherDao.update(teacher);

		Optional<Teacher> actual = teacherDao.findById(teacher.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacher_whenDelete_thenDeleteTeacher() {
		List<Teacher> expected = new ArrayList<>();
		Teacher teacher = new Teacher(2L, "fnt-2", "lnt-2", "323456798", "5234567891", "lnt-2@unv.com", "masters");
		expected.add(teacher);

		teacherDao.delete(new Teacher(1L, "fnt-11", "lnt-11", "323456789", "6234567890", "lns-11@unv.com",
				new ArrayList<>(), "bs"));

		List<Teacher> actual = teacherDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenTeacher_whenFindAllTeacherQualifications_thenReturnListOfCourses() {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "223456789", "4234567890", "lnt-1@unv.com", "phD");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1L, "CS"));
		expected.add(new Course(3L, "Physics"));

		List<Course> actual = teacherDao.findAllTeacherQualifications(teacher);

		assertEquals(expected, actual);
	}

}
