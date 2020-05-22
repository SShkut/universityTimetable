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

import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class RoomDaoTest {
	
	private RoomDao roomDao;
	private EmbeddedDatabase db;

	@BeforeEach
	void setUp() throws Exception {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql")
				.build();
		roomDao = new RoomDao(db);
	}
	
	@Test
	void givenExistentRoomId_whenFindById_thenReturnOptionalOfRoom() {
		Room room = new Room(2L, "b-1", 70);
		Optional<Room> expected = Optional.of(room);
		
		Optional<Room> actual = roomDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentRoomId_whenFindById_thenRetrunEmptyOptional() {
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
	void givenRoomId_whenDelete_thenDeleteRoomWithGivenId() throws DatabaseUnitException, SQLException {
		List<Room> expected = new ArrayList<>();
		expected.add(new Room(2L, "b-1", 70));
		
		roomDao.deleteById(1L);
		
		List<Room> actual = roomDao.findAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewRoom_whenSave_thenInsertRoom() throws DatabaseUnitException, SQLException {
		Room room = new Room(3L, "c-1", 60);
		Optional<Room> expected = Optional.of(room);
		
		roomDao.save(room);
		
		Optional<Room> actual = roomDao.findById(room.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentRoom_whenUpdate_thenUpdateRoom() throws DatabaseUnitException, SQLException {
		Room room = new Room(1L, "a-2", 90);
		Optional<Room> expected = Optional.of(room);
		
		roomDao.update(new Room(1L, "a-2", 90));
		
		Optional<Room> actual = roomDao.findById(room.getId());
		assertEquals(expected, actual);
	}
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
}
