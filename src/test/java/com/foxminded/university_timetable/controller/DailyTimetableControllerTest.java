package com.foxminded.university_timetable.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
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

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.service.DailyTimetableService;

@ExtendWith(MockitoExtension.class)
class DailyTimetableControllerTest {

	private MockMvc mockMvc;

	@Mock
	DailyTimetableService dailyTimetableService;

	@InjectMocks
	DailyTimetableController dailyTimetableController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(dailyTimetableController).build();
	}
	
	@Test
	void givenModel_whenFindAll_thenShowAllDailyTimetables() throws Exception {
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		dailyTimetables.add(new DailyTimetable(1L, LocalDate.now(), new ArrayList<>()));
		dailyTimetables.add(new DailyTimetable(2L, LocalDate.now(), new ArrayList<>()));
		when(dailyTimetableService.findAll()).thenReturn(dailyTimetables);

		mockMvc.perform(get("/daily-timetables"))
			.andExpect(status().isOk())
			.andExpect(view().name("daily-timetables/daily-timetables"))
			.andExpect(model().attribute("dailyTimetables", hasSize(2)));

		verify(dailyTimetableService).findAll();

	}

	@Test
	void givenModel_whenFindById_thenShowDailyTimetable() throws Exception {
		Optional<DailyTimetable> dailyTimetable = Optional.of(new DailyTimetable(1L, LocalDate.now(), new ArrayList<>()));
		when(dailyTimetableService.findById(1L)).thenReturn(dailyTimetable);

		mockMvc.perform(get("/daily-timetables/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("daily-timetables/daily-timetable"))
			.andExpect(model().attributeExists("dailyTimetable"));

		verify(dailyTimetableService).findById(1L);
	}
}
