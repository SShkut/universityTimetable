package com.foxminded.university_timetable.exception;

public class GroupSizeExceededException extends ServiceException {

	public GroupSizeExceededException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupSizeExceededException(String message) {
		super(message);
	}
}
