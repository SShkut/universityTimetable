package com.foxminded.university_timetable.exception;

public class UniversityResourceNotAvailableException extends ServiceException {

	public UniversityResourceNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniversityResourceNotAvailableException(String message) {
		super(message);
	}
}
