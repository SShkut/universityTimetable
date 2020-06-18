package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.CourseDao;
import com.foxminded.university_timetable.model.Course;

@Service
public class CourseService {

	private final CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public Optional<Course> findById(Long id) {
		return courseDao.findById(id);
	}

	public List<Course> findAll() {
		return courseDao.findAll();
	}

	public void delete(Course course) {
		courseDao.delete(course);
	}

	public void update(Course course) {
		courseDao.update(course);
	}
	
	public Course save(Course course) {
		return courseDao.save(course);
	}

	public List<Course> findPrerequisites(Course course) {
		return courseDao.findCoursePrerequisites(course);
	}

	public void addCoursePrerequisite(Course course, Course prerequisite) {
		List<Course> prerequisites = courseDao.findCoursePrerequisites(course);
		Optional<Course> prereqisiteForCheck = prerequisites
				.stream()
				.filter(c -> c.equals(prerequisite))
				.findFirst();
		if (!prereqisiteForCheck.isPresent()) {
			courseDao.addCoursePrerequisite(course, prerequisite);
		}
	}
}
