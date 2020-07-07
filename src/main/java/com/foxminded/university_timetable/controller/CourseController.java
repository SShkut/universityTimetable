package com.foxminded.university_timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.foxminded.university_timetable.service.CourseService;

@Controller
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/course")
	public String courses(Model model) {
		model.addAttribute("courses", courseService.findAll());
		return "courses";
	}
}
