package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.GroupDao;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;

@Service
public class GroupService {

	private final GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	public Optional<Group> findById(Long id) {
		return groupDao.findById(id);
	}
	
	public List<Group> findAll() {
		return groupDao.findAll();
	}
	
	public void delete(Group group) {
		groupDao.delete(group);
	}
	
	public void update(Group group) {
		groupDao.update(group);
	}
	
	public Group save(Group group) {
		return groupDao.save(group);
	}
	
	public List<Student> findGroupStudents(Group group) {
		return groupDao.findGroupStudents(group);
	}
}
