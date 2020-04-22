package com.foxminded.university_timetable.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Timetable {
	
	private String name;
	private List<DailyTimetable> timetables;	
	
	public Timetable(String name) {
		this.name = name;
		this.timetables = new ArrayList<>();
	}

	public Timetable(String name, List<DailyTimetable> timetables) {
		this.name = name;
		this.timetables = timetables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DailyTimetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(List<DailyTimetable> timetables) {
		this.timetables = timetables;
	}
	
	public void addDailyTimetable(DailyTimetable dailyTimetable) {
		getTimetables().add(dailyTimetable);
	}
	
	public Optional<DailyTimetable> getTimetableDay(LocalDate date, Person person) {
		Optional<DailyTimetable> timetable = timetables.stream()
				.filter(table -> table.getDate().equals(date))
				.findFirst();
		if (timetable.isPresent()) {
			if (person instanceof Student) {
				List<TimeSlot> slots =  timetable.get().getTimeSlots().stream()
						.filter(s -> s.getGroup().getStudents().contains(person))
						.filter(s -> s.getGroup().getSemester().getYearOfStudy() == date.getYear())
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				return Optional.of(new DailyTimetable(date, slots));								
			} else if (person instanceof Teacher) {
				List<TimeSlot> slots = timetable.get().getTimeSlots().stream()
						.filter(slot -> slot.getTeacher().equals(person))
						.collect(Collectors.toList());
				return Optional.of(new DailyTimetable(date, slots));	
			}
		}		
		return Optional.empty();
	}
	
	public List<DailyTimetable> getTimetableMonth(Month month, Person person) {
		List<DailyTimetable> result = new ArrayList<>();
		int year = LocalDate.now().getYear();
		IntStream.rangeClosed(1, YearMonth.of(year, month).lengthOfMonth())
			.mapToObj(day -> LocalDate.of(year, month, day)).forEach(date -> {
				Optional<DailyTimetable> timetable = getTimetableDay(date, person);
				if (timetable.isPresent()) {
					result.add(timetable.get());
				}
			});		
		return result;
	}
}