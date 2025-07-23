package com.example.demo.scheduler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.service.RoomService;
import com.example.demo.task.EmailSend;
import com.example.demo.util.EmailTemplate;

@Component
public class RoomScheduler {
	@Autowired
	private RoomService roomService;
	@Autowired
	private EmailSend emailSend;

	private final SimpMessagingTemplate tpl;

	public RoomScheduler(SimpMessagingTemplate tpl) {
		this.tpl = tpl;
	}

	@Scheduled(cron = "0 35 * * * *")
	public void notifyRoomEnd() {
		List<Room> expiredRooms = roomService.expireRooms();

		for (Room room : expiredRooms) {
			Map<String, Object> payload = Map.of("type", "expired", "roomId", room.getRoomId());
			tpl.convertAndSend("/topic/expired/" + room.getRoomId(), payload);
		}
	}

	@Scheduled(cron = "0 30 * * * *")
	public void sendRoomToken() throws IOException {
		System.out.println("토큰 발송");
		emailSend.reservationMail();
	}
	
	@Scheduled(cron = "0 49 * * * *")
	public void roomOpen() {
		roomService.roomOpen();
	}

}
