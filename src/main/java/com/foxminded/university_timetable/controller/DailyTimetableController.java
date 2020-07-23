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
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.service.DailyTimetableService;
import com.foxminded.university_timetable.service.StudentService;
import com.foxminded.university_timetable.service.TeacherService;

@Controller
public class DailyTimetableController {

	private final DailyTimetableService dailyTimetableService;
	private final StudentService studentService;
	private final TeacherService teacherService;

	public DailyTimetableController(DailyTimetableService dailyTimetableService, StudentService studentService,
			TeacherService teacherService) {
		this.dailyTimetableService = dailyTimetableService;
		this.studentService = studentService;
		this.teacherService = teacherService;
	}

	@GetMapping("daily-timetables")
	public String getAll(Model model) {
		model.addAttribute("dailyTimetables", dailyTimetableService.findAll());
		return "daily-timetables/daily-timetables";
	}

	@GetMapping("daily-timetables/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("dailyTimetable", dailyTimetableService.findById(id).orElseThrow(
				() -> new NoSuchElementException(String.format("Daily timetable with id: %d does not exist", id))));
		return "daily-timetables/daily-timetable";
	}

	@GetMapping("daily-timetables/student/{id}")
	public String findTimetableForStudent(@ModelAttribute("dateRange") DailyTimetableDateRangeCommand dateRangeCommand,
			Model model, @PathVariable Long id) {
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		Student student = studentService.findById(id)
				.orElseThrow(() -> new NoSuchElementException(String.format("Student with id: %d does not exist", id)));
		if (dateRangeCommand.getDateFrom() != null || dateRangeCommand.getDateTo() != null) {
			dailyTimetables = dailyTimetableService
					.findTimetableForStudent(student, dateRangeCommand.getDateFrom(), dateRangeCommand.getDateTo());
		}
		model.addAttribute("dailyTimetables", dailyTimetables);
		model.addAttribute("student", student);
		return "daily-timetables/student-timetables";
	}

	@GetMapping("daily-timetables/teacher/{id}")
	public String findTimetableForTeacher(@ModelAttribute("dateRange") DailyTimetableDateRangeCommand dateRangeCommand,
			Model model, @PathVariable Long id) {
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		Teacher teacher = teacherService.findById(id)
				.orElseThrow(() -> new NoSuchElementException(String.format("Teacher with id: %d does not exist", id)));
		if (dateRangeCommand.getDateFrom() != null || dateRangeCommand.getDateTo() != null) {
			dailyTimetables = dailyTimetableService
					.findTimetableForTeacher(teacher, dateRangeCommand.getDateFrom(), dateRangeCommand.getDateTo());
		}
		model.addAttribute("dailyTimetables", dailyTimetables);
		model.addAttribute("teacher", teacher);
		return "daily-timetables/teacher-timetables";
	}
}
