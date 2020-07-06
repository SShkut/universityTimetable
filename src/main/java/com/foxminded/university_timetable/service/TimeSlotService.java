package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.dao.TimeSlotDao;
import com.foxminded.university_timetable.exception.UniversityResourceNotAvailableException;
import com.foxminded.university_timetable.exception.TeacherHasNoQualificationException;
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
		} else {
			throw new TeacherHasNoQualificationException(String.format("Teacher %s %s has no qualification for course %s",
					timeSlot.getTeacher().getFirstName(), timeSlot.getTeacher().getLastName(),
					timeSlot.getCourse().getName()));
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
				} else {
					throw new UniversityResourceNotAvailableException(String.format("Room %s is not available from %s to %s", timeSlot.getRoom(),
							timeSlot.getStartTime().toString(), timeSlot.getEndTime().toString()));
				}
			} else {
				throw new UniversityResourceNotAvailableException(String.format("Teacher %s is not available from %s to %s", timeSlot.getTeacher(),
						timeSlot.getStartTime().toString(), timeSlot.getEndTime().toString()));
			}
		} else {
			throw new UniversityResourceNotAvailableException(String.format("Group %s is already has class from %s to %s", timeSlot.getRoom(),
					timeSlot.getStartTime().toString(), timeSlot.getEndTime().toString()));
		}
	}
}
