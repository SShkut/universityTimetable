package com.foxminded.university_timetable.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foxminded.university_timetable.service.GroupService;

@Controller
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping("groups")
	public String getAll(Model model) {
		model.addAttribute("groups", groupService.findAll());
		return "groups/groups";
	}

	@GetMapping("groups/{id}")
	public String getById(Model model, @PathVariable Long id) {
		model.addAttribute("group", groupService.findById(id)
				.orElseThrow(() -> new NoSuchElementException(String.format("Group with id: %d does not exist", id))));
		return "groups/group";
	}
}
