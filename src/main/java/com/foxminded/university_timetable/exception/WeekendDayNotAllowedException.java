package com.foxminded.university_timetable.exception;

public class WeekendDayNotAllowedException extends ServiceException {

	public WeekendDayNotAllowedException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeekendDayNotAllowedException(String message) {
		super(message);
	}
}
