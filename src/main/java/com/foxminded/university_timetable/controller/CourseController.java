package com.foxminded.university_timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}
}
