package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.CourseService;

@Controller
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("courses")
	public String getAll(Model model) {
		model.addAttribute("courses", courseService.findAll());
		return "courses/courses";
	}

	@GetMapping("courses/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("course", courseService.findById(id).orElseThrow(NoSuchElementException::new));
		return "courses/course";
	}
}
