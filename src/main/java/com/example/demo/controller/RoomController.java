package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRecording;
import com.example.demo.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class RoomController {
	@Autowired
	private RoomService roomService;

	@GetMapping("")
	public String main() {
		return "room/door";
	}
	
	

	@GetMapping("/room")
	public String room(@RequestParam("token") String token, Model model, HttpSession session) {
		Room room = roomService.findByToken(token);
		if (room == null) {
			return "errorPage"; // 잘못된 토큰
		}
		Reservation rsv = roomService.getReservationInfo(room.getReservationNo());
		String username = (String) session.getAttribute("userName");

		if (username == null) // 테스트용
			username = UUID.randomUUID().toString().split("-")[1];

		model.addAttribute("room", room);
		model.addAttribute("rsv", rsv);
		model.addAttribute("username", username);
		return "room/videoroom";
	}

	@ResponseBody
	@PostMapping("/rec")
	public ResponseEntity<String> uploadRecording(@RequestParam("file") MultipartFile file,
			@ModelAttribute RoomRecording rec) {

		String savePath = "C:/recordings/";
		String filename = UUID.randomUUID() + ".webm";
		Path path = Paths.get(savePath + filename);

		try {
			Files.copy(file.getInputStream(), path);
			rec.setName(filename);
			roomService.uploadRec(rec);
			return ResponseEntity.ok("저장 완료: " + filename);
		} catch (IOException ioe) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
		}
	}

}
