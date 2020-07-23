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

import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.service.GroupService;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

	private MockMvc mockMvc;

	@Mock
	GroupService groupService;

	@InjectMocks
	GroupController groupController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
	}
	
	@Test
	void givenModel_whenFindAll_thenShowAllGroups() throws Exception {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1L, "g1", "cs", "cs", new Semester(), new ArrayList<>()));
		groups.add(new Group(2L, "g2", "cs", "cs", new Semester(), new ArrayList<>()));
		when(groupService.findAll()).thenReturn(groups);

		mockMvc.perform(get("/groups"))
			.andExpect(status().isOk())
			.andExpect(view().name("groups/groups"))
			.andExpect(model().attribute("groups", hasSize(2)));
	}

	@Test
	void givenModel_whenFindById_thenShowGroup() throws Exception {
		Group expected = new Group(1L, "g1", "cs", "cs", new Semester(), new ArrayList<>());
		Optional<Group> group = Optional.of(expected);
		when(groupService.findById(1L)).thenReturn(group);

		mockMvc.perform(get("/groups/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("groups/group"))
			.andExpect(model().attributeExists("group"))
			.andExpect(model().attribute("group", expected));
	}
}
