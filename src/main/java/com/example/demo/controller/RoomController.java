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
	public String main(HttpSession session) {
		session.setAttribute("loginCounselor", "test_counselor");
//		session.invalidate();
		return "room/door";
	}

	// 토큰과 로그인 세션 체크
	@GetMapping("/token")
	public String whoRU(@RequestParam("t") String token, Model model, HttpSession session) {
		// 토큰 입력 -> 로그인 -> 돌아옴
		if (token == null) {
			token = (String) session.getAttribute("roomToken");
			session.removeAttribute("roomToken");
		}
		
		//방 정보
		Room room = roomService.findByToken(token);
		
		// 토큰 틀림
		if (room == null) {
			model.addAttribute("msg", "유효하지 않은 상담방 토큰입니다.");
			return "room/errmsg"; // 잘못된 토큰
		}
		
		// 방 번호 전달
		model.addAttribute("roomId", room.getRoomId());

		String client = (String) session.getAttribute("loginClient");
		String counselor = (String) session.getAttribute("loginCounselor");

		// 내담자 세션
		if (client != null && counselor == null) {
			if (client.equals(room.getClientId())) {
				model.addAttribute("who", "client");
				return "room/whoru"; // auto submit: rooId, who
			} else {
				model.addAttribute("msg", "유효하지 않은 상담방입니다.");
				return "room/errmsg";
			}
			
		// 상담사 세션
		} else if (client == null && counselor != null) {
			if (counselor.equals(room.getCounselorId())) {
				model.addAttribute("who", "counselor");
				return "room/whoru";
			} else {
				model.addAttribute("msg", "유효하지 않은 상담방입니다.");
				return "room/errmsg";
			}
		}
		
		// 세션 없음 - 로그인
		session.setAttribute("roomToken", token);
		return "redirect:/client/login";
	}

	// 상담실 정보, 예약정보 보여준 후 webRTC 화상채팅
	@PostMapping("/room")
	public String room(@RequestParam("roomId") int roomId, @RequestParam("who") String who, Model model,
			HttpSession session) {
		Room room = roomService.findByRoomId(roomId);
		if (room == null) {
			model.addAttribute("msg", "유효하지 않은 상담방입니다.");
			return "room/errmsg";
		}
		if (!room.getState().equals("진행")) {
			model.addAttribute("msg", "상담 시작 10분 전부터 입장 가능합니다.");
			model.addAttribute("goto", "/chat");
			return "room/errmsg";
		}
		Reservation rsv = roomService.getReservationInfo(room.getReservationNo());

		String username;
		String opponent;
		switch (who) {
		case "client":
			username = rsv.getClientName();
			opponent = rsv.getCounselorName();
			break;
		case "counselor":
			username = rsv.getCounselorName();
			opponent = rsv.getClientName();
			break;
		default:
			model.addAttribute("msg", "유효하지 않은 사용자입니다.");
			return "room/errmsg";
		}

		model.addAttribute("username", username);
		model.addAttribute("opponent", opponent);
		model.addAttribute("who", who);
		model.addAttribute("room", room);
		model.addAttribute("rsv", rsv);
		return "room/videoroom";
	}
	
	// 영상 녹화
	@ResponseBody
	@PostMapping("/rec")
	public ResponseEntity<String> uploadRecording(@RequestParam("file") MultipartFile file,
			@ModelAttribute RoomRecording rec) {

		String savePath = "C:/recordings/"; // 배포 전 수정 필요: /home/ubuntu/
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
