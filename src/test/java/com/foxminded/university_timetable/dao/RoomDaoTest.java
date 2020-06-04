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
import com.foxminded.university_timetable.model.Room;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomDaoTest {
	
	@Autowired
	private RoomDao roomDao;
	
	@Test
	void givenExistentRoomId_whenFindById_thenReturnOptionalOfRoom() {
		Room room = new Room(2L, "b-1", 70);
		Optional<Room> expected = Optional.of(room);
		
		Optional<Room> actual = roomDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentRoomId_whenFindById_thenReturnEmptyOptional() {
		Optional<Room> expected = Optional.empty();
		
		Optional<Room> actual = roomDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllRooms() {
		List<Room> expected = new ArrayList<>();
		expected.add(new Room(1L, "a-1", 100));
		expected.add(new Room(2L, "b-1", 70));
		
		List<Room> actual = roomDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenRoom_whenDelete_thenDeleteRoom() {
		List<Room> expected = new ArrayList<>();
		expected.add(new Room(2L, "b-1", 70));
		
		roomDao.delete(new Room(1L, null, null));
		
		List<Room> actual = roomDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewRoom_whenSave_thenInsertRoom() {
		Room room = new Room(null, "c-1", 60);
		
		Room inserted = roomDao.save(room);
		
		Optional<Room> expected = Optional.of(inserted);
		Optional<Room> actual = roomDao.findById(room.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentRoom_whenUpdate_thenUpdateRoom() {
		Room room = new Room(1L, "a-2", 90);
		Optional<Room> expected = Optional.of(room);
		
		roomDao.update(new Room(1L, "a-2", 90));
		
		Optional<Room> actual = roomDao.findById(room.getId());
		assertEquals(expected, actual);
	}
}
