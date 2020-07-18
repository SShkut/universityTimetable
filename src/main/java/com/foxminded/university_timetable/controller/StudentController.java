package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.StudentService;

@Controller
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("students")
	public String getAll(Model model) {
		model.addAttribute("students", studentService.findAll());
		return "students/students";
	}

	@GetMapping("students/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("student", studentService.findById(id).orElseThrow(NoSuchElementException::new));
		return "students/student";
	}
}
