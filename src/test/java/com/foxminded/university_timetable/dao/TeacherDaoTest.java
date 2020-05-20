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
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class TeacherDaoTest {
	
	private TeacherDao teacherDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		teacherDao = new TeacherDao(dataSource);
	}
	
	@Test
	void givenExistentTeacjertId_whenFindById_thenReturnOptionalOfTeacher() {
		Optional<Teacher> expected = Optional.of(new Teacher(1L, "fnt-1", "lnt-1", "t23456789", "t234567890", "lnt-1@unv.com", null, "phD"));
		
		Optional<Teacher> actual = teacherDao.findById(1L);
		
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
		expected.add(new Teacher(1L, "fnt-1", "lnt-1", "t23456789", "t234567890", "lnt-1@unv.com", null, "phD"));
		expected.add(new Teacher(2L, "fnt-2", "lnt-2", "t23456798", "t234567891", "lnt-2@unv.com", null, "masters"));
		
		List<Teacher> actual = teacherDao.findAll();
		
		assertEquals(expected, actual);

	}
	
	@Test
	void givenTeacher_whenSave_thenInsertTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacherForInsert = new Teacher(3L, "fnt-3", "lnt-3", "tt3456789", "tt34567890", "lnt-3@unv.com", null, "phD");
		
		teacherDao.save(teacherForInsert);
		
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "teachers"), getActualTable("teachers"));
	}
	
	@Test
	void givenTeacher_whenUpdate_thenUpdateTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacherForUpdate = new Teacher(1L, "fnt-11", "lnt-11", "s23456789", "s234567890", "lns-11@unv.com", null, "bs");
		
		teacherDao.update(teacherForUpdate);
		
		assertEquals(getExpectedTable("testDataExpectedAfterUpdate.xml", "teachers"), getActualTable("teachers"));
	}
	
	@Test
	void givenTeacherId_whenDeleteById_thenDeleteTeacherWithGivenId() throws DatabaseUnitException, SQLException {
		teacherDao.deleteById(1L);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "teachers"), getActualTable("teachers"));
	}
	
	@Test
	void givenTeacherAndCourse_whenAddTeacherQualification_thenAddQualificationToTeacher() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "t23456789", "t234567890", "lnt-1@unv.com", null, "phD");
		Course course = new Course(2L, "Math", null);
		
		teacherDao.addTeacherQualification(teacher, course);
		
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "teacher_course"), getActualTable("teacher_course"));
	}
	
	@Test
	void givenTeacherAndCourse_whenDeleteTeacherQualification_thenRemoveTeacherQualification() throws DatabaseUnitException, SQLException {
		Teacher teacher = new Teacher(1L, "fnt-1", "lnt-1", "t23456789", "t234567890", "lnt-1@unv.com", null, "phD");
		Course course = new Course(1L, "CS", null);
		
		teacherDao.deleteTeacherQualification(teacher, course);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "teacher_course"), getActualTable("teacher_course"));
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
