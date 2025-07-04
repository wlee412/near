package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Room;

@Mapper
public interface RoomMapper {

	public Room findByToken(String token);

}
