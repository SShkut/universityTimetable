package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.TimetableService;

@Controller
public class TimetableController {

	private final TimetableService timetableService;

	public TimetableController(TimetableService timetableService) {
		this.timetableService = timetableService;
	}

	@GetMapping("timetables")
	public String getAll(Model model) {
		model.addAttribute("timetables", timetableService.findAll());
		return "timetables/timetables";
	}

	@GetMapping("timetables/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("timetable", timetableService.findById(id).orElseThrow(NoSuchElementException::new));
		return "timetables/timetable";
	}
}
