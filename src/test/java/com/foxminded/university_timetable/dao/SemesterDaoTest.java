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
import com.foxminded.university_timetable.model.Semester;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestJdbcConfig.class })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class SemesterDaoTest {

	@Autowired
	private SemesterDao semesterDao;

	@Test
	void givenExistentSemesterId_whenFindById_thenReturnOptionalOfSemester() {
		Semester semester = new Semester(2L, 2020, "winter");
		Optional<Semester> expected = Optional.of(semester);

		Optional<Semester> actual = semesterDao.findById(semester.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentSemesterId_whenFindById_thenReturnEmptyOptional() {
		Optional<Semester> expected = Optional.empty();

		Optional<Semester> actual = semesterDao.findById(0L);

		assertEquals(expected, actual);
	}

	@Test
	void whenFindAll_thenReturnListOfAllSemesters() {
		List<Semester> expected = new ArrayList<>();
		expected.add(new Semester(1L, 2020, "summer"));
		expected.add(new Semester(2L, 2020, "winter"));

		List<Semester> actual = semesterDao.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenSemester_whenDelete_thenDeleteSemester() {
		List<Semester> expected = new ArrayList<>();
		expected.add(new Semester(2L, 2020, "winter"));

		semesterDao.delete(new Semester(1L, 2020, "summer"));

		List<Semester> actual = semesterDao.findAll();
		assertEquals(expected, actual);
	}

	@Test
	void givenNewSemester_whenSave_thenInsertSemester() {
		Semester semester = new Semester(2021, "winter");

		Semester inserted = semesterDao.save(semester);

		Optional<Semester> expected = Optional.of(inserted);
		Optional<Semester> actual = semesterDao.findById(semester.getId());
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentSemester_whenUpdate_thenUpdateSemester() {
		Semester semester = new Semester(1L, 2019, "winter");
		Optional<Semester> expected = Optional.of(semester);

		semesterDao.update(semester);

		Optional<Semester> actual = semesterDao.findById(semester.getId());
		assertEquals(expected, actual);
	}
}
