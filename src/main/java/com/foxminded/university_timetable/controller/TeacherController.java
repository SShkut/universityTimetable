package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.TeacherService;

@Controller
public class TeacherController {

	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping("teachers")
	public String getAll(Model model) {
		model.addAttribute("teachers", teacherService.findAll());
		return "teachers/teachers";
	}

	@GetMapping("teachers/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("teacher", teacherService.findById(id).orElseThrow(NoSuchElementException::new));
		return "teachers/teacher";
	}
}
