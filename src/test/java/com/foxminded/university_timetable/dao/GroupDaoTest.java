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

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class GroupDaoTest {

	private GroupDao groupDao;
	private EmbeddedDatabase db;
	
	@BeforeEach
	void setUp() throws Exception {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		groupDao = new GroupDao(db);
	}

	@Test
	void givenExistentGroupId_whenFindById_thenReturnOptionalOfGroup() {
		Group group = new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null);
		Optional<Group> expected = Optional.of(group);
		
		Optional<Group> actual = groupDao.findById(group.getId());		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentGroupId_whenFindById_thenReturnEmptyOptional() {
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
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		expected.add(new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"), null));
		
		groupDao.deleteById(1L);
		
		List<Group> actual = groupDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewGroup_whenSave_thenInsertGroup() throws DatabaseUnitException, SQLException {
		Group group = new Group(4L, "cs-4", "css", "csg", new Semester(1L, 2020, "summer"), null);
		Optional<Group> expected = Optional.of(group);
		
		groupDao.save(group);
			
		Optional<Group> actual = groupDao.findById(group.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentGroup_whenUpdate_thenUpdateGroup() throws DatabaseUnitException, SQLException {
		Group group = new Group(1L, "cs-4", "css", "csg", new Semester(2L, 2020, "winter"), null);
		Optional<Group> expected = Optional.of(group);
		
		groupDao.update(group);
		
		Optional<Group> actual = groupDao.findById(group.getId());
		assertEquals(expected, actual);
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
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
