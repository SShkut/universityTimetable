package com.foxminded.university_timetable.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.command.DailyTimetableDateRangeCommand;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.service.DailyTimetableService;
import com.foxminded.university_timetable.service.StudentService;

@Controller
public class DailyTimetableController {

	private final DailyTimetableService dailyTimetableService;
	private final StudentService studentService;

	public DailyTimetableController(DailyTimetableService dailyTimetableService, StudentService studentService) {
		this.dailyTimetableService = dailyTimetableService;
		this.studentService = studentService;
	}

	@GetMapping("daily-timetables")
	public String getAll(Model model) {
		model.addAttribute("dailyTimetables", dailyTimetableService.findAll());
		return "daily-timetables/daily-timetables";
	}

	@GetMapping("daily-timetables/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("dailyTimetable", dailyTimetableService.findById(id).orElseThrow(NoSuchElementException::new));
		return "daily-timetables/daily-timetable";
	}

	@GetMapping("daily-timetables/student/{id}")
	public String findTimetableForStudent(@ModelAttribute("dateRange") DailyTimetableDateRangeCommand dateRangeCommand,
			Model model, @PathVariable Long id) {
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		Student student = studentService.findById(id).orElseThrow(NoSuchElementException::new);
		if (dateRangeCommand.getDateFrom() != null || dateRangeCommand.getDateTo() != null) {
			dailyTimetables = dailyTimetableService
					.findTimetableForStudent(student,
							dateRangeCommand.getDateFrom(), dateRangeCommand.getDateTo());
		}
		model.addAttribute("dailyTimetables", dailyTimetables);
		model.addAttribute("student", student);
		return "daily-timetables/student-timetables";
	}
}
