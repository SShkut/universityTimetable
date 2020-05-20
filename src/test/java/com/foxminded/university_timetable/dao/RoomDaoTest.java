package com.foxminded.university_timetable.dao;

import static org.dbunit.Assertion.assertEquals;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class RoomDaoTest {
	
	private RoomDao roomDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		roomDao = new RoomDao(dataSource);
	}
	
	@Test
	void givenExistentRoomId_whenFindById_thenReturnOptionalOfRoom() {
		Optional<Room> expected = Optional.of(new Room(2L, "b-1", 70));
		
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
		roomDao.deleteById(1L);
		
		assertEquals(getExpectedTable("testDataExpectedAfterDelete.xml", "rooms"), getActualTable("rooms"));	
	}
	
	@Test
	void givenNewRoom_whenSave_thenInsertRoom() throws DatabaseUnitException, SQLException {		
		roomDao.save(new Room(3L, "c-1", 60));
			
		assertEquals(getExpectedTable("testDataExpectedAfterSave.xml", "rooms"), getActualTable("rooms"));
	}
	
	@Test
	void givenExistentRoom_whenUpdate_thenUpdateRoom() throws DatabaseUnitException, SQLException {		
		roomDao.update(new Room(1L, "a-2", 90));
		
		assertEquals(getExpectedTable("testDataExpectedAfterUpdate.xml", "rooms"), getActualTable("rooms"));
	}

	private ITable getActualTable(String tableName) throws DatabaseUnitException, SQLException {
		IDatabaseConnection conn = new DatabaseConnection(dataSource.getConnection());
		return conn.createDataSet().getTable(tableName);
	}
	
	private ITable getExpectedTable(String fileName, String tableName) throws DataSetException {
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource(fileName));
		return expectedDataSet.getTable(tableName);
	}
}
