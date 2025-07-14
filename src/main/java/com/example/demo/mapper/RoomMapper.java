package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRecording;

@Mapper
public interface RoomMapper {

	public Room findByToken(String token);

	public Room findByRoomId(int roomId);

	public void uploadRec(RoomRecording rec);

	public Reservation getReservationInfo(int reservationNo);

	public List<Integer> janusNums();

}