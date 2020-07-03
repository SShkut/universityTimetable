package com.foxminded.university_timetable.exception;

public class ActiveIsNotAvailableException extends ServiceException {

	public ActiveIsNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActiveIsNotAvailableException(String message) {
		super(message);
	}
}
