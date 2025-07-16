package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Client;
import com.example.demo.model.Counselor;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.service.RoomService;
import com.example.demo.util.EmailTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
public class RoomController {
	@Autowired
	private RoomService roomService;

	@GetMapping("")
	public String main(HttpSession session) {
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

		// 방 정보
		Room room = roomService.findByToken(token);

		// 토큰 틀림
		if (room == null) {
			model.addAttribute("msg", "유효하지 않은 상담방 토큰입니다.");
			model.addAttribute("path", "/room");
			return "room/errmsg"; // 잘못된 토큰
		}

		// 방 번호 전달
		model.addAttribute("roomId", room.getRoomId());

		Client client = (Client) session.getAttribute("loginClient");
		Counselor counselor = (Counselor) session.getAttribute("loginCounselor");

		// 내담자 세션
		if (client != null && counselor == null) {
			if (client.getClientId().equals(room.getClientId())) {
				model.addAttribute("who", "client");
				return "room/whoru"; // auto submit: rooId, who
			} else {
				model.addAttribute("msg", "유효하지 않은 상담방입니다.");
				model.addAttribute("path", "/room");
				return "room/errmsg";
			}

			// 상담사 세션
		} else if (client == null && counselor != null) {
			if (counselor.getCounselorId().equals(room.getCounselorId())) {
				model.addAttribute("who", "counselor");
				return "room/whoru";
			} else {
				model.addAttribute("msg", "유효하지 않은 상담방입니다.");
				model.addAttribute("path", "/room");
				return "room/errmsg";
			}
		}

		return "redirect:/client/login";
	}

	// 상담실 정보, 예약정보 보여준 후 webRTC 화상채팅
	@PostMapping("/video")
	public String room(@RequestParam("roomId") int roomId, @RequestParam("who") String who, Model model,
			HttpSession session) {
		Room room = roomService.findByRoomId(roomId);
		if (room == null) {
			model.addAttribute("msg", "유효하지 않은 상담방입니다.");
			return "room/errmsg";
		}
		if (!room.getState().equals("진행")) {
			model.addAttribute("msg", "상담 시작 10분 전부터 입장 가능합니다.");
			model.addAttribute("path", "/room");
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

	@PostMapping("/mail")
	@ResponseBody
	public String emailTest() throws IOException {
		System.out.println("토큰 발송");
		List<Reservation> rsvList = roomService.getBooked();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy년 M월 d일 E요일 a h:mm");
		EmailTemplate emailTpl = new EmailTemplate("roomTokenEmail.html");

		System.out.println(rsvList.size());
		for (Reservation rsv : rsvList) {
			Map<String, String> data = Map.of("start_time", sd.format(rsv.getStartDate()), "room_token",
					rsv.getRoomToken(), "counselor_name", rsv.getCounselorName(), "client_name", rsv.getClientName(),
					"opponent_phone", "상담사 전화번호: " + rsv.getCounselorPhone());

			// 내담자에게 발송
			String htmlContent = emailTpl.loadEmailTemplate(data);
			roomService.sendEmailToken(rsv.getClientEmail(), htmlContent);

			// 상담사에게 발송 (상대 전화번호만 교체)
			Map<String, String> data2 = new HashMap<>(data);
			data2.put("opponent_phone", "내담자 전화번호: " + rsv.getClientPhone());
			String htmlContent2 = emailTpl.loadEmailTemplate(data2);
			roomService.sendEmailToken(rsv.getCounselorEmail(), htmlContent2);
		}
		return "이메일 발송 완료";
	}
}
