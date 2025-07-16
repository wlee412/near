package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.RoomMapper;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;

import jakarta.mail.internet.MimeMessage;

@Service
public class RoomService {
	@Autowired
	private RoomMapper roomMapper;

	@Autowired
	private JavaMailSender mailSender;

	public Room findByToken(String token) {
		return roomMapper.findByToken(token);
	}

	public Room findByRoomId(int roomId) {
		return roomMapper.findByRoomId(roomId);
	}

	public Reservation getReservationInfo(int reservationNo) {
		return roomMapper.getReservationInfo(reservationNo);
	}

	public List<Reservation> getBooked() {
		return roomMapper.getBooked();
	}

	public void createRoom(int reservationNo, String counselorId, String clientId, LocalDateTime start) {
		List<Integer> usedJanusNums = roomMapper.janusNums();
		Set<Integer> usedSet = new HashSet<>(usedJanusNums);

		int janusNum;
		do {
			janusNum = (int) (Math.random() * 10000) + 10000;
		} while (usedSet.contains(janusNum));

		Room room = new Room();
		room.setRoomToken(UUID.randomUUID().toString());
		room.setReservationNo(reservationNo);
		room.setCounselorId(counselorId);
		room.setClientId(clientId);
		room.setStart(start);
		room.setEnd(start.plusMinutes(30));
		room.setState("예약");
		room.setJanusNum(janusNum);
	}

	public List<Room> expireRooms() {
		List<Room> ExpiredRooms = roomMapper.getExpiredRooms();
		roomMapper.expireRooms(); // 방 상태: 진행 -> 완료
		return ExpiredRooms;
	}

	public void sendEmailToken(String to, String htmlContent) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setTo(to);
			helper.setSubject("n:ear 상담 알림");
			helper.setText(htmlContent, true);
			helper.setFrom("2j1william@gmail.com");

			mailSender.send(message);
			System.out.println("이메일 전송 완료: " + to);
		} catch (Exception e) {
			System.out.println("이메일 전송 실패: " + to);
			e.printStackTrace();
		}
	}

	public void roomOpen() {
		roomMapper.roomOpen();
	}
}
