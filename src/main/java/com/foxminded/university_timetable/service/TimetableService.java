package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.TimetableDao;
import com.foxminded.university_timetable.model.Timetable;

@Service
public class TimetableService {

	private final TimetableDao timetableDao;

	public TimetableService(TimetableDao timetableDao) {
		this.timetableDao = timetableDao;
	}
	
	public Optional<Timetable> findById(Long id) {
		return timetableDao.findById(id);
	}
	
	public List<Timetable> findAll() {
		return timetableDao.findAll();
	}
	
	public void delete(Timetable timetable) {
		timetableDao.delete(timetable);
	}
	
	public void update(Timetable timetable) {
		timetableDao.update(timetable);
	}
	
	public Timetable save(Timetable timetable) {
		return timetableDao.save(timetable);
	}
}
