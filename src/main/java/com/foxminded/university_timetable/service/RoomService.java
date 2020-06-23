package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.RoomDao;
import com.foxminded.university_timetable.model.Room;

@Service
public class RoomService {

	private final RoomDao roomDao;

	public RoomService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public Optional<Room> findById(Long id) {
		return roomDao.findById(id);
	}
	
	public List<Room> findAll() {
		return roomDao.findAll();
	}
	
	public void delete(Room room) {
		roomDao.delete(room);
	}
	
	public void update(Room room) {
		roomDao.update(room);
	}
	
	public void save(Room room) {
		roomDao.save(room);
	}
}
