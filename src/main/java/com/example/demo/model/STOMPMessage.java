package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class STOMPMessage {
	private String type; // draw, modify, remove, clear …
	private String roomId;
	private String sender;
	private String payload; // json을 압축한 Base64 문자열
}