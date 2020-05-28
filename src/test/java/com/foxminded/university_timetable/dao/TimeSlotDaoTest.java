package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.config.TestJdbcConfig;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimeSlotDaoTest {

	@Autowired
	private TimeSlotDao timeSlotDao;
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired 
	private RoomDao roomDao;
	
	@Autowired
	private DailyTimetableDao dailyTimetableDao;
	
	@Test
	void givenExistentTimeSlotId_whenFindById_thenReturnOptionalOfTimeSlot() {
		Optional<Course> course = courseDao.findById(1L);
		Optional<Teacher> teacher = teacherDao.findById(1L);
		Optional<Group> group = groupDao.findById(1L);
		Optional<Room> room = roomDao.findById(1L);
		TimeSlot timeSlot = new TimeSlot(1L, 
				LocalTime.of(9, 0), 
				LocalTime.of(9, 30), 
				course.orElseThrow(NoSuchElementException::new), 
				teacher.orElseThrow(NoSuchElementException::new), 
				group.orElseThrow(NoSuchElementException::new), 
				room.orElseThrow(NoSuchElementException::new));
		Optional<TimeSlot> expected = Optional.of(timeSlot);
		
		Optional<TimeSlot> actual = timeSlotDao.findById(timeSlot.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimeSlot_whenFindAll_returnListOfTimeSlots() {
			List<TimeSlot> expected = new ArrayList<>();
			Optional<Course> course1 = courseDao.findById(1L);
			Optional<Course> course2 = courseDao.findById(2L);
			Optional<Teacher> teacher1 = teacherDao.findById(1L);
			Optional<Teacher> teacher2 = teacherDao.findById(2L);
			Optional<Group> group1 = groupDao.findById(1L);
			Optional<Group> group2 = groupDao.findById(2L);
			Optional<Room> room1 = roomDao.findById(1L);
			Optional<Room> room2 = roomDao.findById(2L);
			TimeSlot timeSlot = new TimeSlot(1L, 
					LocalTime.of(9, 0), 
					LocalTime.of(9, 30), 
					course1.orElseThrow(NoSuchElementException::new), 
					teacher1.orElseThrow(NoSuchElementException::new), 
					group1.orElseThrow(NoSuchElementException::new), 
					room1.orElseThrow(NoSuchElementException::new));
			expected.add(timeSlot);
			timeSlot = new TimeSlot(2L, 
					LocalTime.of(9, 0), 
					LocalTime.of(9, 30), 
					course2.orElseThrow(NoSuchElementException::new), 
					teacher2.orElseThrow(NoSuchElementException::new), 
					group2.orElseThrow(NoSuchElementException::new), 
					room2.orElseThrow(NoSuchElementException::new));
			expected.add(timeSlot);
			timeSlot = new TimeSlot(3L, 
					LocalTime.of(10, 00), 
					LocalTime.of(11, 50), 
					course1.orElseThrow(NoSuchElementException::new), 
					teacher1.orElseThrow(NoSuchElementException::new), 
					group1.orElseThrow(NoSuchElementException::new), 
					room1.orElseThrow(NoSuchElementException::new));
			expected.add(timeSlot);
			
			List<TimeSlot> actual = timeSlotDao.findAll();
			
			assertEquals(expected, actual);			
	}
	
	@Test
	void givenTimeSlot_whenSave_thenInsertTimeSlot() {
		Optional<Course> course = courseDao.findById(4L);
		Optional<Teacher> teacher = teacherDao.findById(2L);
		Optional<Group> group = groupDao.findById(2L);
		Optional<Room> room = roomDao.findById(2L);
		TimeSlot timeSlot = new TimeSlot(4L, 
				LocalTime.of(16, 0), 
				LocalTime.of(17, 30), 
				course.orElseThrow(NoSuchElementException::new), 
				teacher.orElseThrow(NoSuchElementException::new), 
				group.orElseThrow(NoSuchElementException::new), 
				room.orElseThrow(NoSuchElementException::new));
		Optional<TimeSlot> expected = Optional.of(timeSlot);
		
		timeSlotDao.save(timeSlot);
		
		Optional<TimeSlot> actual = timeSlotDao.findById(timeSlot.getId());		
		assertEquals(expected, actual);
	}

	@Test
	void givenTimeSlot_whenUpdate_thenUpdateGivenTimeSlot() {
		Optional<Course> course = courseDao.findById(2L);
		Optional<Teacher> teacher = teacherDao.findById(2L);
		Optional<Group> group = groupDao.findById(2L);
		Optional<Room> room = roomDao.findById(2L);
		TimeSlot timeSlot = new TimeSlot(1L, 
				LocalTime.of(16, 0), 
				LocalTime.of(17, 30), 
				course.orElseThrow(NoSuchElementException::new), 
				teacher.orElseThrow(NoSuchElementException::new), 
				group.orElseThrow(NoSuchElementException::new), 
				room.orElseThrow(NoSuchElementException::new));
		Optional<TimeSlot> expected = Optional.of(timeSlot);
		
		timeSlotDao.update(timeSlot);
		
		Optional<TimeSlot> actual = timeSlotDao.findById(timeSlot.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimeSlotId_whenDelete_thenDeleteTimeSlot() {
		Optional<TimeSlot> timeSlot = timeSlotDao.findById(1L);
		assertTrue(timeSlot.isPresent());
		
		timeSlotDao.deleteById(1L);
		
		timeSlot = timeSlotDao.findById(1L);
		assertTrue(!timeSlot.isPresent());
	}
	
	@Test
	void givenDailyTimetable_whenFindAllTimeSlotsOfDailyTimetable_thenReturnListOfTimeslots() {
		Optional<DailyTimetable> dailyTimetable = dailyTimetableDao.findById(2L);
		Optional<TimeSlot> timeSlot = timeSlotDao.findById(3L);		
		List<TimeSlot> expected = new ArrayList<>();
		expected.add(timeSlot.orElseThrow(NoSuchElementException::new));
		
		List<TimeSlot> actual = timeSlotDao.findAllTimeSlotsOfDailyTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new));
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimeSlotAndDailyTimetable_whenAddTimeSlotToDailyTimetable_thenAddTimeSlotToDailyTimetable() {
		Optional<DailyTimetable> dailyTimetable = dailyTimetableDao.findById(2L);
		Optional<TimeSlot> timeSlot1 = timeSlotDao.findById(2L);
		Optional<TimeSlot> timeSlot2 = timeSlotDao.findById(3L);
		List<TimeSlot> expected = new ArrayList<>();
		expected.add(timeSlot1.orElseThrow(NoSuchElementException::new));
		expected.add(timeSlot2.orElseThrow(NoSuchElementException::new));
		
		timeSlotDao.addTimeSlotToDailyTimetable(timeSlot1.orElseThrow(NoSuchElementException::new),
				dailyTimetable.orElseThrow(NoSuchElementException::new));
		
		List<TimeSlot> actual = timeSlotDao.findAllTimeSlotsOfDailyTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new));
		assertEquals(expected, actual);
	}
}
