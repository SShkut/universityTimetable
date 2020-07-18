package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.DailyTimetableService;

@Controller
public class DailyTimetableController {

	private final DailyTimetableService dailyTimetableService;

	public DailyTimetableController(DailyTimetableService dailyTimetableService) {
		this.dailyTimetableService = dailyTimetableService;
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
}
