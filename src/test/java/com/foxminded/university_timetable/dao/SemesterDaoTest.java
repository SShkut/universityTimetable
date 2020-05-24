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

import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class SemesterDaoTest {
	
	private SemesterDao semesterDao;
	private EmbeddedDatabase db;

	@BeforeEach
	void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		semesterDao = new SemesterDao(db);
	}

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
	void givenSemester_whenDelete_thenDeleteSemesterWithGivenId() throws DatabaseUnitException, SQLException {
		List<Semester> expected = new ArrayList<>();
		expected.add(new Semester(2L, 2020, "winter"));
		
		semesterDao.deleteById(1L);
		
		List<Semester> actual = semesterDao.findAll();
		assertEquals(expected, actual);	
	}
	
	@Test
	void givenNewSemester_whenSave_thenInsertSemester() throws DatabaseUnitException, SQLException {		
		Semester semester = new Semester(3L, 2021, "winter");
		Optional<Semester> expected = Optional.of(semester);
		
		semesterDao.save(new Semester(3L, 2021, "winter"));
			
		Optional<Semester> actual = semesterDao.findById(semester.getId());
		assertEquals(expected, actual);	
	}
	
	@Test
	void givenExistentSemester_whenUpdate_thenUpdateSemester() throws DatabaseUnitException, SQLException {	
		Semester semester = new Semester(1L, 2019, "winter");
		Optional<Semester> expected = Optional.of(semester);
		
		semesterDao.update(semester);
		
		Optional<Semester> actual = semesterDao.findById(semester.getId());
		assertEquals(expected, actual);
	}
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
