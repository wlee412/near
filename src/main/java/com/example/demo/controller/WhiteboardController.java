package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.WhiteboardMessage;

@Controller
public class WhiteboardController {

	private final SimpMessagingTemplate tpl;

	public WhiteboardController(SimpMessagingTemplate tpl) {
		this.tpl = tpl;
	}

	@MessageMapping("/whiteboard/{roomId}")
	public void handle(@Payload WhiteboardMessage msg, @DestinationVariable("roomId") String roomId) {
		// msg.getType() 으로 draw/add/modify/remove/clear 분기
		switch (msg.getType()) {
		case "draw":
//		case "add":
		case "modify":
		case "remove":
		case "clear":
		case "bg":
			tpl.convertAndSend("/topic/whiteboard/" + msg.getRoomId(), msg);
			break;
		default:
			System.out.println("unknown msg");
		}
	}
}