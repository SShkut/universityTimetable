package com.foxminded.university_timetable.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TeacherService teacherService;

	@InjectMocks
	private TeacherController teacherController;

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
	}

	@Test
	void givenModel_whenGiveAll_thenShowTeachers() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(new Teacher(1L, "", "", "", "", "", ""));
		teachers.add(new Teacher(2L, "", "", "", "", "", ""));
		when(teacherService.findAll()).thenReturn(teachers);

		mockMvc.perform(get("/teachers"))
			.andExpect(status().isOk())
			.andExpect(view().name("teachers/teachers"))
			.andExpect(model().attribute("teachers", hasSize(2)));
	}

	@Test
	void givenModel_whenGiveById_thenShowTeacher() throws Exception {
		Teacher expected = new Teacher(1L, "", "", "", "", "", "");
		Optional<Teacher> teacher = Optional.of(expected);
		when(teacherService.findById(1L)).thenReturn(teacher);
		
		mockMvc.perform(get("/teachers/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("teachers/teacher"))
			.andExpect(model().attributeExists("teacher"))
			.andExpect(model().attribute("teacher", expected));
	}
}
