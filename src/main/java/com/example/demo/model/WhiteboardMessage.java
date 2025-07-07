package com.example.demo.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhiteboardMessage {
	private String type; // draw, modify, remove, clear …
	private String roomId;
	private String sender;
	private Map<String, Object> payload; // fabric.js JSON
}