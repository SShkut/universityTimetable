package com.foxminded.university_timetable.exception;

public class RecordAlreadyExistsException extends ServiceException {

	public RecordAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordAlreadyExistsException(String message) {
		super(message);
	}
}
