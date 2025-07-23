package com.example.demo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Reservation;
import com.example.demo.service.RoomService;
import com.example.demo.task.EmailSend;
import com.example.demo.util.EmailTemplate;

@RequestMapping("/mail")
@RestController
public class EmailController {
	@Autowired
	private EmailSend emailSend;
	@Autowired
	private RoomService roomService;
	
	@PostMapping("/rsv")
	public String reservation() throws IOException {
		emailSend.reservationMail();
		return "예약 메일 발송";
	}
	
	@PostMapping("/testrsv")
	public String testReservation() throws IOException {
		List<Reservation> rsvList = roomService.testGetBooked();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy년 M월 d일 E요일 a h:mm");
		EmailTemplate emailTpl = new EmailTemplate("roomTokenEmail.html");

		for (Reservation rsv : rsvList) {
		    Map<String, String> data = Map.of(
		        "start_time", sd.format(rsv.getStartDate()),
		        "room_token", rsv.getRoomToken(),
		        "counselor_name", rsv.getCounselorName(),
		        "client_name", rsv.getClientName(),
		        "opponent_phone", "상담사 전화번호: " + rsv.getCounselorPhone(),
		        "url", "https://js1.jsflux.co.kr"
		    );

		    // 내담자에게 발송
		    String htmlContent = emailTpl.loadEmailTemplate(data);
		    roomService.sendEmailToken(rsv.getClientEmail(), htmlContent);

		    // 상담사에게 발송 (상대 전화번호만 교체)
		    Map<String, String> data2 = new HashMap<>(data);
		    data2.put("opponent_phone", "내담자 전화번호: " + rsv.getClientPhone());
		    String htmlContent2 = emailTpl.loadEmailTemplate(data2);
		    roomService.sendEmailToken(rsv.getCounselorEmail(), htmlContent2);
		}
		return "예약 메일 테스트";
	}
}
