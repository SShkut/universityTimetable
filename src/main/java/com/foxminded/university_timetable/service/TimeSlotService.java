package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.dao.TimeSlotDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.TimeSlot;

@Service
public class TimeSlotService {

	private final TimeSlotDao timeSlotDao;
	private final TeacherDao teacherDao;

	public TimeSlotService(TimeSlotDao timeSlotDao, TeacherDao teacherDao) {
		this.timeSlotDao = timeSlotDao;
		this.teacherDao = teacherDao;
	}
	
	public Optional<TimeSlot> findById(Long id) {
		return timeSlotDao.findById(id);
	}
	
	public List<TimeSlot> findAll() {
		return timeSlotDao.findAll();
	}
	
	public void delete(TimeSlot timeSlot) {
		timeSlotDao.delete(timeSlot);
	}
	
	public void update(TimeSlot timeSlot) {
		timeSlotDao.update(timeSlot);
	}
	
	public void save(TimeSlot timeSlot) {
		List<Course> teacherQualifications = teacherDao.findAllTeacherQualifications(timeSlot.getTeacher());
		boolean hasQualification = teacherQualifications.stream()
				.anyMatch(c -> c.equals(timeSlot.getCourse()));
		if (hasQualification) {
			timeSlotDao.save(timeSlot);
		}
	}
	
	public List<TimeSlot> findAllDailyTimetableTimeSlots(DailyTimetable dailyTimetable) {
		return timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
	}
	
	public void addTimeSlotToDailyTimetable(TimeSlot timeSlot, DailyTimetable dailyTimetable) {
		boolean isGroupAvailable = !timeSlotDao.findByGroupAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getGroup()).isPresent();
		if (isGroupAvailable) {
			boolean isTeacherAvalable = !timeSlotDao.findByTeacherAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getTeacher()).isPresent();
			if(isTeacherAvalable) {
				boolean isRoomAvailable = !timeSlotDao.findByRoomAndTime(dailyTimetable, timeSlot.getStartTime(), timeSlot.getEndTime(), timeSlot.getRoom()).isPresent();
				if (isRoomAvailable) {
					timeSlotDao.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
				}
			}
		}
	}
}
