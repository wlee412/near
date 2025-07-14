package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.RoomMapper;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRecording;

@Service
public class RoomService {
	@Autowired
	private RoomMapper roomMapper;
	
	public Room findByToken(String token) {
		return roomMapper.findByToken(token);
	}
	
	public Room findByRoomId(int roomId) {
		return roomMapper.findByRoomId(roomId);
	}
	
	public void uploadRec(RoomRecording rec) {
		roomMapper.uploadRec(rec);
	}

	public Reservation getReservationInfo(int reservationNo) {
		return roomMapper.getReservationInfo(reservationNo);
	}
	
	public List<Reservation> getBooked() {
		return roomMapper.getBooked();
	}
	
	public void createRoom(int rsv, String counselor, String client, LocalDateTime start) {
		List<Integer> usedJanusNums = roomMapper.janusNums();
		Set<Integer> usedSet = new HashSet<>(usedJanusNums);

		int janusNum;
		do {
		    janusNum = (int)(Math.random() * 10000) + 10000;
		} while (usedSet.contains(janusNum));

		Room room = new Room();
		room.setRoomToken(UUID.randomUUID().toString());
		room.setReservationNo(rsv);
		room.setCounselorId(counselor);
		room.setClientId(client);
		room.setStart(start);
		room.setEnd(start.plusMinutes(30));
		room.setState("예약");
		room.setJanusNum(janusNum);
	}

	public List<Room> expireRooms() {
		List<Room> ExpiredRooms = roomMapper.getExpiredRooms();
		roomMapper.expireRooms();	// 방 상태: 진행 -> 완료 
		return ExpiredRooms;
	}
}
