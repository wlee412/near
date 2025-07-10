package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.RoomMapper;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRecording;

@Service
public class RoomService {
	@Autowired
	private RoomMapper roomMapper;
	
	public Room findByToken(String token) {
		return roomMapper.findByToken(token);
	}
	
	public void uploadRec(RoomRecording rec) {
		roomMapper.uploadRec(rec);
	}

}
