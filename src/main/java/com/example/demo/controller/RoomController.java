package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Room;
import com.example.demo.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class RoomController {
	@Autowired
	private RoomService roomService;
	
	@GetMapping("")
	public String door() {
		return "room/door";
	}
	
	@GetMapping("/room")
	public String room(@RequestParam("token") String token, Model model, HttpSession session) {
		Room room = roomService.findByToken(token);
		if (room == null) {
			return "errorPage"; // 잘못된 토큰
		}
		String username = (String) session.getAttribute("userName");
		
		if (username == null)	// 테스트용
			username = "testname";
		
		model.addAttribute("janusNum", room.getJanusNum());
		model.addAttribute("username", username);
		return "room/videoroom";
	}
}
