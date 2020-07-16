package com.foxminded.university_timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String getStartPage() {
		return "index";
	}
}
