package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.WhiteboardMessage;

@Controller
public class WhiteboardController {
	private final SimpMessagingTemplate tpl;

	public WhiteboardController(SimpMessagingTemplate tpl) {
		this.tpl = tpl;
	}

	@MessageMapping("/draw")
	public void onDraw(WhiteboardMessage msg) {
		// 그대로 같은 방의 모든 구독자에게 브로드캐스트
		tpl.convertAndSend("/topic/whiteboard/" + msg.getRoomId(), msg);
	}
}