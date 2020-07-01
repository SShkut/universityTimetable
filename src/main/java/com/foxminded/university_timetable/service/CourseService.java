package com.foxminded.university_timetable.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university_timetable.dao.CourseDao;
import com.foxminded.university_timetable.exception.DaoException;
import com.foxminded.university_timetable.exception.ServiceException;
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
		try {
			List<Course> result = courseDao.findAll();
			if (result.isEmpty()) {
				throw new ServiceException("No courses found");
			}
			return result;
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}

	public void delete(Course course) {
		try {
			courseDao.delete(course);
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}

	public void update(Course course) {
		try {
			courseDao.update(course);
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}
	
	public void save(Course course) {
		try {
			courseDao.save(course);
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}

	public List<Course> findPrerequisites(Course course) {
		try {
			List<Course> prereqisites = courseDao.findCoursePrerequisites(course);
			if (prereqisites.isEmpty()) {
				throw new ServiceException(String.format("Course %s has no prerequisites", course.getName()));
			}
			return prereqisites;
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}

	public void addCoursePrerequisite(Course course, Course prerequisite) {
		try {
			List<Course> prerequisites = courseDao.findCoursePrerequisites(course);
			boolean isPrerequisitePresent = prerequisites
					.stream()
					.anyMatch(c -> c.equals(prerequisite));
			if (!isPrerequisitePresent) {
				courseDao.addCoursePrerequisite(course, prerequisite);
			} else {
				throw new ServiceException(String.format("Course %s already added as prerequisite for course %s", prerequisite.getName(), course.getName()));
			}
		} catch (DaoException e) {
			throw new ServiceException("Error", e);
		}
	}
}
