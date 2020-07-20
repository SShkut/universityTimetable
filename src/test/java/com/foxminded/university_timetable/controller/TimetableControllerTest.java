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

import com.foxminded.university_timetable.model.Timetable;
import com.foxminded.university_timetable.service.TimetableService;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {
	
	private MockMvc mockMvc;

	@Mock
	private TimetableService timetableService;

	@InjectMocks
	private TimetableController timetableController;

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(timetableController).build();
	}

	@Test
	void givenModel_whenGiveAll_thenShowTimetables() throws Exception {
		List<Timetable> timetables = new ArrayList<>();
		timetables.add(new Timetable(1L, "active"));
		timetables.add(new Timetable(2L, "archive"));
		when(timetableService.findAll()).thenReturn(timetables);

		mockMvc.perform(get("/timetables"))
			.andExpect(status().isOk())
			.andExpect(view().name("timetables/timetables"))
			.andExpect(model().attribute("timetables", hasSize(2)));

		verify(timetableService).findAll();
	}

	@Test
	void givenModel_whenGiveById_thenShowTimetable() throws Exception {
		Optional<Timetable> timetable = Optional.of(new Timetable(1L, "active"));
		when(timetableService.findById(1L)).thenReturn(timetable);
		
		mockMvc.perform(get("/timetables/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("timetables/timetable"))
			.andExpect(model().attributeExists("timetable"));
		
		verify(timetableService).findById(1L);
	}
}
