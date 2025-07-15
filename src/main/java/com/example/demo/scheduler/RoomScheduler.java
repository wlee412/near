package com.example.demo.scheduler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.service.RoomService;
import com.example.demo.util.EmailTemplate;

public class RoomScheduler {
	@Autowired
	private RoomService roomService;

	private final SimpMessagingTemplate tpl;

	public RoomScheduler(SimpMessagingTemplate tpl) {
		this.tpl = tpl;
	}

	@Scheduled(cron = "0 35 * * * *")
	public void notifyRoomEnd() {
		List<Room> expiredRooms = roomService.expireRooms();

		for (Room room : expiredRooms) {
			Map<String, Object> payload = Map.of("type", "expired", "roomId", room.getRoomId());
			tpl.convertAndSend("/topic/room/expired/" + room.getRoomId(), payload);
		}
	}

	@Scheduled(cron = "0 40 * * * *")
	public void sendRoomToken() throws IOException {
		List<Reservation> rsvList = roomService.getBooked();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy년 M월 d일 E요일 a h:mm");
		EmailTemplate emailTpl = new EmailTemplate("roomTokenEmail.html");

		for (Reservation rsv : rsvList) {
		    Map<String, String> data = Map.of(
		        "start_time", sd.format(rsv.getStartDate()),
		        "room_token", rsv.getRoomToken(),
		        "counselor_name", rsv.getCounselorName(),
		        "client_name", rsv.getClientName(),
		        "opponent_phone", "상담사 전화번호: " + rsv.getCounselorPhone()
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
	}

}
