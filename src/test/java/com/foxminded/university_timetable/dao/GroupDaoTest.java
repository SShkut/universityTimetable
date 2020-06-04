package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

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
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupDaoTest {

	@Autowired
	private GroupDao groupDao;	
	
	@Test
	void givenGroup_whenFindGroupStudents_thenReturnListOfStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "fn-1", "ln-1", "123456789", "1234567890", "ln-1@unv.com", null, "cn-123"));
		expected.add(new Student(3L, "fn-3", "ln-3", "123456987", "1234567892", "ln-3@unv.com", null, "cn-125"));
		expected.add(new Student(4L, "fn-4", "ln-4", "123459876", "1234567893", "ln-4@unv.com", null, "cn-126"));		
		
		List<Student> actual = groupDao.findGroupStudents(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		
		assertEquals(expected, actual);
	}

	@Test
	void givenExistentGroupId_whenFindById_thenReturnOptionalOfGroup() {
		Group group = new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null);
		List<Student> students = groupDao.findGroupStudents(group);
		group.setStudents(students);
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
	void givenGroup_whenDelete_thenDeleteGroup() {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(2L, "cs-2", "cs", "cs", new Semester(1L, 2020, "summer"), null));
		expected.add(new Group(3L, "cs-3", "cs", "cs", new Semester(2L, 2020, "winter"), null));
		
		
		groupDao.delete(new Group(1L, null, null, null, null, null));
		
		List<Group> actual = groupDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewGroup_whenSave_thenInsertGroup() {
		Group group = new Group(null, "cs-4", "css", "csg", new Semester(1L, 2020, "summer"), new ArrayList<>());
		List<Student> students = groupDao.findGroupStudents(group);
		group.setStudents(students);
		
		Group inserted = groupDao.save(group);
		
		Optional<Group> expected = Optional.of(inserted);
		Optional<Group> actual = groupDao.findById(group.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentGroup_whenUpdate_thenUpdateGroup() {
		Group group = new Group(1L, "cs-4", "css", "csg", new Semester(2L, 2020, "winter"), null);
		List<Student> students = groupDao.findGroupStudents(group);
		group.setStudents(students);
		Optional<Group> expected = Optional.of(group);
		
		groupDao.update(group);
		
		Optional<Group> actual = groupDao.findById(group.getId());
		assertEquals(expected, actual);
	}

}
