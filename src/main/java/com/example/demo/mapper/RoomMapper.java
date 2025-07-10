package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRecording;

@Mapper
public interface RoomMapper {

	public Room findByToken(String token);

	public void uploadRec(RoomRecording rec);

	public Reservation getReservationInfo(int reservationNo);

}
