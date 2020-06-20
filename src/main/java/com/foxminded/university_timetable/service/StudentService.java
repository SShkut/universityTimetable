package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.GroupDao;
import com.foxminded.university_timetable.dao.StudentDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Student;

@Service
public class StudentService {
	
	private final static int MAX_GROUP_SIZE = 30;

	private final StudentDao studentDao;
	private final GroupDao groupDao;

	public StudentService(StudentDao studentDao, GroupDao groupDao) {
		this.studentDao = studentDao;
		this.groupDao = groupDao;
	}

	public Optional<Student> findById(Long id) {
		return studentDao.findById(id);
	}
	
	public List<Student> findAll() {
		return studentDao.findAll();
	}
	
	public void delete(Student student) {
		studentDao.delete(student);
	}
	
	public void update(Student student) {
		studentDao.update(student);
	}
	
	public void save(Student student) {
		studentDao.save(student);
	}

	public void addStudentToGroup(Student student, Group group) {
		int groupSize = groupDao.findGroupStudents(group).size();
		if (groupSize < MAX_GROUP_SIZE) {
			studentDao.addStudentToGroup(student, group);
		}
	}
	
	public void deleteStudentFromGroup(Student student, Group group) {
		studentDao.deleteStudentFromGroup(student, group);
	}
	
	public void addStudentToCourse(Student student, Course course) {
		studentDao.addStudentToCourse(student, course);
	}
	
	public void deleteStudentFromCourse(Student student, Course course) {
		studentDao.deleteStudentFromCourse(student, course);
	}
	
	public List<Course> findAllStudentCourses(Student student) {
		return studentDao.findAllStudentCourses(student);
	}
	
	public List<Student> findCourseStudents(Course course) {
		return studentDao.findCourseStudents(course);
	}
}
