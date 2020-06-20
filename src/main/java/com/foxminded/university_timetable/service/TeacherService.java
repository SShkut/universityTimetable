package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Teacher;

@Service
public class TeacherService {

	private final TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public Optional<Teacher> findById(Long id) {
		return teacherDao.findById(id);
	}
	
	public List<Teacher> findAll() {
		return teacherDao.findAll();
	}
	
	public void delete(Teacher teacher) {
		teacherDao.delete(teacher);
	}
	
	public void update(Teacher teacher) {
		teacherDao.update(teacher);
	}
	
	public void save(Teacher teacher) {
		teacherDao.save(teacher);
	}
	
	public void addTeacherQualification(Teacher teacher, Course course) {
		teacherDao.addTeacherQualification(teacher, course);
	}
	
	public void deleteTeacherQualification(Teacher teacher, Course course) {
		teacherDao.deleteTeacherQualification(teacher, course);
	}
	
	public List<Course> findAllTeacherQualifications(Teacher teacher) {
		return teacherDao.findAllTeacherQualifications(teacher);
	}
}
