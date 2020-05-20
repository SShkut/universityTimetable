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
class GroupDaoTest {

	private GroupDao groupDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;
	
	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		groupDao = new GroupDao(dataSource);
	}

	@Test
	void givenExistentGroupId_whenFindById_thenReturnOptionalOfGroup() {
		Optional<Group> expected = Optional.of(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		
		Optional<Group> actual = groupDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentGroupId_whenFindById_thenRetrunEmptyOptional() {
		Optional<Group> expected = Optional.empty();
		
		Optional<Group> actual = groupDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllGroups() {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1L, "cs-1", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		expected.add(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		expected.add(new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"), null));
		
		List<Group> actual = groupDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenGroupId_whenDelete_thenDeleteGroupWithGivenId() throws DatabaseUnitException, SQLException {		
		groupDao.deleteById(1L);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "groups"), getActualTable("groups"));	
	}
	
	@Test
	void givenNewGroup_whenSave_thenInsertGroup() throws DatabaseUnitException, SQLException {		
		groupDao.save(new Group(4L, "cs-4", "css", "csg", new Semester(1L, 2020, "summer"), null));
			
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "groups"), getActualTable("groups"));
	}
	
	@Test
	void givenExistentGroup_whenUpdate_thenUpdateGroup() throws DatabaseUnitException, SQLException {		
		groupDao.update(new Group(1L, "cs-4", "css", "csg", new Semester(2L, 2020, "winter"), null));
		
		assertEquals(getExpectedTable("testDataExpectedAfterUpdate.xml", "groups"), getActualTable("groups"));
	}
	
	@Test
	void givenGroup_whenFindStudentsOfGroup_thenReturnListOfStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", null, "cn-126"));		
		
		List<Student> actual = groupDao.findStudentsOfGroup(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		
		assertEquals(expected, actual);
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
