package com.foxminded.university_timetable.dao;

import static org.dbunit.Assertion.assertEquals;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class SemesterDaoTest {
	
	private SemesterDao semesterDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		semesterDao = new SemesterDao(dataSource);
	}

	@Test
	void givenExistentSemesterId_whenFindById_thenReturnOptionalOfSemester() {
		Optional<Semester> expected = Optional.of(new Semester(2L, 2020, "winter"));
		
		Optional<Semester> actual = semesterDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentSemesterId_whenFindById_thenRetrunEmptyOptional() {
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
	void givenSemesterId_whenDelete_thenDeleteSemesterWithGivenId() throws DatabaseUnitException, SQLException {		
		semesterDao.deleteById(1L);
		
		assertEquals(getExpectedTable("semestersExpectedAfterDelete.xml", "semesters"), getActualTable("semesters"));	
	}
	
	@Test
	void givenNewSemester_whenSave_thenInsertSemester() throws DatabaseUnitException, SQLException {		
		semesterDao.save(new Semester(3L, 2021, "winter"));
			
		assertEquals(getExpectedTable("semestersExpectedAfterSave.xml", "semesters"), getActualTable("semesters"));
	}
	
	@Test
	void givenExistentSemester_whenUpdate_thenUpdateSemester() throws DatabaseUnitException, SQLException {		
		semesterDao.update(new Semester(1L, 2019, "winter"));
		
		assertEquals(getExpectedTable("semestersExpectedAfterUpdate.xml", "semesters"), getActualTable("semesters"));
	}

	private ITable getActualTable(String tableName) throws DatabaseUnitException, SQLException {
		IDatabaseConnection conn = new DatabaseConnection(dataSource.getConnection());
		return conn.createDataSet().getTable(tableName);
	}
	
	private ITable getExpectedTable(String fileName, String tableName) throws DataSetException {
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource(fileName));
		return expectedDataSet.getTable(tableName);
	}
}
