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

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class StudentDaoTest {
	
	private StudentDao studentDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		studentDao = new StudentDao(dataSource);
	}

	@Test
	void givenExistentStudentId_whenFindById_thenReturnOptionalOfStudent() {
		Optional<Student> expected = Optional.of(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		
		Optional<Student> actual = studentDao.findById(1L);
		
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
		Student studentForInsert = new Student(6L, "fn-6", "ln-6", "623456789", "6234567890", "ln-6@unv.com", null, "cn-623");
		
		studentDao.save(studentForInsert);
		
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "students"), getActualTable("students"));
	}
	
	@Test
	void givenStudent_whenUpdate_thenUpdateStudent() throws DatabaseUnitException, SQLException {
		Student studentForUpdate = new Student(1L, "fn-11", "ln-11", "223456789", "2234567890", "ln-11@unv.com", null, "cn-1231");
		
		studentDao.update(studentForUpdate);
		
		assertEquals(getExpectedTable("testDataExpectedAfterUpdate.xml", "students"), getActualTable("students"));
	}
	
	@Test
	void givenStudentId_whenDeleteById_thenDeleteStudentWithGivenId() throws DatabaseUnitException, SQLException {
		studentDao.deleteById(1L);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "students"), getActualTable("students"));
	}
	
	@Test
	void givenStudentAndGroup_whenAddStudentToGroup_thenAddStudentToGroup() throws DatabaseUnitException, SQLException {
		Student student = new Student(5L, "fn-5", "ln-5", "123498765", "1234567894", "ln-5@unv.com", null, "cn-127");
		Group group = new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"), null);
		
		studentDao.addStudentToGroup(student, group);
		
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "student_group"), getActualTable("student_group"));
	}
	
	@Test
	void givenStudentAndGroup_whenDeleteStudentFromGroup_thenRemoveStudentFromGroup() throws DatabaseUnitException, SQLException {
		Student student = new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123");
		Group group = new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), null);
		
		studentDao.deleteStudentFromGroup(student, group);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "student_group"), getActualTable("student_group"));
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
