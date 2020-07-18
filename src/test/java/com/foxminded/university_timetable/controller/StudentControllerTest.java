package com.foxminded.university_timetable.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
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

import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

	private MockMvc mockMvc;

	@Mock
	private StudentService studentService;

	@InjectMocks
	private StudentController studentController;

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
	}

	@Test
	void givenModel_whenGiveAll_thenShowStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		students.add(new Student(1L, "", "", "", "", "", ""));
		students.add(new Student(2L, "", "", "", "", "", ""));
		when(studentService.findAll()).thenReturn(students);

		mockMvc.perform(get("/students"))
		.andExpect(status().isOk())
		.andExpect(view().name("students/students"))
		.andExpect(model().attribute("students", hasSize(2)));

		verify(studentService).findAll();
	}

	@Test
	void givenModel_whenGiveById_thenShowStudent() throws Exception {
		Optional<Student> student = Optional.of(new Student(1L, "", "", "", "", "", ""));
		when(studentService.findById(1L)).thenReturn(student);
		
		mockMvc.perform(get("/students/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("students/student"))
			.andExpect(model().attributeExists("student"));
		
		verify(studentService).findById(1L);
	}
}
