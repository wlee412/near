package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.STOMPMessage;

@Controller
public class STOMPController {

	private final SimpMessagingTemplate tpl;

	public STOMPController(SimpMessagingTemplate tpl) {
		this.tpl = tpl;
	}

	@MessageMapping("/whiteboard/{roomId}")
	public void whiteboard(@Payload STOMPMessage msg, @DestinationVariable("roomId") String roomId) {
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

	@MessageMapping("/textmsg/{roomId}")
	public void textmsg(@Payload STOMPMessage msg, @DestinationVariable("roomId") String roomId) {
		System.out.println("sender:" + msg.getSender());
		System.out.println("type:" + msg.getType());
		System.out.println("text:" + msg.getPayload());
		switch (msg.getType()) {
		case "chat":
			tpl.convertAndSend("/topic/textmsg/" + msg.getRoomId(), msg);
			break;
		default:
			System.out.println("unknown msg");
		}
	}
}