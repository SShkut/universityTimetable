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

import com.foxminded.university_timetable.dao.RoomDao;
import com.foxminded.university_timetable.model.Room;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@Mock
	private RoomDao roomDao;
	
	@InjectMocks
	private RoomService roomService;
	
	@Test
	void whenFindAll_thenReturnAllRooms() {
		List<Room> expected = new ArrayList<>();
		expected.add(new Room(1L, "", 0));
		expected.add(new Room(2L, "", 0));

		when(roomDao.findAll()).thenReturn(expected);
		
		List<Room> actual = roomService.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentRoomId_whenFindById_thenReturnOptionalOfRoom() {
		Room room = new Room(1L, "", 0);
		Optional<Room> expected = Optional.of(room);
		when(roomDao.findById(room.getId())).thenReturn(expected);
		
		Optional<Room> actual = roomService.findById(room.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentRoomId_whenFindById_thenReturnEmptyOptional() {
		Room room = new Room(-1L, "", 0);
		Optional<Room> expected = Optional.empty();
		when(roomDao.findById(room.getId())).thenReturn(expected);
		
		Optional<Room> actual = roomService.findById(room.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenRoom_whenDelete_thenDeleteRoom() {
		Room room = new Room(1L, "", 0);
		
		roomService.delete(room);
		
		verify(roomDao).delete(room);
	}
	
	@Test
	void givenRoom_whenUpdate_thenUpdateRoom() {
		Room room = new Room(1L, "", 0);
		
		roomService.update(room);
		
		verify(roomDao).update(room);
	}
	
	@Test
	void givenRoom_whenSave_thenSaveRoom() {
		Room room = new Room(1L, "", 0);
		
		roomService.save(room);
		
		verify(roomDao).save(room);
	}
}
