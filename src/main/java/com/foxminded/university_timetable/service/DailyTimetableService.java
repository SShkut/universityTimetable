package com.foxminded.university_timetable.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.DailyTimetableDao;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.Timetable;

@Service
public class DailyTimetableService {
	
	private final static Set<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY , DayOfWeek.SUNDAY);
	
	private final DailyTimetableDao dailyTimetableDao;

	public DailyTimetableService(DailyTimetableDao dailyTimetableDao) {
		this.dailyTimetableDao = dailyTimetableDao;
	}

	public Optional<DailyTimetable> findById(Long id) {
		return dailyTimetableDao.findById(id);
	}
	
	public List<DailyTimetable> findAll() {
		return dailyTimetableDao.findAll();
	}
	
	public void delete(DailyTimetable dailyTimetable) {
		dailyTimetableDao.delete(dailyTimetable);
	}
	
	public void update(DailyTimetable dailyTimetable) {
		if (isNotWeekend(dailyTimetable)) {
			dailyTimetableDao.update(dailyTimetable);
		}
	}
	
	public DailyTimetable save(DailyTimetable dailyTimetable) {
		if (isNotWeekend(dailyTimetable)) {
			return dailyTimetableDao.save(dailyTimetable);
		}
		return new DailyTimetable();
	}
	
	private boolean isNotWeekend(DailyTimetable dailyTimetable) {
		return !WEEKEND.contains(dailyTimetable.getDate().getDayOfWeek());
	}
	
	public void addDailyTimetableToTimetable(DailyTimetable dailyTimetable, Timetable timetable) {
		List<DailyTimetable> dailyTimetablesExistent = dailyTimetableDao.findTimetableDailyTimetables(timetable);
		boolean isDateOccupied = dailyTimetablesExistent.stream()
				.anyMatch(d -> d.getDate().equals(dailyTimetable.getDate()));
		if (!isDateOccupied) {
			dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable, timetable);
		}
	}
	
	public Optional<DailyTimetable> findByDate(LocalDate date) {
		return dailyTimetableDao.findByDate(date);
	}
	
	public List<DailyTimetable> findTimetableForStudent(Student student, LocalDate start, LocalDate end) {
		return dailyTimetableDao.findTimetableForStudent(student, start, end);
	}
	
	public List<DailyTimetable> findTimetableForTeacher(Teacher teacher, LocalDate start, LocalDate end) {
		return dailyTimetableDao.findTimetableForTeacher(teacher, start, end);
	}
	
	public List<DailyTimetable> findTimetableDailyTimetables(Timetable timetable) {
		return dailyTimetableDao.findTimetableDailyTimetables(timetable);
	}
}
