package com.foxminded.university_timetable.command;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class DailyTimetableDateRangeCommand {

	@DateTimeFormat(iso = ISO.DATE)
	LocalDate dateFrom;
	@DateTimeFormat(iso = ISO.DATE)
	LocalDate dateTo;

	public DailyTimetableDateRangeCommand(LocalDate dateFrom, LocalDate dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}
}
