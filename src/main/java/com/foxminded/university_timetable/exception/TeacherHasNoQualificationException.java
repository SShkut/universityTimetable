package com.foxminded.university_timetable.exception;

public class TeacherHasNoQualificationException extends ServiceException {

	public TeacherHasNoQualificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TeacherHasNoQualificationException(String message) {
		super(message);
	}
}
