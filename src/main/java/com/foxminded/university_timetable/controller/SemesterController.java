package com.foxminded.university_timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.foxminded.university_timetable.service.SemesterService;

@Controller
public class SemesterController {

	private final SemesterService semesterService;

	public SemesterController(SemesterService semesterService) {
		this.semesterService = semesterService;
	}

	@GetMapping("semesters")
	public String getAll(Model model) {
		model.addAttribute("semesters", semesterService.findAll());
		return "semesters/semesters";
	}
}
