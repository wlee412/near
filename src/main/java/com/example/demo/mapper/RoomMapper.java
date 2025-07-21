package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;

@Mapper
public interface RoomMapper {

	public Room findByToken(String token);

	public Room findByRoomId(int roomId);

	public Reservation getReservationInfo(int reservationNo);

	public List<Reservation> getBooked();

	public List<Integer> janusNums();

	public List<Room> getExpiredRooms();

	public void expireRooms();

	public void roomOpen();

	public void createRoom(Room room);

	public void cancelRoom(int reservationNo);

}