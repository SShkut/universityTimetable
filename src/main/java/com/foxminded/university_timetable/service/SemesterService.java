package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.SemesterDao;
import com.foxminded.university_timetable.model.Semester;

@Service
public class SemesterService {

	private final SemesterDao semesterDao;
	
	public SemesterService(SemesterDao semesterDao) {
		this.semesterDao = semesterDao;
	}

	public Optional<Semester> findById(Long id) {
		return semesterDao.findById(id);
	}
	
	public List<Semester> findAll() {
		return semesterDao.findAll();
	}
	
	public void delete(Semester semester) {
		semesterDao.delete(semester);
	}
	
	public void update(Semester semester) {
		semesterDao.update(semester);
	}
	
	public void save(Semester semester) {
		semesterDao.save(semester);
	}
}
