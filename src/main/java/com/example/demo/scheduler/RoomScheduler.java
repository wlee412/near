package com.example.demo.scheduler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.service.RoomService;

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
			Map<String, Object> payload = Map.of(
					"type", "expired", 
					"roomId", room.getRoomId()
					);
			tpl.convertAndSend("/topic/room/expired/" + room.getRoomId(), payload);
		}
	}
	
	@Scheduled(cron = "0 40 * * * *")
	public void sendRoomToken() {
		List<Reservation> rsv = roomService.getBooked();
		// 이메일 전송
	}

}
