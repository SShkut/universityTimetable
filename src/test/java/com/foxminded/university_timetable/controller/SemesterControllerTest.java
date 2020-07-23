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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.service.SemesterService;

@ExtendWith(MockitoExtension.class)
class SemesterControllerTest {

	private MockMvc mockMvc;

	@Mock
	private SemesterService semesterService;

	@InjectMocks
	private SemesterController semesterController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(semesterController).build();
	}

	@Test
	void gievenModel_whenGiveAll_thenShowSemesters() throws Exception {
		List<Semester> semesters = new ArrayList<>();
		semesters.add( new Semester(1L, 2020, "summer"));
		semesters.add( new Semester(1L, 2020, "winter"));
		when(semesterService.findAll()).thenReturn(semesters);

		mockMvc.perform(get("/semesters"))
			.andExpect(status().isOk())
			.andExpect(view().name("semesters/semesters"))
			.andExpect(model().attribute("semesters", hasSize(2)));

		verify(semesterService).findAll();
	}
}
