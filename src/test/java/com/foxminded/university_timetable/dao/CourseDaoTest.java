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

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class CourseDaoTest {
	
	private CourseDao courseDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;
	
	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		courseDao = new CourseDao(dataSource);
	}

	@Test
	void givenExistentCourseId_whenFindById_thenReturnOptionalOfCourse() {
		Optional<Course> expected = Optional.of(new Course(2L, "Math", null));
		
		Optional<Course> actual = courseDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentCourseId_whenFindById_thenRetrunEmptyOptional() {
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
		courseDao.delteById(1L);
		
		assertEquals(getExpectedTable("coursesExpectedAfterDelete.xml"), getActualTable());	
	}
	
	@Test
	void givenNewCourse_whenSave_thenInsertCourse() throws DatabaseUnitException, SQLException {		
		courseDao.save(new Course(5L, "Calculus", null));
			
		assertEquals(getExpectedTable("coursesExpectedAfterSave.xml"), getActualTable());
	}
	
	@Test
	void givenExistentCourse_whenUpdate_thenUpdateCourse() throws DatabaseUnitException, SQLException {		
		courseDao.update(new Course(1L, "CS-2", null));
		
		assertEquals(getExpectedTable("coursesExpectedAfterUpdate.xml"), getActualTable());
	}

	private ITable getActualTable() throws DatabaseUnitException, SQLException {
		IDatabaseConnection conn = new DatabaseConnection(dataSource.getConnection());
		return conn.createDataSet().getTable("courses");
	}
	
	private ITable getExpectedTable(String fileName) throws DataSetException {
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource(fileName));
		return expectedDataSet.getTable("courses");
	}
}
