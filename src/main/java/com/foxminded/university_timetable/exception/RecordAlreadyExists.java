package com.foxminded.university_timetable.exception;

public class RecordAlreadyExists extends ServiceException {

	public RecordAlreadyExists(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordAlreadyExists(String message) {
		super(message);
	}
}
