package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.GroupDao;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	private GroupDao groupDao;
	
	@InjectMocks
	private GroupService groupService;
	
	@Test
	void whenFindAll_thenReturnAllGroups() {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1L, "", "", "", new Semester()));
		expected.add(new Group(2L, "", "", "", new Semester()));
		when(groupDao.findAll()).thenReturn(expected);
		
		List<Group> actual = groupService.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentGroupId_whenFindById_thenReturnOptionalOfGroup() {
		Group group = new Group(1L, "", "", "", new Semester());
		Optional<Group> expected = Optional.of(group);
		when(groupDao.findById(group.getId())).thenReturn(expected);
		
		Optional<Group> actual = groupService.findById(group.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentGroupId_whenFindById_thenReturnEmptyOptional() {
		Group group = new Group(-1L, "", "", "", new Semester());
		Optional<Group> expected = Optional.empty();
		when(groupDao.findById(group.getId())).thenReturn(expected);
		
		Optional<Group> actual = groupService.findById(group.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenGroup_whenDelete_thenDeleteGroup() {
		Group group = new Group(1L, "", "", "", new Semester());
		
		groupService.delete(group);
		
		verify(groupDao).delete(group);
	}
	
	@Test
	void givenGroup_whenUpdate_thenUpdateGroup() {
		Group group = new Group(1L, "", "", "", new Semester());
		
		groupService.update(group);
		
		verify(groupDao).update(group);
	}
	
	@Test
	void givenGroup_whenSave_thenSaveGroup() {
		Group group = new Group(1L, "", "", "", new Semester());
		
		groupService.save(group);
		
		verify(groupDao).save(group);
	}
	
	@Test
	void givenGroup_whenFindGroupStudents_thenReturnStudentsOfGroup() {
		Group group = new Group(1L, "", "", "", new Semester());
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1L, "", "", "", "", "", ""));
		expected.add(new Student(2L, "", "", "", "", "", ""));
		when(groupDao.findGroupStudents(group)).thenReturn(expected);
		
		List<Student> actual = groupService.findGroupStudents(group);
		
		assertEquals(expected, actual);
	}
}
