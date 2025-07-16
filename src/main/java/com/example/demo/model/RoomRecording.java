package com.example.demo.model;

import java.security.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("rec")
public class RoomRecording {
	private String name;
	private int roomId;
	private Timestamp reg_date;
}
