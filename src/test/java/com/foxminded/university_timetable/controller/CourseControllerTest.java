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

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.service.CourseService;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

	private MockMvc mockMvc;

	@Mock
	CourseService courseService;

	@InjectMocks
	CourseController courseController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
	}

	@Test
	void givenModel_whenFindAll_thenShowAllCourses() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course("CS", new ArrayList<>()));
		courses.add(new Course("Math", new ArrayList<>()));
		when(courseService.findAll()).thenReturn(courses);

		mockMvc.perform(get("/courses"))
			.andExpect(status().isOk())
			.andExpect(view().name("courses/courses"))
			.andExpect(model().attribute("courses", hasSize(2)));
	}

	@Test
	void givenModel_whenFindById_thenShowCourse() throws Exception {
		Course expected = new Course(1L, "CS", new ArrayList<>());
		Optional<Course> course = Optional.of(expected);
		when(courseService.findById(1L)).thenReturn(course);

		mockMvc.perform(get("/courses/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/course"))
				.andExpect(model().attributeExists("course"))
				.andExpect(model().attribute("course", expected))
				.andReturn();
	}
}
