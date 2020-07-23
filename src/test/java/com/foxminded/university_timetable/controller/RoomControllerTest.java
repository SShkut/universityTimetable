package com.foxminded.university_timetable.controller;

import static org.hamcrest.Matchers.hasSize;
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

import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.service.RoomService;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

	private MockMvc mockMvc;

	@Mock
	private RoomService roomService;

	@InjectMocks
	private RoomController roomController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
	}

	@Test
	void givenModel_whenFindAll_thenShowAllRooms() throws Exception {
		List<Room> rooms = new ArrayList<>();
		rooms.add(new Room(1L, "1-b", 20));
		rooms.add(new Room(2L, "1-c", 40));
		when(roomService.findAll()).thenReturn(rooms);
		
		mockMvc.perform(get("/rooms"))
			.andExpect(status().isOk())
			.andExpect(view().name("rooms/rooms"))
			.andExpect(model().attribute("rooms", hasSize(2)));
	}
}
