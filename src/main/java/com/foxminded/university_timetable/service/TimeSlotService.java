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
	
	public TimeSlot save(TimeSlot timeSlot) {
		List<Course> teacherQualifications = teacherDao.findAllTeacherQualifications(timeSlot.getTeacher());
		boolean hasQualification = teacherQualifications.stream()
				.anyMatch(c -> c.equals(timeSlot.getCourse()));
		if (hasQualification) {
			return timeSlotDao.save(timeSlot);
		}
		return new TimeSlot();
	}
	
	public List<TimeSlot> findAllDailyTimetableTimeSlots(DailyTimetable dailyTimetable) {
		return timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
	}
	
	public void addTimeSlotToDailyTimetable(TimeSlot timeSlot, DailyTimetable dailyTimetable) {
		List<TimeSlot> alreadyExistentTimeSlots = timeSlotDao.findAllDailyTimetableTimeSlots(dailyTimetable);
		boolean isGroupFit = alreadyExistentTimeSlots.stream()
				.anyMatch(t -> isGroupFitPredicate(t, timeSlot));
		boolean isTeacherFit = alreadyExistentTimeSlots.stream()
				.anyMatch(t -> isTeacherFitPredicate(t, timeSlot));
		boolean isRoomFree = alreadyExistentTimeSlots.stream()
				.anyMatch(t -> isRoomFreePredicate(t, timeSlot));
		if (isGroupFit && isTeacherFit && isRoomFree) {
			timeSlotDao.addTimeSlotToDailyTimetable(timeSlot, dailyTimetable);
		}
	}

	private boolean isGroupFitPredicate(TimeSlot timeSlotNew, TimeSlot timeSlotExistent) {
		return !(timeSlotNew.getStartTime().equals(timeSlotExistent.getStartTime()) && 
				timeSlotNew.getEndTime().equals(timeSlotExistent.getEndTime()) && 
				timeSlotNew.getGroup().equals(timeSlotExistent.getGroup()));
	}	
	
	private boolean isTeacherFitPredicate(TimeSlot timeSlotNew, TimeSlot timeSlotExistent) {
		return !(timeSlotNew.getStartTime().equals(timeSlotExistent.getStartTime()) && 
				timeSlotNew.getEndTime().equals(timeSlotExistent.getEndTime()) && 
				timeSlotNew.getTeacher().equals(timeSlotExistent.getTeacher()));
	}

	private boolean isRoomFreePredicate(TimeSlot timeSlotNew, TimeSlot timeSlotExistent) {
		return !(timeSlotNew.getStartTime().equals(timeSlotExistent.getStartTime()) && 
				timeSlotNew.getEndTime().equals(timeSlotExistent.getEndTime()) && 
				timeSlotNew.getRoom().equals(timeSlotExistent.getRoom()));
	}
}
