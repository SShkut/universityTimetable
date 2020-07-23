package com.foxminded.university_timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.foxminded.university_timetable.service.RoomService;

@Controller
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping("/rooms")
	public String getAll(Model model) {
		model.addAttribute("rooms", roomService.findAll());
		return "rooms/rooms";
	}
}
