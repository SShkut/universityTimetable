package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
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
		
	}

}
